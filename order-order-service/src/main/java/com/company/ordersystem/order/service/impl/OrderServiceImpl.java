package com.company.ordersystem.order.service.impl;

import com.company.ordersystem.common.constant.EOrderStatus;
import com.company.ordersystem.common.dto.OrderItemDTO;
import com.company.ordersystem.common.dto.OrderRequestDTO;
import com.company.ordersystem.common.dto.OrderResponseDTO;
import com.company.ordersystem.common.dto.OrderStatusUpdateRequestDTO;
import com.company.ordersystem.common.entity.OrderEntity;
import com.company.ordersystem.common.entity.OrderItemEntity;
import com.company.ordersystem.common.entity.ProductEntity;
import com.company.ordersystem.common.entity.UserEntity;
import com.company.ordersystem.common.exception.InsufficientStockException;
import com.company.ordersystem.common.exception.OrderNotFoundException;
import com.company.ordersystem.common.exception.OrderValidationException;
import com.company.ordersystem.common.exception.ProductNotFoundException;
import com.company.ordersystem.common.exception.UserNotFoundException;
import com.company.ordersystem.common.mapper.OrderMapper;
import com.company.ordersystem.order.repository.OrderItemRepository;
import com.company.ordersystem.order.repository.OrderRepository;
import com.company.ordersystem.order.repository.ProductRepository;
import com.company.ordersystem.order.repository.UserRepository;
import org.springframework.util.Assert;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements com.company.ordersystem.order.service.OrderService {

    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
    private final OrderMapper orderMapper;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;

    @Override
    @Transactional
    public OrderResponseDTO createOrder(OrderRequestDTO request) {
        // Get current authenticated user
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        UserEntity user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("User not authorized"));
        
        Assert.notNull(request.getItems(), "Order items cannot be null");
        Assert.isTrue(!request.getItems().isEmpty(), "Order must have at least one item");

        // Create order entity
        OrderEntity order = new OrderEntity();
        order.setStatus(EOrderStatus.CREATED);
        order.setCreatedAt(LocalDateTime.now());
        order.setCustomerId(user.getId());
        
        double totalPrice = 0.0;
        List<OrderItemEntity> orderItems = new java.util.ArrayList<>();

        // Process each order item
        for (OrderItemDTO itemDTO : request.getItems()) {
            ProductEntity product = productRepository.findById(itemDTO.getProductId())
                    .orElseThrow(() -> new ProductNotFoundException(itemDTO.getProductId()));
            
            // Validate stock availability
            if (product.getStock() < itemDTO.getQuantity()) {
                throw new InsufficientStockException(
                    itemDTO.getProductId(), 
                    itemDTO.getQuantity(), 
                    product.getStock()
                );
            }

            // Create order item
            OrderItemEntity orderItem = new OrderItemEntity();
            orderItem.setProductId(itemDTO.getProductId());
            orderItem.setQuantity(itemDTO.getQuantity());
            orderItem.setPrice(product.getPrice() * itemDTO.getQuantity());
            totalPrice += orderItem.getPrice();
            orderItem.setOrder(order);
            orderItems.add(orderItem);
            
            // Decrease stock
            product.setStock(product.getStock() - itemDTO.getQuantity());
            productRepository.save(product);
        }

        order.setItems(orderItems);
        order.setTotalPrice(totalPrice);

        OrderEntity savedOrder = orderRepository.save(order);
        
        log.info("Order created with id: {}", savedOrder.getId());
        return orderMapper.toResponseDTO(savedOrder);
    }

    @Override
    public OrderResponseDTO getOrderById(Long id) {
        OrderEntity order = orderRepository.findById(id)
                .orElseThrow(() -> new OrderNotFoundException(id));

        // Verify user can access this order
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        UserEntity user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("User not authorized"));
        
        // Verify ownership (unless admin)
        boolean isAdmin = user.getRoles().stream()
                .anyMatch(role -> role.getName().name().equals("ROLE_ADMIN"));
        
        if (!isAdmin && !order.getCustomerId().equals(user.getId())) {
            throw new OrderValidationException("You cannot access another customer's order");
        }
        
        return orderMapper.toResponseDTO(order);
    }

    @Override
    public List<OrderResponseDTO> getCustomerOrders() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        UserEntity user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("User not authorized"));
        
        List<OrderEntity> orders = orderRepository.findByCustomerId(user.getId());
        return orderMapper.toResponseDTOList(orders);
    }

    @Override
    public List<OrderResponseDTO> getAllOrders() {
        List<OrderEntity> orders = orderRepository.findAll();
        return orderMapper.toResponseDTOList(orders);
    }

    @Override
    @Transactional
    public OrderResponseDTO updateOrderStatus(OrderStatusUpdateRequestDTO request) {
        OrderEntity order = orderRepository.findById(request.getId())
                .orElseThrow(() -> new OrderNotFoundException(request.getId()));

        EOrderStatus currentStatus = order.getStatus();
        EOrderStatus newStatus = request.getStatus();

        // Validate status transition
        validateStatusTransition(currentStatus, newStatus);

        order.setStatus(newStatus);
        order.setUpdatedAt(LocalDateTime.now());

        OrderEntity updated = orderRepository.save(order);
        log.info("Order status updated: orderId={}, status={}", updated.getId(), newStatus);
        
        return orderMapper.toResponseDTO(updated);
    }

    @Override
    @Transactional
    public void cancelOrder(Long id) {
        OrderEntity order = orderRepository.findById(id)
                .orElseThrow(() -> new OrderNotFoundException(id));

        // Verify user can cancel this order
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        UserEntity user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("User not authorized"));
        
        // Verify ownership
        if (!order.getCustomerId().equals(user.getId())) {
            throw new OrderValidationException("You cannot cancel another customer's order");
        }

        if (order.getStatus() == EOrderStatus.DELIVERED) {
            throw new OrderValidationException("Cannot cancel a delivered order");
        }

        if (order.getStatus() == EOrderStatus.CANCELLED) {
            throw new OrderValidationException("Order is already cancelled");
        }

        order.setStatus(EOrderStatus.CANCELLED);
        order.setUpdatedAt(LocalDateTime.now());
        orderRepository.save(order);

        // Restore stock for cancelled order
        for (OrderItemEntity item : order.getItems()) {
            ProductEntity product = productRepository.findById(item.getProductId())
                    .orElseThrow(() -> new ProductNotFoundException(item.getProductId()));
            product.setStock(product.getStock() + item.getQuantity());
            productRepository.save(product);
        }

        log.info("Order cancelled: orderId={}", id);
    }

    @Override
    @Transactional
    public void deleteOrder(Long id) {
        if (!orderRepository.existsById(id)) {
            throw new OrderNotFoundException(id);
        }
        orderRepository.deleteById(id);
        log.info("Order deleted: orderId={}", id);
    }

    private void validateStatusTransition(EOrderStatus currentStatus, EOrderStatus newStatus) {
        if (newStatus == EOrderStatus.CANCELLED) {
            return; // Can cancel from any status (except DELIVERED, handled in cancelOrder)
        }

        switch (currentStatus) {
            case CREATED:
                if (newStatus != EOrderStatus.CONFIRMED && newStatus != EOrderStatus.CANCELLED) {
                    throw new OrderValidationException(
                        "Cannot transition from CREATED to " + newStatus
                    );
                }
                break;
            case CONFIRMED:
                if (newStatus != EOrderStatus.SHIPPED && newStatus != EOrderStatus.CANCELLED) {
                    throw new OrderValidationException(
                        "Cannot transition from CONFIRMED to " + newStatus
                    );
                }
                break;
            case SHIPPED:
                if (newStatus != EOrderStatus.DELIVERED && newStatus != EOrderStatus.CANCELLED) {
                    throw new OrderValidationException(
                        "Cannot transition from SHIPPED to " + newStatus
                    );
                }
                break;
            case DELIVERED:
                throw new OrderValidationException("Cannot change status of a delivered order");
            case CANCELLED:
                throw new OrderValidationException("Cannot change status of a cancelled order");
            case RETURNED:
                throw new OrderValidationException("Cannot change status of a returned order");
        }
    }
}

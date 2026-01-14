package com.company.ordersystem.return_.service.impl;

import com.company.ordersystem.common.constant.EReturnStatus;
import com.company.ordersystem.common.dto.ReturnRequestDTO;
import com.company.ordersystem.common.dto.ReturnResponseDTO;
import com.company.ordersystem.common.dto.ReturnStatusUpdateRequestDTO;
import com.company.ordersystem.common.entity.OrderEntity;
import com.company.ordersystem.common.entity.OrderItemEntity;
import com.company.ordersystem.common.entity.ReturnEntity;
import com.company.ordersystem.common.entity.UserEntity;
import com.company.ordersystem.common.exception.UserNotFoundException;
import com.company.ordersystem.common.mapper.ReturnMapper;
import com.company.ordersystem.return_.repository.OrderRepository;
import com.company.ordersystem.return_.repository.ProductRepository;
import com.company.ordersystem.return_.repository.ReturnRepository;
import com.company.ordersystem.return_.repository.UserRepository;
import com.company.ordersystem.return_.service.ReturnService;
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
public class ReturnServiceImpl implements ReturnService {

    private final ProductRepository productRepository;
    private final OrderRepository orderRepository;
    private final ReturnRepository returnRepository;
    private final ReturnMapper returnMapper;
    private final UserRepository userRepository;

    @Override
    @Transactional
    public ReturnResponseDTO createReturn(ReturnRequestDTO dto) {

        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        UserEntity user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("User not authorized"));

        OrderEntity order = orderRepository.findById(dto.getOrderId()).orElse(null);
        Assert.notNull(order, "Order id not found:" + dto.getOrderId());

        Assert.isTrue(order.getCustomerId().equals(user.getId()),
                "You cannot return another customer's order");


        boolean exists = productRepository.existsById(dto.getProductId());
        Assert.isTrue(exists , "Product not found: " + dto.getProductId());


        OrderItemEntity orderItem = order.getItems().stream()
                .filter(item->item.getProductId().equals(dto.getProductId())).findFirst().orElse(null);
                Assert.notNull(orderItem, "Product is not part of order:" + dto.getProductId());


        Assert.isTrue(dto.getQuantity() <= orderItem.getQuantity(),
                "Return Quantity cannot be greater than ordered quantity");


        ReturnEntity entity = returnMapper.toEntity(dto);
        entity.setOrderId(order.getId()); //sakat olabilir
        entity.setProductId(dto.getProductId());
        entity.setQuantity(dto.getQuantity());
        entity.setStatus(EReturnStatus.REQUESTED);
        entity.setCreatedAt(LocalDateTime.now());
        ReturnEntity saved = returnRepository.save(entity);
        return returnMapper.toResponseDTO(saved);
    }

    @Override
    public ReturnResponseDTO getReturnById(Long id) {
        ReturnEntity entity = returnRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Return not found with id: " + id));

        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        UserEntity user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        OrderEntity order = orderRepository.findById(entity.getOrderId()).orElse(null);
        Assert.notNull(order, "Order not found for return: " + entity.getOrderId());

        Assert.isTrue(order.getCustomerId().equals(user.getId()),
                "You cannot view another customer's return");
        return returnMapper.toResponseDTO(entity);
    }

    public List<ReturnResponseDTO> getReturnsByOrderId(Long orderId) {
        return returnRepository.findByOrderId(orderId)
                .stream()
                .map(returnMapper::toResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<ReturnResponseDTO> getCustomerReturns() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        UserEntity user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("User not authorized"));

        // Get all orders for the customer
        List<OrderEntity> customerOrders = orderRepository.findAll().stream()
                .filter(order -> order.getCustomerId().equals(user.getId()))
                .collect(Collectors.toList());

        // Get all returns for customer's orders
        List<Long> orderIds = customerOrders.stream()
                .map(OrderEntity::getId)
                .collect(Collectors.toList());

        return returnRepository.findAll().stream()
                .filter(ret -> orderIds.contains(ret.getOrderId()))
                .map(returnMapper::toResponseDTO)
                .collect(Collectors.toList());
    }

    public List<ReturnResponseDTO> getAllReturns() {
        return returnRepository.findAll()
                .stream()
                .map(returnMapper::toResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public ReturnResponseDTO updateReturnStatus(ReturnStatusUpdateRequestDTO requestDTO) {

        ReturnEntity entity = returnRepository.findById(requestDTO.getId())
                .orElse(null);

        Assert.notNull(entity, "Return status not found: " + requestDTO.getId());

        EReturnStatus newStatus = requestDTO.getStatus();
        entity.setStatus(newStatus);
        entity.setUpdatedAt(LocalDateTime.now());

        if (newStatus == EReturnStatus.CONFIRMED) {

            OrderEntity order = orderRepository.findById(entity.getOrderId()).orElse(null);
            Assert.notNull(order, "Order id not found: " + entity.getOrderId());


            OrderItemEntity orderItem = order.getItems().stream()
                    .filter(item -> item.getProductId().equals(entity.getProductId()))
                    .findFirst()
                    .orElse(null);

            Assert.notNull(orderItem,
                    "Product not found in this order: " + entity.getProductId());

            int orderedQuantity = orderItem.getQuantity();
            int returnQuantity = entity.getQuantity();

            Assert.isTrue(returnQuantity <= orderedQuantity,
                    "Return quantity " + returnQuantity +
                            " cannot exceed ordered quantity " + orderedQuantity);

            productRepository.IncrementStock(entity.getProductId(), returnQuantity);
            log.info("Stock incremented for productId={}, quantity={} after returnId={}",
                    entity.getProductId(), returnQuantity, entity.getId());
        }

        ReturnEntity updated = returnRepository.save(entity);
        return returnMapper.toResponseDTO(updated);
    }

    @Override
    @Transactional
    public void deleteReturn(Long id) {
        if (!returnRepository.existsById(id)) {
            throw new RuntimeException("Return not found with id: " + id);
        }
        returnRepository.deleteById(id);
    }
}

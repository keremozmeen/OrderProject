package com.company.ordersystem.user.service.impl;

import com.company.ordersystem.common.dto.UserResponseDTO;
import com.company.ordersystem.common.dto.UserSignupRequestDTO;
import com.company.ordersystem.common.dto.UserUpdateRequestDTO;
import com.company.ordersystem.common.entity.UserEntity;
import com.company.ordersystem.common.entity.RoleEntity;
import com.company.ordersystem.common.constant.ERole;
import com.company.ordersystem.common.mapper.UserMapper;
import com.company.ordersystem.user.repository.RoleRepository;
import com.company.ordersystem.user.repository.UserRepository;
import com.company.ordersystem.user.service.UserService;
import org.springframework.util.Assert;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor // Constructor yazmana gerek kalmaz, hataları çözer
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder encoder;
    private final UserMapper userMapper;
    private final RoleRepository roleRepository;

    @Override
    @Transactional
    public UserResponseDTO register(UserSignupRequestDTO dto) {
        Assert.isTrue(!userRepository.existsByEmail(dto.getEmail()),
                "Email already in use: " + dto.getEmail());

        UserEntity user = userMapper.toEntity(dto);
        user.setPassword(encoder.encode(dto.getPassword()));

        RoleEntity userRole = roleRepository.findByName(ERole.ROLE_USER)
                .orElseThrow(() -> new RuntimeException("Default role USER not found"));

        user.setRoles(Set.of(userRole));
        UserEntity savedUser = userRepository.save(user);

        return userMapper.toResponseDTO(savedUser);
    }

    @Override
    @Transactional(readOnly = true)
    public UserResponseDTO getUserById(Long id) {
        return userRepository.findById(id)
                .map(userMapper::toResponseDTO)
                .orElse(null);
    }

    @Override
    @Transactional(readOnly = true)
    public List<UserResponseDTO> getAllUsers() {
        return userRepository.findAll()
                .stream()
                .map(userMapper::toResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public UserResponseDTO updateUser(UserUpdateRequestDTO dto) {
        UserEntity user = userRepository.findById(dto.getId())
                .orElseThrow(() -> new RuntimeException("User not found with id " + dto.getId()));

        if (dto.getFirstName() != null) user.setFirstName(dto.getFirstName());
        if (dto.getLastName() != null) user.setLastName(dto.getLastName());
        if (dto.getEmail() != null) user.setEmail(dto.getEmail());
        if (dto.getPassword() != null) user.setPassword(encoder.encode(dto.getPassword()));

        UserEntity updated = userRepository.save(user);
        return userMapper.toResponseDTO(updated);
    }

    @Override
    @Transactional
    public void deleteUser(Long id) {
        if (!userRepository.existsById(id)) {
            throw new RuntimeException("User not found with id " + id);
        }
        userRepository.deleteById(id);
    }

    @Override
    @Transactional
    public void assignRole(Long userId, ERole role) {
        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with id " + userId));

        RoleEntity roleEntity = roleRepository.findByName(role)
                .orElseThrow(() -> new RuntimeException("Role not found: " + role.name()));

        user.getRoles().add(roleEntity);
        userRepository.save(user);
    }
}
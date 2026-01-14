package com.company.ordersystem.user.service;

import com.company.ordersystem.common.constant.ERole;
import com.company.ordersystem.common.dto.UserResponseDTO;
import com.company.ordersystem.common.dto.UserSignupRequestDTO;
import com.company.ordersystem.common.dto.UserUpdateRequestDTO;

import java.util.List;

public interface UserService {
    UserResponseDTO register(UserSignupRequestDTO dto);
    UserResponseDTO getUserById(Long id);
    List<UserResponseDTO> getAllUsers();
    UserResponseDTO updateUser(UserUpdateRequestDTO dto);
    void deleteUser(Long id);
    void assignRole(Long userId, ERole role);
}

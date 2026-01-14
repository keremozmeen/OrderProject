package com.company.ordersystem.user.controller;

import com.company.ordersystem.common.dto.AssignRoleRequestDTO;
import com.company.ordersystem.common.dto.ResultDTO;
import com.company.ordersystem.common.dto.UserResponseDTO;
import com.company.ordersystem.common.dto.UserUpdateRequestDTO;
import com.company.ordersystem.user.service.UserService;
import org.springframework.util.Assert;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/admin/users")
@RequiredArgsConstructor
@Tag(name="user-controller", description = "Admin level user management APIs")
public class UserController {

    private final UserService userService;

    @Operation(summary = "Getting users by Id")
    @GetMapping("/{id}")
    public ResponseEntity<ResultDTO<UserResponseDTO>> getUserById(@Valid @PathVariable Long id) {
        UserResponseDTO user = userService.getUserById(id);
        Assert.notNull(user, "User Not Found");
        return ResponseEntity.ok(new ResultDTO<>("00", "Success", user));
    }

    @Operation(summary = "Get All Users")
    @GetMapping
    public ResponseEntity<ResultDTO<List<UserResponseDTO>>> getAllUsers() {
        List<UserResponseDTO> users = userService.getAllUsers();
        Assert.notNull(users, "Users Not Found");
        return ResponseEntity.ok(new ResultDTO<>("00", "Success", users));
    }

    @Operation(summary = "Update User Information")
    @PutMapping
    public ResponseEntity<ResultDTO<UserResponseDTO>> updateUser(@Valid @RequestBody UserUpdateRequestDTO request) {
        UserResponseDTO updated = userService.updateUser(request);
        Assert.notNull(updated, "User Not Found");
        return ResponseEntity.ok(new ResultDTO<>("00", "Success", updated));
    }

    @Operation(summary = "Delete Selected User")
    @DeleteMapping("/{id}")
    public ResponseEntity<ResultDTO<Void>> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.ok(new ResultDTO<>("00", "Success", null));
    }

    @Operation(summary = "Assign Role To Users")
    @PostMapping("/assign-role")
    public ResponseEntity<ResultDTO<Void>> assignRole(@Valid @RequestBody AssignRoleRequestDTO request) {
        userService.assignRole(request.getUserId(), request.getRole());
        return ResponseEntity.ok(new ResultDTO<>("00", "Success", null));
    }
}
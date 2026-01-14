package com.company.ordersystem.user.controller;

import com.company.ordersystem.common.dto.LoginRequestDTO;
import com.company.ordersystem.common.dto.ResultDTO;
import com.company.ordersystem.common.dto.UserResponseDTO;
import com.company.ordersystem.common.dto.UserSignupRequestDTO;
import com.company.ordersystem.user.service.JwtService; // Paket yolu düzeltildi
import com.company.ordersystem.user.service.UserService; // Paket yolu düzeltildi
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/v1/noauth")
@RequiredArgsConstructor
@Tag(name = "auth-controller", description = "Authentication and Registration APIs")
public class AuthController {

    private final UserService userService;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    @Operation(summary = "Register Method")
    @PostMapping("/register")
    public ResponseEntity<ResultDTO<UserResponseDTO>> register(@Valid @RequestBody UserSignupRequestDTO request) {
        log.info("Register attempt for email={}", request.getEmail());
        UserResponseDTO user = userService.register(request);
        log.info("Register successful for email={}", request.getEmail());
        return ResponseEntity.ok(new ResultDTO<>("00", "Success", user));
    }

    @Operation(summary = "Login Method")
    @PostMapping("/login")
    public ResponseEntity<ResultDTO<String>> login(@Valid @RequestBody LoginRequestDTO request) {
        log.info("Login attempt for email={}", request.getEmail());
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
        );
        String token = jwtService.generateJwtToken(authentication);
        log.info("Login successful, token generated for email={}", request.getEmail());
        return ResponseEntity.ok(new ResultDTO<>("00", "Success", token));
    }
}
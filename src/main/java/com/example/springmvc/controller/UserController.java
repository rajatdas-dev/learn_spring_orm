package com.example.springmvc.controller;

import com.example.springmvc.dto.request.user.AddressRequestDTO;
import com.example.springmvc.dto.request.user.UserProfileRequestDTO;
import com.example.springmvc.dto.request.user.UserRequestDTO;
import com.example.springmvc.dto.response.user.AddressResponseDTO;
import com.example.springmvc.dto.response.user.UserProfileResponseDTO;
import com.example.springmvc.dto.response.user.UserResponseDTO;
import com.example.springmvc.response.ApiResponse;
import com.example.springmvc.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping
    public ResponseEntity<ApiResponse<UserResponseDTO>> createUser(@Valid @RequestBody UserRequestDTO userRequestDTO) {
        UserResponseDTO userResponseDTO = userService.createUser(userRequestDTO);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("User created successfully", userResponseDTO));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<UserResponseDTO>> getUserById(@PathVariable Long id) {
        UserResponseDTO userResponseDTO = userService.getUserById(id);
        return ResponseEntity.ok(ApiResponse.success("User fetched successfully", userResponseDTO));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<UserResponseDTO>>> getAllUsers() {
        List<UserResponseDTO> users = userService.getAllUsers();
        return ResponseEntity.ok(ApiResponse.success("All users fetched successfully", users));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<UserResponseDTO>> updateUser(
            @PathVariable Long id,
            @RequestBody UserRequestDTO requestDTO) {
        UserResponseDTO updated = userService.updateUser(id, requestDTO);
        return ResponseEntity.ok(ApiResponse.success("User updated successfully", updated));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.ok(ApiResponse.success("User deleted successfully"));
    }

    @PostMapping("/{id}/addresses")
    public ResponseEntity<ApiResponse<AddressResponseDTO>> addAddress(
            @PathVariable Long id,
            @Valid @RequestBody AddressRequestDTO addressRequestDTO) {
        AddressResponseDTO responseDTO = userService.addAddress(id, addressRequestDTO);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Address added successfully", responseDTO));
    }

    @GetMapping("/{id}/addresses")
    public ResponseEntity<ApiResponse<List<AddressResponseDTO>>> getUserAddresses(
            @PathVariable Long id) {
        List<AddressResponseDTO> addresses = userService.getUserAddresses(id);
        return ResponseEntity.ok(ApiResponse.success("User addresses fetched successfully", addresses));
    }

    @PutMapping("/{id}/profile")
    public ResponseEntity<ApiResponse<UserProfileResponseDTO>> updateProfile(
            @PathVariable Long id,
            @RequestBody UserProfileRequestDTO profileRequestDTO) {
        UserProfileResponseDTO responseDTO = userService.updateProfile(id, profileRequestDTO);
        return ResponseEntity.ok(ApiResponse.success("User profile updated successfully", responseDTO));
    }

    @GetMapping("/{id}/profile")
    public ResponseEntity<ApiResponse<UserProfileResponseDTO>> getProfile(
            @PathVariable Long id) {
        UserProfileResponseDTO responseDTO = userService.getProfile(id);
        return ResponseEntity.ok(ApiResponse.success("User profile fetched successfully", responseDTO));
    }
}

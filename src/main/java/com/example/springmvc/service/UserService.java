package com.example.springmvc.service;

import com.example.springmvc.dto.request.user.AddressRequestDTO;
import com.example.springmvc.dto.request.user.UserProfileRequestDTO;
import com.example.springmvc.dto.request.user.UserRequestDTO;
import com.example.springmvc.dto.response.user.AddressResponseDTO;
import com.example.springmvc.dto.response.user.UserProfileResponseDTO;
import com.example.springmvc.dto.response.user.UserResponseDTO;

import java.util.List;

public interface UserService {

    UserResponseDTO createUser(UserRequestDTO requestDTO);

    UserResponseDTO getUserById(Long id);

    List<UserResponseDTO> getAllUsers();

    UserResponseDTO updateUser(Long id, UserRequestDTO requestDTO);

    void deleteUser(Long id);

    AddressResponseDTO addAddress(Long userId, AddressRequestDTO addressRequestDTO);

    List<AddressResponseDTO> getUserAddresses(Long userId);

    UserProfileResponseDTO updateProfile(Long userId, UserProfileRequestDTO profileRequestDTO);

    UserProfileResponseDTO getProfile(Long userId);
}

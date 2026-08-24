package com.example.springmvc.service;

import com.example.springmvc.dto.request.user.UserRequestDTO;
import com.example.springmvc.dto.response.UserResponseDTO;

public interface UserService {

    UserResponseDTO createUser(UserRequestDTO requestDTO);
}

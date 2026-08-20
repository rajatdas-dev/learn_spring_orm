package com.example.springmvc.service;

import com.example.springmvc.dto.request.UserRequestDTO;
import com.example.springmvc.dto.response.UserResponseDTO;
import com.example.springmvc.entity.User;

public interface UserService {

    UserResponseDTO createUser(UserRequestDTO requestDTO);
}

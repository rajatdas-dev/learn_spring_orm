package com.example.springmvc.controller;

import com.example.springmvc.dto.request.user.UserRequestDTO;
import com.example.springmvc.dto.response.UserResponseDTO;
import com.example.springmvc.service.impl.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/user")
public class UserController {

    @Autowired
    private UserServiceImpl userService;

    @PostMapping("/create")
    public ResponseEntity<UserResponseDTO> createUser(@RequestBody UserRequestDTO userRequestDTO){
      return  ResponseEntity.ok(userService.createUser(userRequestDTO));
    }


}

package com.example.springmvc.controller;

import com.example.springmvc.entity.User;
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
    public ResponseEntity<User> createUser(@RequestBody User user){
      User savedUser =  userService.createUser(user);

      return  ResponseEntity.ok(savedUser);
    }


}

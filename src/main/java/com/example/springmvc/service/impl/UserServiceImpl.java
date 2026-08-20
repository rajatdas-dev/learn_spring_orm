package com.example.springmvc.service.impl;

import com.example.springmvc.entity.User;
import com.example.springmvc.repository.UserRepository;
import com.example.springmvc.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public User createUser(User user) {
       return userRepository.save(user);
    }
}

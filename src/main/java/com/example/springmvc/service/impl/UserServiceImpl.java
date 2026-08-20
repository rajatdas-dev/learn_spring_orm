package com.example.springmvc.service.impl;

import com.example.springmvc.dto.request.UserRequestDTO;
import com.example.springmvc.dto.response.UserResponseDTO;
import com.example.springmvc.entity.User;
import com.example.springmvc.repository.UserRepository;
import com.example.springmvc.service.UserService;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

  @Autowired
  private  UserRepository userRepository;

    @Override
    public UserResponseDTO createUser(UserRequestDTO userRequestDTO) {
//       return  userRepository.save();
    }
}

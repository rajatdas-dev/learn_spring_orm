package com.example.springmvc.service.impl;

import com.example.springmvc.dto.request.user.UserRequestDTO;
import com.example.springmvc.dto.response.UserResponseDTO;
import com.example.springmvc.entity.User;
import com.example.springmvc.repository.UserRepository;
import com.example.springmvc.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

  @Autowired
  private  UserRepository userRepository;

    @Override
    public UserResponseDTO createUser(UserRequestDTO userRequestDTO) {

            User user = toEntity(userRequestDTO);
            userRepository.save(user);

            UserResponseDTO userResponseDTO = toResponseDTO(user);
            return  userResponseDTO;

    }


    User toEntity(UserRequestDTO userRequestDTO){
        User user = new User();
        user.setName(userRequestDTO.getName());
        user.setAge(userRequestDTO.getAge());

        return  user;
    }

    UserResponseDTO toResponseDTO(User user){
        UserResponseDTO userResponseDTO = new UserResponseDTO();

        userResponseDTO.setAge(user.getAge());
        userResponseDTO.setName(user.getName());
        userResponseDTO.setEmail(user.getEmail());

        return  userResponseDTO;
    }
}

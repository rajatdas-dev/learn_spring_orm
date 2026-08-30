package com.example.springmvc.service.impl;

import com.example.springmvc.dto.request.user.AddressRequestDTO;
import com.example.springmvc.dto.request.user.UserProfileRequestDTO;
import com.example.springmvc.dto.request.user.UserRequestDTO;
import com.example.springmvc.dto.response.user.AddressResponseDTO;
import com.example.springmvc.dto.response.user.UserProfileResponseDTO;
import com.example.springmvc.dto.response.user.UserResponseDTO;
import com.example.springmvc.entity.Address;
import com.example.springmvc.entity.User;
import com.example.springmvc.entity.UserProfile;
import com.example.springmvc.exception.ErrorCode;
import com.example.springmvc.exception.ResourceNotFoundException;
import com.example.springmvc.repository.AddressRepository;
import com.example.springmvc.repository.UserProfileRepository;
import com.example.springmvc.repository.UserRepository;
import com.example.springmvc.service.UserService;
import com.example.springmvc.util.mapper.ModelMapperUtil;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AddressRepository addressRepository;

    @Autowired
    private UserProfileRepository userProfileRepository;

    @Autowired
    private ModelMapperUtil modelMapperUtil;

    @Override
    @Transactional
    public UserResponseDTO createUser(UserRequestDTO userRequestDTO) {
        User user = new User();
        user.setName(userRequestDTO.getName());
        user.setEmail(userRequestDTO.getEmail());
        user.setAge(userRequestDTO.getAge());

        User savedUser = userRepository.save(user);
        return modelMapperUtil.map(savedUser, UserResponseDTO.class);
    }

    @Override
    public UserResponseDTO getUserById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        ErrorCode.USER_NOT_FOUND,
                        "User not found with id: " + id
                ));
        return modelMapperUtil.map(user, UserResponseDTO.class);
    }

    @Override
    public List<UserResponseDTO> getAllUsers() {
        return userRepository.findAll().stream()
                .map(user -> modelMapperUtil.map(user, UserResponseDTO.class))
                .toList();
    }

    @Override
    @Transactional
    public UserResponseDTO updateUser(Long id, UserRequestDTO requestDTO) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        ErrorCode.USER_NOT_FOUND,
                        "User not found with id: " + id
                ));

        if (requestDTO.getName() != null) {
            user.setName(requestDTO.getName());
        }
        if (requestDTO.getEmail() != null) {
            user.setEmail(requestDTO.getEmail());
        }
        if (requestDTO.getAge() != null) {
            user.setAge(requestDTO.getAge());
        }

        return modelMapperUtil.map(user, UserResponseDTO.class);
    }

    @Override
    @Transactional
    public void deleteUser(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        ErrorCode.USER_NOT_FOUND,
                        "User not found with id: " + id
                ));
        userRepository.delete(user);
    }

    @Override
    @Transactional
    public AddressResponseDTO addAddress(
            Long userId,
            AddressRequestDTO addressRequestDTO
    ) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        ErrorCode.USER_NOT_FOUND,
                        "User not found with id: " + userId
                ));

        Address address = modelMapperUtil.map(
                addressRequestDTO,
                Address.class
        );
        user.addAddress(address);
        Address savedAddress = addressRepository.save(address);

        return modelMapperUtil.map(savedAddress, AddressResponseDTO.class);
    }

    @Override
    public List<AddressResponseDTO> getUserAddresses(Long userId) {
        if (!userRepository.existsById(userId)) {
            throw new ResourceNotFoundException(
                    ErrorCode.USER_NOT_FOUND,
                    "User not found with id: " + userId
            );
        }
        return addressRepository.findByUserId(userId).stream()
                .map(address -> modelMapperUtil.map(address, AddressResponseDTO.class))
                .toList();
    }

    @Override
    @Transactional
    public UserProfileResponseDTO updateProfile(
            Long userId,
            UserProfileRequestDTO profileRequestDTO
    ) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        ErrorCode.USER_NOT_FOUND,
                        "User not found with id: " + userId
                ));

        UserProfile profile = user.getUserProfile();
        if (profile == null) {
            profile = new UserProfile();
            user.setUserProfile(profile);
        }

        if (profileRequestDTO.getPhoneNumber() != null) {
            profile.setPhoneNumber(profileRequestDTO.getPhoneNumber());
        }
        if (profileRequestDTO.getAvatarUrl() != null) {
            profile.setAvatarUrl(profileRequestDTO.getAvatarUrl());
        }
        if (profileRequestDTO.getDateOfBirth() != null) {
            profile.setDateOfBirth(profileRequestDTO.getDateOfBirth());
        }
        if (profileRequestDTO.getBio() != null) {
            profile.setBio(profileRequestDTO.getBio());
        }

        UserProfile savedProfile = userProfileRepository.save(profile);
        return modelMapperUtil.map(savedProfile, UserProfileResponseDTO.class);
    }

    @Override
    public UserProfileResponseDTO getProfile(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        ErrorCode.USER_NOT_FOUND,
                        "User not found with id: " + userId
                ));

        UserProfile profile = user.getUserProfile();
        if (profile == null) {
            throw new ResourceNotFoundException(
                    ErrorCode.USER_NOT_FOUND,
                    "Profile not found for user: " + userId
            );
        }

        return modelMapperUtil.map(profile, UserProfileResponseDTO.class);
    }
}

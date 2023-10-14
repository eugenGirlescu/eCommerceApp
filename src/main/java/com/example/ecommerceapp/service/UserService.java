package com.example.ecommerceapp.service;

import com.example.ecommerceapp.dto.UserDTO;
import com.example.ecommerceapp.exception.UserAlreadyExistsException;
import com.example.ecommerceapp.mapper.UserMapper;
import com.example.ecommerceapp.model.User;
import com.example.ecommerceapp.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public User registerUser(UserDTO userDTO) {
        if(userRepository.findByUsernameIgnoreCase(userDTO.getUserName()).isPresent()
                || userRepository.findByEmailIgnoreCase(userDTO.getEmail()).isPresent()) {
            throw  new UserAlreadyExistsException("User with this email or username already exists!");
        }
        User user = UserMapper.INSTANCE.userDtoToUser(userDTO);
        return userRepository.save(user);
    }
}

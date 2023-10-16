package com.example.ecommerceapp.service;

import com.example.ecommerceapp.dto.UserGetDTO;
import com.example.ecommerceapp.dto.UserPostDTO;
import com.example.ecommerceapp.exception.UserAlreadyExistsException;
import com.example.ecommerceapp.model.User;
import com.example.ecommerceapp.repository.UserRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final ModelMapper modelMapper;

    public UserGetDTO registerUser(UserPostDTO userPostDTO) {
        if(userRepository.findByUserNameIgnoreCase(userPostDTO.getUserName()).isPresent()
                || userRepository.findByEmailIgnoreCase(userPostDTO.getEmail()).isPresent()) {
            throw  new UserAlreadyExistsException("User with this email or username already exists!");
        }
        User user = convertFromDto(userPostDTO);
        User savedUser = userRepository.save(user);

        return convertToUserGetDTO(savedUser);
    }

    private User convertFromDto(UserPostDTO userPostDTO) {
        return modelMapper.map(userPostDTO, User.class);
    }

    private UserGetDTO convertToUserGetDTO(User user) {
        return modelMapper.map(user, UserGetDTO.class);
    }
}

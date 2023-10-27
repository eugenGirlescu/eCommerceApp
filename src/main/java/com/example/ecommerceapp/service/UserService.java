package com.example.ecommerceapp.service;

import com.example.ecommerceapp.dto.UserGetDTO;
import com.example.ecommerceapp.dto.UserLoginDTO;
import com.example.ecommerceapp.dto.UserPostDTO;
import com.example.ecommerceapp.exception.UserAlreadyExistsException;
import com.example.ecommerceapp.model.User;
import com.example.ecommerceapp.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final ModelMapper modelMapper;
    private final EncryptionService encryptionService;
    private final JWTService jwtService;

    public UserGetDTO registerUser(UserPostDTO userPostDTO) {
        if (userRepository.findByUserNameIgnoreCase(userPostDTO.getUserName()).isPresent()
                || userRepository.findByEmailIgnoreCase(userPostDTO.getEmail()).isPresent()) {
            throw new UserAlreadyExistsException("User with this email or username already exists!");
        }
        User user = convertFromDto(userPostDTO);
        user.setPassword(encryptionService.encryptPassword(userPostDTO.getPassword()));
        User savedUser = userRepository.save(user);

        return convertToUserGetDTO(savedUser);
    }

    public String loginUser(UserLoginDTO userLoginDTO) {
        Optional<User> userFromDb = userRepository.findByUserNameIgnoreCase(userLoginDTO.getUserName());
        if(userFromDb.isPresent()) {
           User user = userFromDb.get();
           if(encryptionService.checkPassword(userLoginDTO.getPassword(), user.getPassword())) {
               return jwtService.generateJWT(user);
           }
        }
        return null;
    }

    public User convertFromDto(UserPostDTO userPostDTO) {
        return modelMapper.map(userPostDTO, User.class);
    }

    public UserGetDTO convertToUserGetDTO(User user) {
        return modelMapper.map(user, UserGetDTO.class);
    }

    public User convertToUser(UserGetDTO userGetDTO) {
        return modelMapper.map(userGetDTO, User.class);
    }
}

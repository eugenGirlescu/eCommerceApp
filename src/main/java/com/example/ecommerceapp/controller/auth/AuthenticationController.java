package com.example.ecommerceapp.controller.auth;

import com.example.ecommerceapp.dto.UserGetDTO;
import com.example.ecommerceapp.dto.UserLoginDTO;
import com.example.ecommerceapp.dto.UserLoginResponseDTO;
import com.example.ecommerceapp.dto.UserPostDTO;
import com.example.ecommerceapp.model.User;
import com.example.ecommerceapp.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthenticationController {

    private final UserService userService;

    public AuthenticationController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    public ResponseEntity<UserGetDTO> registerUser( @Valid @RequestBody UserPostDTO userPostDTO) {
        return new ResponseEntity<>(userService.registerUser(userPostDTO), HttpStatus.OK);
    }

    @PostMapping("/login")
    public ResponseEntity<UserLoginResponseDTO> loginUser(@RequestBody UserLoginDTO userLoginDTO) {
        String jwt = userService.loginUser(userLoginDTO);
        if(jwt == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(new UserLoginResponseDTO(jwt),HttpStatus.OK);
    }

    @GetMapping("/me")
    public UserGetDTO getLoggedInUserProfile(@AuthenticationPrincipal UserGetDTO user) {
        return user;
    }
}

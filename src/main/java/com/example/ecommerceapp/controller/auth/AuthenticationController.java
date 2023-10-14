package com.example.ecommerceapp.controller.auth;

import com.example.ecommerceapp.dto.UserDTO;
import com.example.ecommerceapp.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthenticationController {

    private final UserService userService;

    @PostMapping("/register")
    public void registerUser(@RequestBody UserDTO userDTO) {
        userService.registerUser(userDTO);
    }
}

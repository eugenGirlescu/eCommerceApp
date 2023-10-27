package com.example.ecommerceapp.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NonNull;

@Data
public class UserLoginDTO {

    @NonNull
    @NotBlank(message = "Username is mandatory")
    private String userName;

    @NonNull
    @NotBlank(message = "Password is mandatory")
    @Size(min = 8, message = "Password must be at least 8 characters long")
    private String password;
}

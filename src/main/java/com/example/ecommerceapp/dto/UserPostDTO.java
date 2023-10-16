package com.example.ecommerceapp.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserPostDTO {
    @NonNull
    @NotBlank(message = "Username is mandatory")
    private String userName;

    @Email(message = "Please try again with a valid email address!")
    @NonNull
    @NotBlank(message = "Email is mandatory")
    @Pattern(regexp = "^[a-zA-Z0-9_!#$%&'*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$")
    private String email;

    @NonNull
    @NotBlank(message = "Password is mandatory")
    @Size(min = 8, message = "Password must be at least 8 characters long")
    private String password;

    @NonNull
    @NotBlank(message = "First name is mandatory")
    private String firstName;

    @NonNull
    @NotBlank(message = "Last name is mandatory")
    private String lastName;
}

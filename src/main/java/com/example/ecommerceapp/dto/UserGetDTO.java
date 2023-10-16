package com.example.ecommerceapp.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserGetDTO {
    private String userName;
    private String email;
    private String firstName;
    private String lastName;
}

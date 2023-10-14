package com.example.ecommerceapp.mapper;

import com.example.ecommerceapp.dto.UserDTO;
import com.example.ecommerceapp.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface UserMapper {
    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

    @Mapping(target = "id", ignore = true)
    User userDtoToUser(UserDTO userDTO);
}

package com.qjewels.qjewels.mapper;

import com.qjewels.qjewels.dto.UserDTO;
import com.qjewels.qjewels.dto.UserRegistrationDTO;
import com.qjewels.qjewels.model.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {
    User toEntity(UserRegistrationDTO dto);
    UserDTO toDto(User user);
}
package org.example.springwebcapstoneproject.mapper;

import org.example.springwebcapstoneproject.dto.register.RegisterUserDto;
import org.example.springwebcapstoneproject.entity.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {

    User toEntity(RegisterUserDto dto);
}

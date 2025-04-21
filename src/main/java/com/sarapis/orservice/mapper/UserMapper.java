package com.sarapis.orservice.mapper;

import com.sarapis.orservice.dto.auth.RegisterDTO;
import com.sarapis.orservice.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;

@Mapper(componentModel = "spring")
public abstract class UserMapper {
  @Autowired
  protected PasswordEncoder passwordEncoder;

  @Mapping(target = "password", expression = "java(passwordEncoder.encode(request.getPassword()))")
  public abstract User toEntity(RegisterDTO.Request request);
  public abstract RegisterDTO.Response toDto(User user);
}

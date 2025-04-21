package com.sarapis.orservice.service.auth;

import com.sarapis.orservice.dto.auth.*;
import com.sarapis.orservice.mapper.UserMapper;
import com.sarapis.orservice.model.User;
import com.sarapis.orservice.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final UserRepository userRepository;
    private final AuthenticationManager authenticationManager;
    private final UserMapper userMapper;



    public RegisterDTO.Response signup(RegisterDTO.Request input) {
        User user = userMapper.toEntity(input);
        User savedUser = userRepository.save(user);
        return userMapper.toDto(user);
    }

    public User authenticate(LoginDTO.Request input) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        input.getEmail(),
                        input.getPassword()
                )
        );

        return userRepository.findByEmail(input.getEmail())
                .orElseThrow();
    }
}

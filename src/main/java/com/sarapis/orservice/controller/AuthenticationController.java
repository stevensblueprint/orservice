package com.sarapis.orservice.controller;

import com.sarapis.orservice.dto.auth.LoginResponseDTO;
import com.sarapis.orservice.dto.auth.LoginUserDTO;
import com.sarapis.orservice.dto.auth.RegisterUserDTO;
import com.sarapis.orservice.model.User;
import com.sarapis.orservice.repository.UserRepository;
import com.sarapis.orservice.service.auth.AuthenticationService;
import com.sarapis.orservice.service.auth.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;
import java.util.Optional;

@RequestMapping("/auth")
@RestController
@RequiredArgsConstructor
public class AuthenticationController {

    private final JwtService jwtService;
    private final AuthenticationService authenticationService;
    private final UserRepository userRepository;

    @PostMapping("/signup")
    public ResponseEntity<User> register(@RequestBody RegisterUserDTO registerUserDto) {
        User registeredUser = authenticationService.signup(registerUserDto);

        return ResponseEntity.ok(registeredUser);
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> authenticate(@RequestBody LoginUserDTO loginUserDto) {
        User authenticatedUser = authenticationService.authenticate(loginUserDto);

        String jwtToken = jwtService.generateToken(authenticatedUser);

        LoginResponseDTO loginResponse = new LoginResponseDTO();
        loginResponse.setToken(jwtToken);
        loginResponse.setExpiresIn(jwtService.getExpirationTime());

        return ResponseEntity.ok(loginResponse);
    }

    @GetMapping("/test")
    public ResponseEntity<User> test(@RequestParam String email) {
        return ResponseEntity.ok(userRepository.findByEmail(email).orElse(null));
    }

    @DeleteMapping
    public ResponseEntity<Void> delete(@RequestParam String email) {
        Optional<User> userOptional = userRepository.findByEmail(email);
        if (userOptional.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        userRepository.delete(userOptional.get());
        return ResponseEntity.noContent().build();
    }
}

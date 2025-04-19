package com.sarapis.orservice.controller;

import com.sarapis.orservice.dto.auth.*;
import com.sarapis.orservice.model.User;
import com.sarapis.orservice.service.auth.AuthenticationService;
import com.sarapis.orservice.service.auth.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/auth")
@RestController
@RequiredArgsConstructor
public class AuthenticationController {

    private final JwtService jwtService;
    private final AuthenticationService authenticationService;

    @PostMapping("/signup")
    public ResponseEntity<RegisterDTO.Response> register(
            @RequestBody RegisterDTO.Request registerUserDto
    ) {
         return ResponseEntity.ok(authenticationService.signup(registerUserDto));
    }

    @PostMapping("/login")
    public ResponseEntity<LoginDTO.Response> authenticate(@RequestBody LoginDTO.Request loginUserDto) {
        User authenticatedUser = authenticationService.authenticate(loginUserDto);

        return ResponseEntity.ok(jwtService.createJwtResponse(authenticatedUser));
    }
}

package com.sarapis.orservice.service.auth;

import com.sarapis.orservice.model.User;
import com.sarapis.orservice.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;


    public List<User> allUsers() {
        return new ArrayList<>(userRepository.findAll());
    }
}

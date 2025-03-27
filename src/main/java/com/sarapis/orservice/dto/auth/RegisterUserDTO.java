package com.sarapis.orservice.dto.auth;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RegisterUserDTO {

    private String email;
    private String password;
    private String firstName;
    private String lastName;
}

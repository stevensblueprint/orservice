package com.sarapis.orservice.dto.auth;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LoginUserDTO {

    private String email;
    private String password;
}

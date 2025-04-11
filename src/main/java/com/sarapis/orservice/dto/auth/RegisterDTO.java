package com.sarapis.orservice.dto.auth;

import lombok.*;

public class RegisterDTO {
    @Getter
    @Setter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Request {
        private String email;
        private String firstName;
        private String lastName;
        private String password;
    }

    @Getter
    @Setter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Response {
        private String email;
        private String firstName;
        private String lastName;
    }
}

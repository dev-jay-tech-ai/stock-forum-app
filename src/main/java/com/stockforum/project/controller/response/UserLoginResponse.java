package com.stockforum.project.controller.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UserLoginResponse {
    // if login works well, return token
    private String token;
}

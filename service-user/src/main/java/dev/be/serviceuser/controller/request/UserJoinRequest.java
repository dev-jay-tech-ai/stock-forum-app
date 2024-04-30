package dev.be.serviceuser.controller.request;

import lombok.Getter;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class UserJoinRequest {
    private String userName;
    private String firstName;
    private String lastName;
    private String email;
    private String password;
}
package com.stockforum.project.controller.response;

import com.stockforum.project.model.User;
import com.stockforum.project.model.UserRole;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UserJoinResponse {
    private Integer id;
    private String userName;
    public static UserJoinResponse fromUser(User user) {
        return new UserJoinResponse(
                user.getId(),
                user.getUserName()
        );
    }

}

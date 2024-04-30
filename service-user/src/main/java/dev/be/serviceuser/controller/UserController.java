package dev.be.serviceuser.controller;


import dev.be.serviceuser.controller.request.ProfileRequest;
import dev.be.serviceuser.controller.request.UpdatePasswordRequest;
import dev.be.serviceuser.controller.request.UserJoinRequest;
import dev.be.serviceuser.controller.request.UserLoginRequest;
import dev.be.serviceuser.controller.response.ProfileResponse;
import dev.be.serviceuser.controller.response.Response;
import dev.be.serviceuser.controller.response.UserJoinResponse;
import dev.be.serviceuser.controller.response.UserLoginResponse;
import dev.be.serviceuser.model.User;
import dev.be.serviceuser.service.UserService;
import dev.be.serviceuser.utils.ClassUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping("/join")
    public Response<UserJoinResponse> join(@RequestBody UserJoinRequest request) {
        return Response.success(UserJoinResponse.fromUser(userService.join(request.getUserName(), request.getFirstName(), request.getLastName(), request.getEmail(), request.getPassword())));
    }

    @PostMapping("/login")
    public Response<UserLoginResponse> login(@RequestBody UserLoginRequest request) {
        String token = userService.login(request.getUserName(), request.getPassword());
        return Response.success(new UserLoginResponse(token));
    }

    @GetMapping("/me")
    public Response<UserJoinResponse> me(Authentication authentication) {
        return Response.success(UserJoinResponse.fromUser(userService.loadUserByUsername(authentication.getName())));
    }

    @PostMapping("/profile")
    public Response<ProfileResponse> createProfile(@RequestBody ProfileRequest request, Authentication authentication) {
        User user = ClassUtils.getSafeCastInstance(authentication.getPrincipal(), User.class);
        return Response.success(ProfileResponse.fromProfile(userService.create(user.getId(), request.getProfileImgUrl(), request.getDescription())));
    }

    @PutMapping("/profile/{userId}")
    public Response<ProfileResponse> modifyProfile(@PathVariable Integer userId, @RequestBody ProfileRequest request) {
        return Response.success(ProfileResponse.fromProfile(userService.modify(userId, request.getProfileImgUrl(), request.getDescription())));
    }

    @PostMapping("/logout")
    public void logout(Authentication authentication) {
        User user = ClassUtils.getSafeCastInstance(authentication.getPrincipal(), User.class);
        userService.logout(user);
    }

    @PutMapping("/{userId}/password")
    public Response<String> updatePassword(@PathVariable("userId") Integer userId, @RequestBody UpdatePasswordRequest request, Authentication authentication) {
        User user = ClassUtils.getSafeCastInstance(authentication.getPrincipal(), User.class);
        userService.updatePassword(user, userId, request.getNewPassword());
        return Response.success("Password updated successfully");
    }

}
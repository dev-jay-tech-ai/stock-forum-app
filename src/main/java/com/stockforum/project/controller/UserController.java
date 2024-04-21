package com.stockforum.project.controller;


import com.stockforum.project.controller.request.UserJoinRequest;
import com.stockforum.project.controller.request.UserLoginRequest;
import com.stockforum.project.controller.response.Response;
import com.stockforum.project.controller.response.UserJoinResponse;
import com.stockforum.project.controller.response.UserLoginResponse;
import com.stockforum.project.service.UserService;
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
    // private final AlarmService alarmService;

    @PostMapping("/join")
    public Response<UserJoinResponse> join(@RequestBody UserJoinRequest request) {
        return Response.success(UserJoinResponse.fromUser(userService.join(request.getName(), request.getPassword())));
    }

    @PostMapping("/login")
    public Response<UserLoginResponse> login(@RequestBody UserLoginRequest request) {
        String token = userService.login(request.getName(), request.getPassword());
        return Response.success(new UserLoginResponse(token));
    }

    @GetMapping("/me")
    public Response<UserJoinResponse> me(Authentication authentication) {
        return Response.success(UserJoinResponse.fromUser(userService.loadUserByUsername(authentication.getName())));
    }

//    @GetMapping("/alarm")
//    public Response<Page<AlarmResponse>> alarm(Pageable pageable, Authentication authentication) {
//        User user = ClassUtils.getSafeCastInstance(authentication.getPrincipal(), User.class);
//        return Response.success(userService.alarmList(user.getId(), pageable).map(AlarmResponse::fromAlarm));
//    }

//    @GetMapping(value = "/alarm/subscribe")
//    public SseEmitter subscribe(Authentication authentication) {
//        log.info("subscribe");
//        User user = ClassUtils.getSafeCastInstance(authentication.getPrincipal(), User.class);
//        return alarmService.connectNotification(user.getId());
//    }
}

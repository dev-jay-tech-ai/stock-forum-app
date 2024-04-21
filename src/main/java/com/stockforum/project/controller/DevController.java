//package com.stockforum.project.controller;
//
//import com.stockforum.project.model.AlarmArgs;
//import com.stockforum.project.model.AlarmEvent;
//import com.stockforum.project.model.AlarmType;
//import com.stockforum.project.model.entity.UserEntity;
//import com.stockforum.project.producer.AlarmProducer;
//import com.stockforum.project.repository.UserEntityRepository;
//import com.stockforum.project.service.AlarmService;
//import lombok.RequiredArgsConstructor;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RestController;
//
//@RestController
//@RequestMapping("/api-dev/v1")
//@RequiredArgsConstructor
//public class DevController {
//
//    private final AlarmService notificationService;
//    private final UserEntityRepository userEntityRepository;
//    private final AlarmProducer alarmProducer;
//
//    @GetMapping("/notification")
//    public void test() {
//        UserEntity entity = userEntityRepository.findById(5).orElseThrow();
//        notificationService.send(AlarmType.NEW_LIKE_ON_POST, new AlarmArgs(0, 0), entity.getId());
//    }
//
//    @GetMapping("/send")
//    public void send() {
//        alarmProducer.send(new AlarmEvent(AlarmType.NEW_LIKE_ON_POST, new AlarmArgs(0, 0), 5));
//    }
//
//}

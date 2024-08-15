package com.hufs.AnswerDev.Model.User;

import com.hufs.AnswerDev.Model.User.Dto.fromClient.LoginClientDto;
import com.hufs.AnswerDev.Model.User.Dto.fromClient.SignUpClientDto;
import com.hufs.AnswerDev.Model.User.Dto.fromServer.ResponseTokenServerDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.ExecutionException;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
public class UserController {
    @Autowired
    UserService userService;

    @PostMapping("/signup")
    public ResponseTokenServerDto signUp(@RequestBody SignUpClientDto dto) throws ExecutionException, InterruptedException {
        return userService.signUp(dto).get();
    }

    @PostMapping("/login")
    public ResponseTokenServerDto login(@RequestBody LoginClientDto dto) throws ExecutionException, InterruptedException {
        return userService.login(dto).get();
    }
}

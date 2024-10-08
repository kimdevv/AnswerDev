package com.hufs.AnswerDev.Model.User;

import com.hufs.AnswerDev.Model.User.Dto.fromClient.LoginClientDto;
import com.hufs.AnswerDev.Model.User.Dto.fromClient.SignUpClientDto;
import com.hufs.AnswerDev.Model.User.Dto.fromServer.MyInfoServerDto;
import com.hufs.AnswerDev.Model.User.Dto.fromServer.ResponseTokenServerDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.concurrent.ExecutionException;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
public class UserController {
    @Autowired
    UserService userService;

    @CrossOrigin(origins="*", methods = RequestMethod.POST)
    @PostMapping("/signup")
    public ResponseTokenServerDto signUp(@RequestBody SignUpClientDto dto) throws ExecutionException, InterruptedException {
        return userService.signUp(dto).get();
    }

    @CrossOrigin(origins="*", methods = RequestMethod.POST)
    @PostMapping("/login")
    public ResponseTokenServerDto login(@RequestBody LoginClientDto dto) throws ExecutionException, InterruptedException {
        return userService.login(dto).get();
    }

    @CrossOrigin(origins="*", methods = RequestMethod.GET)
    @GetMapping("/myInfo")
    public MyInfoServerDto myInfo() throws ExecutionException, InterruptedException {
        return userService.myInfo().get();
    }
}

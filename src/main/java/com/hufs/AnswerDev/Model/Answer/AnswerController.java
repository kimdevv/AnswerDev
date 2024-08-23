package com.hufs.AnswerDev.Model.Answer;

import com.hufs.AnswerDev.Model.Answer.Dto.fromClient.RegisterAnswerClientDto;
import com.hufs.AnswerDev.Model.Answer.Dto.fromServer.RegisterAnswerServerDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.concurrent.ExecutionException;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/answer")
public class AnswerController {

    @Autowired
    AnswerService answerService;

    @CrossOrigin(origins="*", methods = RequestMethod.POST)
    @PostMapping("/register")
    public RegisterAnswerServerDto registerAnswer(@RequestBody RegisterAnswerClientDto dto) throws IOException, ExecutionException, InterruptedException {
        return answerService.registerAnswer(dto).get();
    }
}

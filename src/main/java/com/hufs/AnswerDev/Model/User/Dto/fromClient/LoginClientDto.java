package com.hufs.AnswerDev.Model.User.Dto.fromClient;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class LoginClientDto {
    private String username;
    private String password;
}

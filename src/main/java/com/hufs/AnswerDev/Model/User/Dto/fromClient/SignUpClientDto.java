package com.hufs.AnswerDev.Model.User.Dto.fromClient;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@RequiredArgsConstructor
public class SignUpClientDto {
    private String username;
    private String password;
    private String nickname;
    private String respectPerson;
}

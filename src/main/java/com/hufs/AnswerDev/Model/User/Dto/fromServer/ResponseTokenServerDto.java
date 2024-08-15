package com.hufs.AnswerDev.Model.User.Dto.fromServer;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class ResponseTokenServerDto {
    private String accessToken;
    private String refreshToken;
}

package com.hufs.AnswerDev.Model.Answer.Dto.fromServer;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ResponseAnswerImageServerDto {
    int imageNumber;
    String imageUrl;
}

package com.hufs.AnswerDev.Model.Answer.Dto.fromServer;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RegisterAnswerServerDto {
    ArrayList<ResponseAnswerImageServerDto> imageData;
}

package com.hufs.AnswerDev.Model.AnswerImage;

import com.hufs.AnswerDev.Model.Answer.Answer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AnswerImageService {

    @Autowired
    AnswerImageRepository answerImageRepository;

    @Async
    @Transactional
    public void saveImageToDB(Answer answer, int pageNo, String url) {
        AnswerImage newAnswerImage = AnswerImage.builder()
                .answer(answer)
                .pageNo(pageNo)
                .url(url)
                .build();

        answerImageRepository.save(newAnswerImage);
    }
}

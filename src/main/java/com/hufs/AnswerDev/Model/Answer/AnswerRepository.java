package com.hufs.AnswerDev.Model.Answer;

import org.springframework.data.jpa.repository.JpaRepository;

public interface AnswerRepository extends JpaRepository<Answer, Integer> {
    Answer findByName(String name);
    Answer findByUrl(String Url);
}

package com.hufs.AnswerDev.Model.AnswerImage;

import com.hufs.AnswerDev.Model.Answer.Answer;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Entity
public class AnswerImage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne
    @JoinColumn(name="answerId", nullable = false)
    private Answer answer;

    private int pageNo;

    @Column(length=1000)
    private String url;
}

package com.hufs.AnswerDev.Model.Answer;

import com.hufs.AnswerDev.Model.AnswerImage.AnswerImage;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Entity
public class Answer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String name;

    @Column(length=600)
    private String url;

    @OneToMany(mappedBy="answer", cascade=CascadeType.ALL, orphanRemoval=true)
    private List<AnswerImage> imageList;
}

package com.example.bilingualb10.entity;

import com.example.bilingualb10.enums.QuestionType;
import jakarta.persistence.*;
import lombok.*;
import java.time.ZonedDateTime;
import static jakarta.persistence.CascadeType.*;

@Entity
@Table(name = "answers")
@Getter
@Setter
@Builder
@NoArgsConstructor
public class Answer {
    @Id
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "answer_gen")
    @SequenceGenerator(
            name = "answer_gen",
            sequenceName = "answer_seq",
            allocationSize = 1,
            initialValue = 10)
    private Long id;
    @Column(length = 15000)
    private String statement;
    private String audioFile;
    @Enumerated(EnumType.STRING)
    private QuestionType questionType;
    private ZonedDateTime dateOfSubmission;
    private Integer count;
    private Boolean checked;
    @ManyToOne(cascade = {
            DETACH,
            MERGE,
            REFRESH
    })
    private Question question;
    @ManyToOne(cascade = {
            DETACH,
            MERGE,
            REFRESH
    })
    private User user;
    @OneToOne(mappedBy = "answer", cascade = {
            MERGE,
            DETACH,
            REFRESH
    })
    private Result result;

    public Answer(Long id, String statement, String audioFile, QuestionType questionType, ZonedDateTime dateOfSubmission, Integer count, Boolean checked, Question question, User user, Result result) {
        this.id = id;
        this.statement = statement;
        this.audioFile = audioFile;
        this.questionType = questionType;
        this.dateOfSubmission = dateOfSubmission;
        this.count = count;
        this.checked = checked;
        this.question = question;
        this.user = user;
        this.result = result;
    }
}
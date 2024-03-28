package com.example.bilingualb10.entity;

import com.example.bilingualb10.enums.QuestionType;
import jakarta.persistence.*;
import lombok.*;
import java.time.ZonedDateTime;
import java.util.List;
import static jakarta.persistence.CascadeType.*;

@Entity
@Table(name = "questions")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Question {
    @Id
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "question_gen")
    @SequenceGenerator(
            name = "question_gen",
            sequenceName = "question_seq",
            allocationSize = 1,
            initialValue = 10)
    private Long id;
    private String title;
    @Column(length = 2000)
    private String statement;
    private String correctAnswer;
    private Integer duration;
    private ZonedDateTime updatedAt;
    private Boolean enable;
    private Integer attempts;
    private String fileUrl;
    @Enumerated(EnumType.STRING)
    private QuestionType questionType;
    @Column(length = 15000)
    private String passage;
    @OneToMany(mappedBy = "question", cascade = {
            MERGE,
            DETACH,
            REFRESH,
            REMOVE
    })
    private List<Option> options;
    @ManyToOne(cascade = {
            MERGE,
            DETACH,
            REFRESH
    })
    private Test test;
    @OneToMany(mappedBy = "question", cascade = {
            MERGE,
            DETACH,
            REFRESH,
            REMOVE
    })
    private List<Answer> answers;
}
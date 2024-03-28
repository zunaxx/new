package com.example.bilingualb10.dto.questionDto;

import com.example.bilingualb10.enums.QuestionType;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.ZonedDateTime;

@Builder
@Getter
@Setter
@NoArgsConstructor
public class QuestionsResponse{
    private Long id;
    private String title;
    private QuestionType questionType;
    private Boolean enable;
    private Integer duration;
    private Integer testDuration;
    private ZonedDateTime updateAt;
    private int row_number;

    public QuestionsResponse(Long id, String title,QuestionType questionType, Boolean enable, Integer duration, Integer testDuration,ZonedDateTime updateAt, int row_number) {
        this.id = id;
        this.title = title;
        this.questionType = questionType;
        this.enable = enable;
        this.duration = duration;
        this.testDuration = testDuration;
        this.updateAt = updateAt;
        this.row_number = row_number;
    }
}
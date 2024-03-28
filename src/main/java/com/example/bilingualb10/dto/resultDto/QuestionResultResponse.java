package com.example.bilingualb10.dto.resultDto;

import com.example.bilingualb10.enums.QuestionType;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Builder
@Getter
@Setter
@NoArgsConstructor
public class QuestionResultResponse {
    private Long id;
    private String questionTitle;
    private Integer score;
    private Boolean checked;
    private QuestionType questionType;


    public QuestionResultResponse(Long id, String questionTitle, Integer score, Boolean checked,QuestionType questionType) {
        this.id = id;
        this.questionTitle = questionTitle;
        this.score = score;
        this.checked = checked;
        this.questionType=questionType;
    }
}
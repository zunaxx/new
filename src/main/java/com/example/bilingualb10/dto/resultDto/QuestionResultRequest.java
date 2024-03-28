package com.example.bilingualb10.dto.resultDto;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class QuestionResultRequest {
    private Long userId;
    private Long questionId;
    private Double score;
}
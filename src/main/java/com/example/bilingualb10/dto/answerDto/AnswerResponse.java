package com.example.bilingualb10.dto.answerDto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AnswerResponse {
    private Long id;
    private String questionType;
    private Long userId;
    private Long questionId;
    private Long resultId;
}
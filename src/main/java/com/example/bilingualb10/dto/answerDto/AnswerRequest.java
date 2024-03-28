package com.example.bilingualb10.dto.answerDto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AnswerRequest {
    private String statement;
    private List<Long> optionsId;
    private String audioUrl;
    private Long questionId;
    private Integer count;
}
package com.example.bilingualb10.dto.questionDto;

import com.example.bilingualb10.enums.QuestionType;
import lombok.*;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class QuestionResponse {
    private Long id;
    private QuestionType questionType;
    private String title;
    private String statement;
    private String correctAnswer;
    private Integer duration;
    private Integer attempts;
    private String fileUrl;
    private String passage;
    private Boolean enable;
}
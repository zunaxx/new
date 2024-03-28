package com.example.bilingualb10.dto.questionDto;

import com.example.bilingualb10.dto.optionDto.OptionRequest;
import lombok.*;
import java.util.List;

@Builder
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class QuestionRequest {
    private String title;
    private String statement;
    private String correctAnswer;
    private Integer duration;
    private Integer attempts;
    private String fileUrl;
    private String passage;
    private List<OptionRequest>options;
}
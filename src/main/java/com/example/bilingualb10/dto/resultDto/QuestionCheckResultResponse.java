package com.example.bilingualb10.dto.resultDto;

import com.example.bilingualb10.dto.optionDto.OptionResponse;
import com.example.bilingualb10.entity.Answer;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class QuestionCheckResultResponse {
    private String fullName;
    private String testTitle;
    private String questionTitle;
    private Integer duration;
    private String questionType;
    private List<OptionResponse> optionList;
    private List<OptionResponse> optionFromUser;
    private Integer attempts;
    private String audioFile;
    private String correctAnswer;
    private String statement;
    private String passage;
    private Answer answer;
    private String respond;
    private Integer number;
    private Integer count;
    private Integer score;

    public QuestionCheckResultResponse(String fullName, String testTitle, String questionTitle, Integer duration, String questionType, String statement, String audioUrl, String correctAnswer , Integer score) {
        this.fullName = fullName;
        this.testTitle = testTitle;
        this.questionTitle = questionTitle;
        this.duration = duration;
        this.questionType = questionType;
        this.statement = statement;
        this.audioFile = audioUrl;
        this.correctAnswer = correctAnswer;
        this.score = score;
    }
}
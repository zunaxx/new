package com.example.bilingualb10.dto.resultDto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.util.List;

@Setter
@Getter
@Builder
@NoArgsConstructor
public class TestResultResponse {
    private String userFullName;
    private String dateOfSubmission;
    private String testName;
    private Boolean checked;
    private Integer finalScore;
    private Integer maxScore;
    private Long userId;
    private Long testId;
    private List<QuestionResultResponse> questionResultResponseList;

    public TestResultResponse(String userFullName, String dateOfSubmission, String testName, Boolean checked, Integer finalScore, Integer maxScore, Long userId, Long testId, List<QuestionResultResponse> questionResultResponseList) {
        this.userFullName = userFullName;
        this.dateOfSubmission = dateOfSubmission;
        this.testName = testName;
        this.checked = checked;
        this.finalScore = finalScore;
        this.maxScore = maxScore;
        this.userId = userId;
        this.testId = testId;
        this.questionResultResponseList = questionResultResponseList;
    }

    public TestResultResponse(String userFullName, String dateOfSubmission, String testName, Boolean checked, Integer finalScore, Long userId, Long testId, List<QuestionResultResponse> questionResultResponseList) {
        this.userFullName = userFullName;
        this.dateOfSubmission = dateOfSubmission;
        this.testName = testName;
        this.checked = checked;
        this.finalScore = finalScore;
        this.userId = userId;
        this.testId = testId;
        this.questionResultResponseList = questionResultResponseList;
    }
}
package com.example.bilingualb10.repository.dao;

import com.example.bilingualb10.dto.resultDto.QuestionCheckResultResponse;
import com.example.bilingualb10.dto.resultDto.QuestionResultResponse;
import com.example.bilingualb10.dto.resultDto.TestResultResponse;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ResultDao {

    List<TestResultResponse> getAllTestResults();
    TestResultResponse getTestResultById(Long testId, Long userId);
    List<QuestionResultResponse> getAllQuestionResultsByTestIdAndUserId(Long testId, Long userId);
    List<TestResultResponse> getAllTestResultsLikeUser(Long userId);
    List<Long> getAllAnswersOfOneUserForOneTest(Long userId, Long testId);
    QuestionCheckResultResponse getCheckResult(Long userId, Long questionId);
    QuestionCheckResultResponse recordSayingStatementCheck(Long userId, Long questionId);
    QuestionCheckResultResponse respondInNAndHighlightTheAnswer(Long userId, Long questionId);
    QuestionCheckResultResponse getQuestionsResultWithFiles(Long userId, Long questionId);

    List<Boolean> getAllStatusesByUserIdAndTestId(Long userId, Long testId);
}
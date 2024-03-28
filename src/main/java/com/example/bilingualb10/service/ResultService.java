package com.example.bilingualb10.service;

import com.example.bilingualb10.dto.SimpleResponse;
import com.example.bilingualb10.dto.resultDto.QuestionCheckResultResponse;
import com.example.bilingualb10.dto.resultDto.QuestionResultRequest;
import com.example.bilingualb10.dto.resultDto.TestResultResponse;
import java.util.List;

public interface ResultService {
    SimpleResponse saveQuestionResult(QuestionResultRequest questionResultRequest);
    List<TestResultResponse> getAllTestResults();
    TestResultResponse getTestResultById(Long testId, Long userId);
    List<TestResultResponse> getAllTestResultsLikeUser();
    SimpleResponse deleteTestResult(Long userId, Long testId);
    QuestionCheckResultResponse getQuestionsResults(Long userId, Long questionId);
}
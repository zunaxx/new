package com.example.bilingualb10.service;

import com.example.bilingualb10.dto.SimpleResponse;
import com.example.bilingualb10.dto.questionDto.QuestionsResponse;
import com.example.bilingualb10.dto.testDto.PassTestByIdResponse;
import com.example.bilingualb10.dto.testDto.TestRequest;
import com.example.bilingualb10.dto.testDto.TestResponse;
import java.util.List;

public interface TestService {
    SimpleResponse createTest(TestRequest testRequest);
    List<TestResponse> getAllTest();
    List<QuestionsResponse> getAllQuestionsByTestId(Long testId);
    SimpleResponse updateTest(Long testId, TestRequest testRequest);
    SimpleResponse deleteTestById(Long testId);
    Boolean updateEnable(Long testId, Boolean enable);
    List<PassTestByIdResponse> passTest(Long testId);
}
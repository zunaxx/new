package com.example.bilingualb10.repository.dao;

import com.example.bilingualb10.dto.questionDto.QuestionsResponse;
import com.example.bilingualb10.dto.testDto.PassTestByIdResponse;
import com.example.bilingualb10.dto.testDto.TestResponse;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface TestDao {
    List<TestResponse> getAllTests();
    List<QuestionsResponse> getAllQuestionsByTestId(Long testId);
    List<PassTestByIdResponse> passTest(Long testId);
}
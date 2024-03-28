package com.example.bilingualb10.repository.dao;

import com.example.bilingualb10.dto.questionDto.QuestionResponse;

public interface QuestionDao {
    QuestionResponse getQuestionById(Long questionId);
}
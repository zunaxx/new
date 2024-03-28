package com.example.bilingualb10.service;

import com.example.bilingualb10.dto.SimpleResponse;
import com.example.bilingualb10.dto.questionDto.OptionRequest;
import com.example.bilingualb10.dto.questionDto.QuestionRequest;
import com.example.bilingualb10.dto.optionDto.OptionResponse;
import com.example.bilingualb10.dto.questionDto.QuestionResponse;
import com.example.bilingualb10.enums.QuestionType;
import java.util.List;

public interface QuestionService {
    SimpleResponse createQuestion(Long testId, QuestionType questionType, QuestionRequest questionRequest);
    SimpleResponse updateQuestions(Long questionId, QuestionRequest questionRequest);
    QuestionResponse getByIdQuestion(Long questionId);
    SimpleResponse deleteQuestions(Long questionId);
    SimpleResponse deleteOption(Long optionId);
    SimpleResponse updateOptionsIsTrue(Long optionId, Boolean isTrue);
    Boolean updateQuestionEnable(Long questionId, Boolean enable);
    List<OptionResponse> getOptionsByQuestionId(Long questionId);
    SimpleResponse saveOption(Long questionId, OptionRequest optionRequest);
}
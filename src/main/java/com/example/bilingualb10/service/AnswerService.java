package com.example.bilingualb10.service;

import com.example.bilingualb10.dto.SimpleResponse;
import com.example.bilingualb10.dto.answerDto.AnswerRequest;
import com.example.bilingualb10.dto.answerDto.AnswerResponse;

import java.util.List;

public interface AnswerService {
    SimpleResponse saveUserAnswer( List<AnswerRequest> answerRequest);

    List<AnswerResponse> getAll();
}
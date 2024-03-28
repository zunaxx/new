package com.example.bilingualb10.repository.dao;

import com.example.bilingualb10.dto.answerDto.AnswerResponse;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface AnswerDao {

    List<AnswerResponse> getAll();
}
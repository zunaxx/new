package com.example.bilingualb10.repository.dao;

import com.example.bilingualb10.dto.optionDto.OptionResponse;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface OptionDao {
    List<OptionResponse> getOptionsByQuestionId(Long questionId);
}
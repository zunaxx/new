package com.example.bilingualb10.api;

import com.example.bilingualb10.dto.SimpleResponse;
import com.example.bilingualb10.dto.answerDto.AnswerRequest;
import com.example.bilingualb10.dto.answerDto.AnswerResponse;
import com.example.bilingualb10.service.AnswerService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/answer/")
@PreAuthorize("hasAnyAuthority('ADMIN', 'USER')")
@Tag(name = "answer API")
@CrossOrigin(origins = "*", maxAge = 3600)
public class AnswerApi {
    private final AnswerService answerService;

    @PostMapping
    @Operation(summary = "Метод для сохранения ответов пройденного теста пользователем")
    public SimpleResponse saveAnswer(@RequestBody List<AnswerRequest> answerRequest){
        return  answerService.saveUserAnswer(answerRequest);
    }

    @GetMapping
    @Operation(summary = "Метод для доставания ответов пройденного теста пользователем")
    public List<AnswerResponse> getAll(){
        return answerService.getAll();
    }
}
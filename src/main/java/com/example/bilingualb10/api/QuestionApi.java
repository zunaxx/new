package com.example.bilingualb10.api;

import com.example.bilingualb10.dto.SimpleResponse;
import com.example.bilingualb10.dto.optionDto.OptionResponse;
import com.example.bilingualb10.dto.questionDto.OptionRequest;
import com.example.bilingualb10.dto.questionDto.QuestionRequest;
import com.example.bilingualb10.dto.questionDto.QuestionResponse;
import com.example.bilingualb10.enums.QuestionType;
import com.example.bilingualb10.service.QuestionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/questions")
@PreAuthorize("hasAnyAuthority('ADMIN', 'USER')")
@Tag(name = "question API")
@CrossOrigin(origins = "*", maxAge = 3600)
public class QuestionApi {
    private final QuestionService questionService;

    @PostMapping
    @Operation(summary = "Метод для создания вопросов ")
    public SimpleResponse createQuestion(@RequestParam Long testId,
                                         @RequestParam QuestionType questionType,
                                         @RequestBody QuestionRequest questionRequest) {
        return questionService.createQuestion(testId, questionType, questionRequest);
    }

    @PutMapping
    @Operation(summary = "Метод для обновления вопросов по id")
    public SimpleResponse updateQuestions(@RequestParam Long questionId,
                                          @RequestBody QuestionRequest questionRequest) {
        return questionService.updateQuestions(questionId, questionRequest);
    }

    @PutMapping("/updateOption")
    @Operation(summary = "Метод для изменения isTrue по id варианта")
    public SimpleResponse updateOption (@RequestParam Long optionId,
                                          @RequestBody Boolean isTrue) {
        return questionService.updateOptionsIsTrue(optionId, isTrue);
    }

    @GetMapping
    @Operation(summary = "Метод для того что бы найти вопрос по id")
    public QuestionResponse getById(@RequestParam Long questionId){
        return questionService.getByIdQuestion(questionId);
    }

    @DeleteMapping
    @Operation(summary = "Метод для удаления вопросов по id  ")
    public SimpleResponse deleteQuestions(@RequestParam Long questionId) {
        return questionService.deleteQuestions(questionId);
    }

    @DeleteMapping("/deleteOption")
    @Operation(summary = "Метод для удаления опций по id ")
    public SimpleResponse deleteOption(@RequestParam Long optionId){
        return questionService.deleteOption(optionId);
    }

    @PutMapping("/updateEnable")
    @Operation(summary = "Метод для изменения enable по id вопроса")
    public Boolean updateQuestionEnable (@RequestParam Long questionId,
                                        @RequestBody Boolean enable) {
        return questionService.updateQuestionEnable(questionId, enable);
    }

    @GetMapping("/getOptionsByQuestionId")
    @Operation(summary = "Метод для доставания options по question Id")
    public List<OptionResponse> getOptionsByQuestionId(@RequestParam Long questionId){
        return questionService.getOptionsByQuestionId(questionId);
    }

    @PostMapping("/saveOption")
    @Operation(summary = "Метод для сохранения options на question ")
    public SimpleResponse saveOption(@RequestParam Long questionId,
                                     @RequestBody OptionRequest optionRequest){
        return questionService.saveOption(questionId,optionRequest);
    }
}
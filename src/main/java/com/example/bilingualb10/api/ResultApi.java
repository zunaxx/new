package com.example.bilingualb10.api;

import com.example.bilingualb10.dto.SimpleResponse;
import com.example.bilingualb10.dto.resultDto.QuestionCheckResultResponse;
import com.example.bilingualb10.dto.resultDto.QuestionResultRequest;
import com.example.bilingualb10.dto.resultDto.TestResultResponse;
import com.example.bilingualb10.service.ResultService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/result/")
@PreAuthorize("hasAnyAuthority('ADMIN', 'USER')")
@Tag(name = "result API")
@CrossOrigin(origins = "*", maxAge = 3600)
public class ResultApi {
    private final ResultService resultService;

    @GetMapping
    @Operation(summary = "Метод для доставания результатов всех тестов от всех юзеров")
    List<TestResultResponse> getAllTestResults() {
        return resultService.getAllTestResults();
    }

    @GetMapping("/getById")
    @Operation(summary = "Метод для достания результата одного теста одного юзера")
    TestResultResponse getTestResultById(@RequestParam Long testId,
                                         @RequestParam Long userId) {
        return resultService.getTestResultById(testId, userId);
    }

    @PostMapping()
    @Operation(summary = "Метод, который создает результат для answer")
    SimpleResponse saveQuestionResult(@RequestBody QuestionResultRequest questionResultRequest) {
        return resultService.saveQuestionResult(questionResultRequest);
    }

    @GetMapping("/userGetResults")
    @Operation(summary = "Метод, который хочет достать результаты как юзер")
    List<TestResultResponse> getAllTestResultsLikeUser() {
        return resultService.getAllTestResultsLikeUser();
    }

    @DeleteMapping
    @Operation(summary = "Метод, который хочет удалить результаты теста одного юзера")
    SimpleResponse deleteTestResult(@RequestParam Long userId,
                                    @RequestParam Long testId) {
        return resultService.deleteTestResult(userId, testId);
    }

    @GetMapping("/getQuestionsResults")
    @Operation(summary = " Метод, для достования результатов пользователя")
    QuestionCheckResultResponse getQuestionsResults(@RequestParam Long userId,
                                                    @RequestParam Long questionId){
        return resultService.getQuestionsResults(userId, questionId);
    }
}
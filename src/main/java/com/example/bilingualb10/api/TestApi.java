package com.example.bilingualb10.api;

import com.example.bilingualb10.dto.SimpleResponse;
import com.example.bilingualb10.dto.questionDto.QuestionsResponse;
import com.example.bilingualb10.dto.testDto.PassTestByIdResponse;
import com.example.bilingualb10.dto.testDto.TestRequest;
import com.example.bilingualb10.dto.testDto.TestResponse;
import com.example.bilingualb10.service.TestService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/tests")
@PreAuthorize("hasAnyAuthority('ADMIN', 'USER')")
@Tag(name = "test API")
@CrossOrigin(origins = "*", maxAge = 3600)
public class TestApi {
    private final TestService testService;

    @PostMapping
    @Operation(summary = "Метод для создания теста ")
    public SimpleResponse createTest(@RequestBody @Valid TestRequest testRequest) {
        return testService.createTest(testRequest);
    }

    @GetMapping
    @Operation(summary = "Метод для того что бы вывести все существующие тесты ")
    public List<TestResponse> getAllTest() {
        return testService.getAllTest();
    }

    @GetMapping("/getById")
    @Operation(summary = "Выводит все вопросы по одному тесту id")
    public List<QuestionsResponse> getTestById(@RequestParam Long testId) {
        return testService.getAllQuestionsByTestId(testId);
    }

    @PutMapping
    @Operation(summary = "Метод для изменения теста по id")
    public SimpleResponse updateTestById(@RequestParam Long testId,
                                         @RequestBody @Valid TestRequest testRequest) {
        return testService.updateTest(testId, testRequest);
    }

    @DeleteMapping
    @Operation(summary = "Метод для удаления теста по id")
    public SimpleResponse deleteTestById(@RequestParam Long testId) {
        return testService.deleteTestById(testId);
    }

    @PutMapping("/updateEnable")
    @Operation(summary = "Метод для изменения enable по test id")
    public Boolean updateEnableTest(@RequestParam Long testId,
                                    @RequestBody @Valid Boolean enable) {
        return testService.updateEnable(testId, enable);
    }

    @GetMapping("/getAllQuestionsByTestId")
    @Operation(summary = "Метод для доставания всех вопросов по test id (user pass)")
    public List<PassTestByIdResponse> getAll(@RequestParam Long testId) {
        return testService.passTest(testId);
    }
}
package com.example.bilingualb10.service.impl;

import com.example.bilingualb10.dto.SimpleResponse;
import com.example.bilingualb10.dto.questionDto.QuestionsResponse;
import com.example.bilingualb10.dto.testDto.PassTestByIdResponse;
import com.example.bilingualb10.dto.testDto.TestRequest;
import com.example.bilingualb10.dto.testDto.TestResponse;
import com.example.bilingualb10.entity.Question;
import com.example.bilingualb10.entity.Test;
import com.example.bilingualb10.entity.User;
import com.example.bilingualb10.enums.Role;
import com.example.bilingualb10.globalException.AlreadyExistsException;
import com.example.bilingualb10.globalException.BadRequestException;
import com.example.bilingualb10.globalException.NotFoundException;
import com.example.bilingualb10.repository.TestRepository;
import com.example.bilingualb10.repository.dao.TestDao;
import com.example.bilingualb10.service.AuthenticationService;
import com.example.bilingualb10.service.QuestionService;
import com.example.bilingualb10.service.TestService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class TestServiceImpl implements TestService {
    private final TestRepository testRepository;
    private final TestDao testDao;
    private final QuestionService questionService;
    private final AuthenticationService authenticationService;

    @Override
    public SimpleResponse createTest(TestRequest testRequest) {
        authenticationService.checkAuthentication(Role.USER);
        Test test = new Test();
        test.setTitle(testRequest.getTitle());
        test.setDescription(testRequest.getDescription());
        test.setEnable(false);
        testRepository.save(test);
        log.info(String.format("Test with id: %s successfully saved", test.getId()));
        return new SimpleResponse(
                HttpStatus.OK,
                String.format("Test with id: %s successfully saved", test.getId())
        );
    }

    @Override
    public List<TestResponse> getAllTest() {
        return testDao.getAllTests();
    }

    @Override
    public List<QuestionsResponse> getAllQuestionsByTestId(Long testId) {
        Test test = testRepository.findById(testId).orElseThrow(() ->
                new NotFoundException(String.format("Test with id: %s not found", testId)));
        for (QuestionsResponse q: testDao.getAllQuestionsByTestId(testId)) {
            test.setDuration(q.getTestDuration());
        }
        testRepository.save(test);
        return testDao.getAllQuestionsByTestId(testId);
    }

    @Override
    public SimpleResponse updateTest(Long testId, TestRequest testRequest) {
        authenticationService.checkAuthentication(Role.USER);
        Test test = testRepository.findById(testId).orElseThrow(() -> {
            log.error(String.format("Test with id: %s not found", testId));
            return new NotFoundException(String.format("Test with id: %s not found", testId));
        });
        if (!testRequest.getTitle().isEmpty()) {
            if (!testRequest.getTitle().equalsIgnoreCase(test.getTitle())) {
                if (!testRequest.getTitle().equalsIgnoreCase("STRING")) {
                    test.setTitle(testRequest.getTitle());
                }
            }
        } else {
            throw new BadRequestException("Test title must contain letter");
        }
        if (!testRequest.getDescription().isEmpty()) {
            if (!testRequest.getDescription().equalsIgnoreCase(test.getDescription())) {
                if (!testRequest.getDescription().equalsIgnoreCase("STRING")) {
                    test.setDescription(testRequest.getDescription());
                }
            }
        } else {
            throw new BadRequestException("Test description must contain letter");
        }
        testRepository.save(test);
        log.info(String.format("Test with id: %s successfully updated", testId));
        return new SimpleResponse(
                HttpStatus.OK,
                String.format("Test with id: %s is successfully updated", testId)
        );
    }

    @Override
    public SimpleResponse deleteTestById(Long testId) {
        authenticationService.checkAuthentication(Role.USER);
        Test test = testRepository.findById(testId).orElseThrow(() -> {
            log.error(String.format("Test with id: %s not found", testId));
            return new NotFoundException(String.format("Test with id: %s not found", testId));
        });
        if (test.getUsers().isEmpty() || test.getEnable().equals(false)) {
            List<Question> questions = test.getQuestions();
            for (Question q : questions) {
                questionService.deleteQuestions(q.getId());
            }
            List<User> users = test.getUsers();
            for (User user : users) {
                user.setTests(null);
            }
            testRepository.delete(test);
            return new SimpleResponse(
                    HttpStatus.OK,
                    String.format("Test with id %s is successfully deleted", testId)
            );
        } else {
            throw new AlreadyExistsException("The test already has results, or the test status is ENABLE!!!");
        }
    }

    @Override
    public Boolean updateEnable(Long testId, Boolean enable) {
        authenticationService.checkAuthentication(Role.USER);
        Test test = testRepository.findById(testId).orElseThrow(() -> {
            log.error(String.format("Test with id: %s not found", testId));
            return new NotFoundException(String.format("Test with id: %s not found", testId));
        });
        test.setEnable(enable);
        return test.getEnable();
    }

    @Override
    public List<PassTestByIdResponse> passTest(Long testId) {
        List<PassTestByIdResponse>passTestByIdResponses = new ArrayList<>();
        Test test = testRepository.findById(testId).orElseThrow(() ->
                new NotFoundException(String.format("Test with id: %s not found", testId)));
        if(test.getEnable().equals(true)){
            return testDao.passTest(testId);
        }
        return passTestByIdResponses;
    }
}
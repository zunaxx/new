package com.example.bilingualb10.service.impl;

import com.example.bilingualb10.dto.SimpleResponse;
import com.example.bilingualb10.dto.optionDto.OptionRequest;
import com.example.bilingualb10.dto.questionDto.QuestionRequest;
import com.example.bilingualb10.dto.optionDto.OptionResponse;
import com.example.bilingualb10.dto.questionDto.QuestionResponse;
import com.example.bilingualb10.entity.Option;
import com.example.bilingualb10.entity.Question;
import com.example.bilingualb10.entity.Test;
import com.example.bilingualb10.enums.QuestionType;
import com.example.bilingualb10.enums.Role;
import com.example.bilingualb10.globalException.BadRequestException;
import com.example.bilingualb10.globalException.NotFoundException;
import com.example.bilingualb10.repository.OptionRepository;
import com.example.bilingualb10.repository.QuestionRepository;
import com.example.bilingualb10.repository.TestRepository;
import com.example.bilingualb10.repository.dao.QuestionDao;
import com.example.bilingualb10.service.AuthenticationService;
import com.example.bilingualb10.repository.dao.OptionDao;
import com.example.bilingualb10.service.QuestionService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class QuestionsUniServiceImpl implements QuestionService {
    private final OptionRepository optionRepository;
    private final QuestionRepository questionRepository;
    private final TestRepository testRepository;
    private final AuthenticationService authenticationService;
    private final QuestionDao questionDao;
    private final OptionDao optionDao;

    @Override
    public SimpleResponse createQuestion(Long testId, QuestionType questionType, QuestionRequest questionRequest) {
        authenticationService.checkAuthentication(Role.USER);
        Long id;
        switch (questionType) {
            case SELECT_REAL_ENGLISH_WORD -> id = createSelectRealEnglishWord(testId, questionType, questionRequest);
            case RECORD_SAYING_STATEMENT -> id = createRecordSayingStatement(testId, questionType, questionRequest);
            case TYPE_WHAT_YOU_HEAR -> id = createTypeWhatYouHear(testId, questionType, questionRequest);
            case DESCRIBE_IMAGE -> id = createDescribeImage(testId, questionType, questionRequest);
            case RESPOND_AT_LEAST_N_WORDS -> id = createRespondInAtLeastNWords(testId, questionType, questionRequest);
            case LISTEN_AND_SELECT_ENGLISH_WORDS -> id = createListenAndSelectRealEnglishWord(testId, questionType, questionRequest);
            case SELECT_THE_BEST_TITLE -> id = createSelectTheBestTitle(testId, questionType, questionRequest);
            case HIGHLIGHT_THE_ANSWER -> id = createHighlightTheAnswer(testId, questionType, questionRequest);
            case SELECT_THE_MAIN_IDEA -> id = createSelectTheMainIdea(testId, questionType, questionRequest);
            default -> throw new BadRequestException("Question save failed");
        }
        return new SimpleResponse(
                HttpStatus.CREATED,
                "Question with id: " + id + " is successfully created !!!");
    }

    private Long createSelectRealEnglishWord(Long testId, QuestionType questionType, QuestionRequest questionRequest) {
        Test test = testRepository.findById(testId).orElseThrow(() -> {
            log.error("There are no any tests with ID: " + testId);
            return new NotFoundException(String.format("Test with ID: %s is not found !!!", testId));
        });
        Question question = Question
                .builder()
                .title(questionRequest.getTitle())
                .questionType(questionType)
                .duration(questionRequest.getDuration())
                .enable(false)
                .test(test)
                .build();
        List<Option> optionList = new ArrayList<>();

        for (OptionRequest optionRequest : questionRequest.getOptions()) {
            Option option = new Option();
            option.setTitle(optionRequest.getTitle());
            option.setIsTrue(optionRequest.getIsTrue() != null ? optionRequest.getIsTrue() : false);
            option.setQuestion(question);
            optionList.add(option);
            optionRepository.save(option);
        }
        testRepository.save(test);
        questionRepository.save(question);
        log.info("Question is successfully created (SELECT_REAL_ENGLISH_WORD)");
        return question.getId();
    }

    private Long createListenAndSelectRealEnglishWord(Long testId, QuestionType questionType, QuestionRequest questionRequest) {
        Test test = testRepository.findById(testId).orElseThrow(() -> {
            log.error(String.format("Test with id: %s not found", testId));
            return new NotFoundException(String.format("Test with id: %s not found", testId));
        });
        Question question = Question
                .builder()
                .title(questionRequest.getTitle())
                .questionType(questionType)
                .duration(questionRequest.getDuration())
                .enable(false)
                .test(test)
                .build();

        List<Option> optionList = new ArrayList<>();
        for (OptionRequest optionRequest : questionRequest.getOptions()) {
            Option option = new Option();
            if (optionRequest.getAudioUrl() == null || optionRequest.getAudioUrl().isEmpty()) {
                log.error("Option must contain audio file");
                throw new BadRequestException("Option must contain audio file");
            }
            option.setTitle(optionRequest.getTitle());
            option.setIsTrue(optionRequest.getIsTrue() != null ? optionRequest.getIsTrue() : false);
            option.setQuestion(question);
            option.setAudioUrl(optionRequest.getAudioUrl());
            optionList.add(option);
            optionRepository.save(option);
        }
        testRepository.save(test);
        questionRepository.save(question);
        log.info("Question is successfully created (SELECT_REAL_ENGLISH_WORD)");
        return question.getId();
    }

    private Long createRecordSayingStatement(Long testId, QuestionType questionType, QuestionRequest questionRequest) {
        Test test = testRepository.findById(testId).orElseThrow(() -> {
            log.error(String.format("Test with id: %s not found", testId));
            return new NotFoundException(String.format("Test with id: %s not found", testId));
        });
        Question question = Question.builder()
                .title(questionRequest.getTitle())
                .questionType(questionType)
                .statement(questionRequest.getStatement())
                .duration(questionRequest.getDuration())
                .enable(false)
                .test(test)
                .build();
        testRepository.save(test);
        questionRepository.save(question);
        log.info("Question is successfully created (RECORD_SAYING_STATEMENT)");
        return question.getId();
    }

    private Long createTypeWhatYouHear(Long testId, QuestionType questionType, QuestionRequest questionRequest) {
        Test test = testRepository.findById(testId).orElseThrow(() -> {
            log.error("There are no any tests with ID: " + testId);
            return new NotFoundException(String.format("Test with ID: %s is not found !!!", testId));
        });
        Question question = Question.builder()
                .title(questionRequest.getTitle())
                .questionType(questionType)
                .duration(questionRequest.getDuration())
                .attempts(questionRequest.getAttempts())
                .fileUrl(questionRequest.getFileUrl())
                .correctAnswer(questionRequest.getCorrectAnswer())
                .enable(false)
                .test(test)
                .build();
        testRepository.save(test);
        questionRepository.save(question);
        log.info("Question is successfully created (TYPE_WHAT_YOU_HEAR)");
        return question.getId();
    }

    private Long createSelectTheBestTitle(Long testId, QuestionType questionType, QuestionRequest questionRequest) {
        Test test = testRepository.findById(testId).orElseThrow(() -> {
            log.error("There are no any tests with ID: " + testId);
            return new NotFoundException(String.format("Test with ID: %s is not found !!!", testId));
        });
        Question question = Question
                .builder()
                .title(questionRequest.getTitle())
                .questionType(questionType)
                .duration(questionRequest.getDuration())
                .passage(questionRequest.getPassage())
                .enable(false)
                .test(test)
                .build();

        List<Option> optionList = new ArrayList<>();
        int counter = 0;
        for (OptionRequest optionRequest : questionRequest.getOptions()) {
            Option option = new Option();
            option.setTitle(optionRequest.getTitle());
            option.setIsTrue(optionRequest.getIsTrue() != null ? optionRequest.getIsTrue() : false);
            if (optionRequest.getIsTrue()) {
                counter++;
                if (counter > 1) {
                    throw new BadRequestException("The question must accept only one True option !!!");
                }
            }
            option.setQuestion(question);
            optionList.add(option);
            optionRepository.save(option);
        }
        testRepository.save(test);
        questionRepository.save(question);
        log.info("Question is successfully created (SELECT_THE_BEST_TITLE)");
        return question.getId();
    }

    private Long createRespondInAtLeastNWords(Long testId, QuestionType questionType, QuestionRequest questionRequest) {
        Test test = testRepository.findById(testId).orElseThrow(() -> {
            log.error("There are no any tests with ID: " + testId);
            return new NotFoundException(String.format("Test with ID: %s is not found !!!", testId));
        });
        Question question = Question.builder()
                .title(questionRequest.getTitle())
                .questionType(questionType)
                .duration(questionRequest.getDuration())
                .statement(questionRequest.getStatement())
                .attempts(questionRequest.getAttempts())
                .enable(false)
                .test(test)
                .build();
        testRepository.save(test);
        questionRepository.save(question);
        log.info("Question is successfully created (RESPOND_AT_LEAST_N_WORDS)");
        return question.getId();
    }

    private Long createDescribeImage(Long testId, QuestionType questionType, QuestionRequest questionReques) {
        Test test = testRepository.findById(testId).orElseThrow(() -> {
            log.error(String.format("Test with id: %s not found", testId));
            return new NotFoundException(String.format("Test with id: %s not found", testId));
        });
        Question question = Question
                .builder()
                .title(questionReques.getTitle())
                .questionType(questionType)
                .duration(questionReques.getDuration())
                .fileUrl(questionReques.getFileUrl())
                .correctAnswer(questionReques.getCorrectAnswer())
                .enable(false)
                .test(test)
                .build();
        questionRepository.save(question);
        testRepository.save(test);
        log.info("Question successfully created (DESCRIBE_IMAGE");
        return question.getId();
    }

    private Long createSelectTheMainIdea(Long testId, QuestionType questionType, QuestionRequest questionRequest) {
        Test test = testRepository.findById(testId).orElseThrow(() -> {
            log.error(String.format("Test with id: %s not found", testId));
            return new NotFoundException(String.format("Test with id: %s not found", testId));
        });
        Question question = Question
                .builder()
                .title(questionRequest.getTitle())
                .questionType(questionType)
                .duration(questionRequest.getDuration())
                .passage(questionRequest.getPassage())
                .correctAnswer(questionRequest.getCorrectAnswer())
                .enable(false)
                .test(test)
                .build();

        List<Option> optionList = new ArrayList<>();
        int counter = 0;
        for (OptionRequest optionRequest : questionRequest.getOptions()) {
            Option option = new Option();
            option.setTitle(optionRequest.getTitle());
            option.setIsTrue(optionRequest.getIsTrue() != null ? optionRequest.getIsTrue() : false);
            if (optionRequest.getIsTrue()) {
                counter++;
                if (counter > 1) {
                    throw new BadRequestException("The question must accept only one True option !!!");
                }
            }
            option.setQuestion(question);
            optionList.add(option);
            optionRepository.save(option);

        }
        testRepository.save(test);
        questionRepository.save(question);
        log.info("Question is successfully created (SELECT_THE_MAIN_IDEA)");
        return question.getId();
    }

    private Long createHighlightTheAnswer(Long testId, QuestionType questionType, QuestionRequest questionRequest) {
        Test test = testRepository.findById(testId).orElseThrow(() -> {
            log.error(String.format("Test with id: %s not found", testId));
            return new NotFoundException(String.format("Test with id: %s not found", testId));
        });
        Question question = Question
                .builder()
                .title(questionRequest.getTitle())
                .questionType(questionType)
                .duration(questionRequest.getDuration())
                .statement(questionRequest.getStatement())
                .passage(questionRequest.getPassage())
                .correctAnswer(questionRequest.getCorrectAnswer())
                .enable(false)
                .test(test)
                .build();
        questionRepository.save(question);
        testRepository.save(test);
        log.info(String.format("Question with id: %s successfully created", question.getId()));
        return question.getId();
    }

    @Override
    public SimpleResponse updateQuestions(Long questionId, QuestionRequest questionRequest) {
        authenticationService.checkAuthentication(Role.USER);
        Question question = questionRepository.findById(questionId).orElseThrow(() -> {
            log.error(String.format("There are no any questions with ID: " + questionId));
            return new NotFoundException(String.format("Question with ID: %s is not found !!!", questionId));
        });
        if (!question.getAnswers().isEmpty()) {
            log.error("This question can't be updated , because have result");
            throw new BadRequestException("This question can't be updated , because have result");
        } else {
            if (!questionRequest.getTitle().isEmpty()) {
                if (!questionRequest.getTitle().equalsIgnoreCase(question.getTitle())) {
                    if (!questionRequest.getTitle().equalsIgnoreCase("STRING")) {
                        question.setTitle(questionRequest.getTitle());
                    }
                }
            } else {
                throw new BadRequestException("Question title must contain title");
            }
            if (!questionRequest.getStatement().isEmpty()) {
                if (!questionRequest.getStatement().equalsIgnoreCase(question.getStatement())) {
                    if (!questionRequest.getStatement().equalsIgnoreCase("STRING")) {
                        question.setStatement(questionRequest.getStatement());
                    }
                }
            } else {
                throw new BadRequestException("Question statement must contain statement");
            }
            if (!questionRequest.getCorrectAnswer().isEmpty()) {
                if (!questionRequest.getCorrectAnswer().equalsIgnoreCase(question.getCorrectAnswer())) {
                    if (!questionRequest.getCorrectAnswer().equalsIgnoreCase(question.getCorrectAnswer())) {
                        question.setCorrectAnswer(questionRequest.getCorrectAnswer());
                    }
                }
            } else {
                throw new BadRequestException("Question correct answer must contain letter ");
            }
            if (questionRequest.getDuration() != question.getDuration()) {
                if (questionRequest.getDuration() == 0) {
                    question.setDuration(question.getDuration());
                } else {
                    question.setDuration(questionRequest.getDuration());
                }
            }
            if(questionRequest.getAttempts() != question.getAttempts()){
                if(questionRequest.getAttempts() ==0){
                    question.setAttempts(question.getAttempts());
                }else {
                    question.setAttempts(questionRequest.getAttempts());
                }
            }
            if (!questionRequest.getFileUrl().isEmpty()) {
                if (!questionRequest.getFileUrl().equalsIgnoreCase(question.getFileUrl())) {
                    if (!questionRequest.getFileUrl().equalsIgnoreCase("STRING")) {
                        question.setFileUrl(questionRequest.getFileUrl());
                    }
                }
            } else {
                throw new BadRequestException("Question must contain file Url");
            }
            if(!questionRequest.getPassage().isEmpty()){
                if(!questionRequest.getPassage().equalsIgnoreCase(question.getPassage())){
                    if(!questionRequest.getPassage().equalsIgnoreCase("STRING")){
                        question.setPassage(questionRequest.getPassage());
                    }
                }
            }else {
                throw new BadRequestException("Question must contain passage");
            }
            question.setUpdatedAt(ZonedDateTime.now());
            questionRepository.save(question);
            testRepository.save(question.getTest());
            return SimpleResponse.builder()
                    .httpStatus(HttpStatus.OK)
                    .message(String.format("Question: %s is successfully updated !!!", question.getId()))
                    .build();
        }
    }

    @Override
    public QuestionResponse getByIdQuestion(Long questionId) {
        questionRepository.findById(questionId).orElseThrow(()->{
            log.error(String.format("Question with id: %s not found",questionId));
            return new NotFoundException(String.format("Question with id: %s not found",questionId));
        });
       return questionDao.getQuestionById(questionId);
    }

    @Override
    public SimpleResponse deleteQuestions(Long questionId) {
        authenticationService.checkAuthentication(Role.USER);
        Question question = questionRepository.findById(questionId).orElseThrow(
                () -> new NotFoundException(String.format("Question with ID: %s is not found !!!", questionId))
        );
        optionRepository.deleteAll(question.getOptions());
        questionRepository.deleteById(questionId);
        return new SimpleResponse(
                HttpStatus.OK,
                String.format("Question with id: %s is successfully deleted !!!", question.getId())
        );
    }

    @Override
    public SimpleResponse deleteOption(Long optionId) {
        authenticationService.checkAuthentication(Role.USER);
        Option option = optionRepository.findById(optionId).orElseThrow(
                () -> new NotFoundException(String.format("Option with ID: %s is not found !!!", optionId))
        );
        optionRepository.deleteById(optionId);
        return SimpleResponse.builder()
                .httpStatus(HttpStatus.OK)
                .message(String.format("Option with id: %s is successfully deleted !!!", optionId))
                .build();
    }

    @Override
    public SimpleResponse updateOptionsIsTrue(Long optionId, Boolean isTrue) {
        authenticationService.checkAuthentication(Role.USER);
        Option option = optionRepository.findById(optionId).orElseThrow(() -> {
            log.error(String.format("Option with id: %s not found", optionId));
            return new NotFoundException(String.format("Option with id: %s not found", optionId));
        });
        option.setIsTrue(isTrue);
        return new SimpleResponse(
                HttpStatus.OK,
                String.format("Option with id: %s successfully updated", optionId)
        );
    }

    @Override
    public Boolean updateQuestionEnable(Long questionId, Boolean enable) {
        authenticationService.checkAuthentication(Role.USER);
        Question question = questionRepository.findById(questionId).orElseThrow(() -> {
            log.error(String.format("Question with id: %s not found", questionId));
            return new NotFoundException(String.format("Question with id: %s not found", questionId));
        });
        question.setUpdatedAt(ZonedDateTime.now());
        question.setEnable(enable);
        return question.getEnable();
    }

    @Override
    public List<OptionResponse> getOptionsByQuestionId(Long questionId) {
        authenticationService.checkAuthentication(Role.USER);
        return optionDao.getOptionsByQuestionId(questionId);
    }

    @Override
    public SimpleResponse saveOption(Long questionId, com.example.bilingualb10.dto.questionDto.OptionRequest optionRequest) {
        Question question = questionRepository.findById(questionId).orElseThrow(() -> {
            log.error(String.format("Question with id: %s not found", questionId));
            return new NotFoundException(String.format("Question with id: %s not found", questionId));
        });
        Option option = Option
                .builder()
                .title(optionRequest.getTitle())
                .audioUrl(optionRequest.getAudioUrl())
                .isTrue(optionRequest.getIsTrue())
                .question(question)
                .build();
        questionRepository.save(question);
        optionRepository.save(option);
        log.info( String.format("Option with id: %s successfully saved in question with id: %s ",option.getId(), questionId));
        return new SimpleResponse(
                HttpStatus.OK,
                String.format("Option with id: %s successfully saved in question with id: %s ",option.getId(), questionId)
        );
    }
}
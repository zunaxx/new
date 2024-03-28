package com.example.bilingualb10.service.impl;

import com.example.bilingualb10.dto.SimpleResponse;
import com.example.bilingualb10.dto.optionDto.OptionResponse;
import com.example.bilingualb10.dto.resultDto.QuestionCheckResultResponse;
import com.example.bilingualb10.dto.resultDto.QuestionResultRequest;
import com.example.bilingualb10.dto.resultDto.TestResultResponse;
import com.example.bilingualb10.entity.*;
import com.example.bilingualb10.enums.Role;
import com.example.bilingualb10.globalException.AlreadyExistsException;
import com.example.bilingualb10.globalException.BadRequestException;
import com.example.bilingualb10.globalException.NotFoundException;
import com.example.bilingualb10.repository.*;
import com.example.bilingualb10.repository.dao.OptionDao;
import com.example.bilingualb10.repository.dao.ResultDao;
import com.example.bilingualb10.service.AuthenticationService;
import com.example.bilingualb10.service.ResultService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static com.example.bilingualb10.enums.QuestionType.*;

@RequiredArgsConstructor
@Service
@Slf4j
@Transactional
public class ResultServiceImpl implements ResultService {
    private final AuthenticationService authenticationService;
    private final AnswerRepository answerRepository;
    private final QuestionRepository questionRepository;
    private final ResultDao resultDao;
    private final UserRepository userRepository;
    private final ResultRepository resultRepository;
    private final TestRepository testRepository;
    private final OptionDao optionDao;

    @Override
    public SimpleResponse saveQuestionResult(QuestionResultRequest questionResultRequest) {
        authenticationService.checkAuthentication(Role.USER);
        Question question = questionRepository.findById(questionResultRequest.getQuestionId())
                .orElseThrow(() -> {
                    log.error(String.format("Question with id: %s not found", questionResultRequest.getQuestionId()));
                    return new NotFoundException(String.format("Question with id: %s not found", questionResultRequest.getQuestionId()));
                });
        User user = userRepository.findById(questionResultRequest.getUserId())
                .orElseThrow(() -> {
                    log.error("Not found");
                    return new NotFoundException("User not found with id " + questionResultRequest.getUserId());
                });
        Answer answer = new Answer();
        for (Answer a : user.getAnswers()) {
            if (a.getQuestion().equals(question)) {
                answer = answerRepository.findById(a.getId()).orElseThrow(() -> {
                    log.error("There are no any answer with id: " + a.getId());
                    return new NotFoundException(
                            String.format("Answer with id: %s is not found !!!", a.getId()));
                });
            }
        }
        if (answer.getResult() != null)
            throw new AlreadyExistsException("Result is already exists");
        if (
                question.getQuestionType().equals(SELECT_REAL_ENGLISH_WORD) ||
                        question.getQuestionType().equals(LISTEN_AND_SELECT_ENGLISH_WORDS)
        ) {
            checkQuestionWithOptions(user, question);
        } else {
            Result result = new Result();
            if (result.getScore() < 0 || result.getScore() > 10) {
                throw new BadRequestException("Score should be greater than 0 and less than or equal to 10");
            }
            result.setAnswer(answer);
            result.setScore(questionResultRequest.getScore());
            // Лишний код
            result.setChecked(true);
            // А это не лишний код
            answer.setChecked(true);
            result.setDateOfSubmission(answer.getDateOfSubmission());
            answerRepository.save(answer);
            resultRepository.save(result);
        }
        return SimpleResponse.builder()
                .httpStatus(HttpStatus.OK)
                .message("Result successfully saved for answer with id " + answer.getId())
                .build();
    }

    private void checkQuestionWithOptions(User user, Question question) {
        ZonedDateTime dateTime = null;
        Answer answer = new Answer();
        for (Answer a : user.getAnswers()) {
            if (a.getQuestion().equals(question)) {
                dateTime = a.getDateOfSubmission();
                answer = a;
                break;
            }
        }
        List<Option> trueOptions = new ArrayList<>();
        for (Option o : question.getOptions()) {
            if (o.getIsTrue().equals(true)) {
                trueOptions.add(o);
                log.info(String.format("Option with id: %s in new List", o.getId()));
            }
        }
        List<Option> usersOptions = user.getOptions();
        usersOptions = usersOptions.stream()
                .filter(option -> option.getQuestion().getId()
                        .equals(question.getId())).collect(Collectors.toList());
        double numberOfTrueOptions = trueOptions.size();
        if (numberOfTrueOptions == 0) {
            Result result = Result
                    .builder()
                    .dateOfSubmission(dateTime)
                    .score(0)
                    .checked(true)
                    .answer(answer)
                    .build();
            answer.setChecked(true);
            answerRepository.save(answer);
            resultRepository.save(result);
            testRepository.save(question.getTest());
        } else {
            double numberOfWrongOptions;
            double numberOfAllOptions = question.getOptions().size();
            double numberOfRightChosenOptions = 0;
            for (Option option : usersOptions) {
                if (option.getIsTrue()) numberOfRightChosenOptions++;
            }
            numberOfWrongOptions = usersOptions.size() - numberOfRightChosenOptions;
            double score = ((numberOfRightChosenOptions / numberOfTrueOptions) - (numberOfWrongOptions / numberOfAllOptions)) * 10;
            if (score < 0) score = 0;
            Result result = Result
                    .builder()
                    .dateOfSubmission(dateTime)
                    .score(score)
                    .checked(true)
                    .answer(answer)
                    .build();
            answer.setChecked(true);
            answerRepository.save(answer);
            resultRepository.save(result);
            testRepository.save(question.getTest());
        }
    }

    @Override
    public List<TestResultResponse> getAllTestResults() {
        return resultDao.getAllTestResults();
    }

    @Override
    public TestResultResponse getTestResultById(Long testId, Long userId) {
        authenticationService.checkAuthentication(Role.USER);
        testRepository.findById(testId).orElseThrow(() -> {
            log.error(String.format("Test with id: %s not found", testId));
            return new NotFoundException(String.format("Test with id: %s not found", testId));
        });
        userRepository.findById(userId).orElseThrow(() -> {
            log.error(String.format("User with id: %s not found", userId));
            return new NotFoundException(String.format("User with id: %s not found", userId));
        });
        return resultDao.getTestResultById(testId, userId);
    }

    @Override
    public List<TestResultResponse> getAllTestResultsLikeUser() {
        authenticationService.checkAuthentication(Role.ADMIN);
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        User user = userRepository.getUserByEmail(email).orElseThrow(() -> {
            log.error("User with email is not found !!!");
            return new NotFoundException("User is not found !!!");
        });
        return resultDao.getAllTestResultsLikeUser(user.getId());
    }

    @Override
    public SimpleResponse deleteTestResult(Long userId, Long testId) {
        User user = userRepository.findById(userId).orElseThrow(() -> {
            log.error(String.format("User with id: %s not found", userId));
            return new NotFoundException(String.format("User with id: %s not found", userId));
        });
        Test test = testRepository.findById(testId).orElseThrow(() -> {
            log.error(String.format("Test with id: %s not found", testId));
            return new NotFoundException(String.format("Test with id: %s not found", testId));
        });
        for (Answer a : user.getAnswers()) {
            if (a.getUser().equals(user)) {
                if (a.getQuestion().getTest().equals(test)) {
                    resultRepository.deleteResultByAnswerId(a.getId());
                    answerRepository.deleteById(a.getId());
                }
            } else {
                throw new BadRequestException("This user has not taken this test");
            }
        }
        return SimpleResponse.builder()
                .httpStatus(HttpStatus.OK)
                .message(String.format("Your test with id %d was successfully deleted", testId))
                .build();
    }

    @Override
    public QuestionCheckResultResponse getQuestionsResults(Long userId, Long questionId) {
        User user = userRepository.findById(userId).orElseThrow(() -> {
            log.error(String.format("User with id: %s not found", userId));
            return new NotFoundException(String.format("User with id: %s not found", userId));
        });
        Question question = questionRepository.findById(questionId).orElseThrow(() -> {
            log.error(String.format("Question with id: %s not found", questionId));
            return new NotFoundException(String.format("Question with id: %s not found", questionId));
        });
        if (user.getAnswers() != null) {
            switch (question.getQuestionType()) {
                case RECORD_SAYING_STATEMENT -> {
                    return resultDao.recordSayingStatementCheck(userId, questionId);
                }
                case RESPOND_AT_LEAST_N_WORDS, HIGHLIGHT_THE_ANSWER -> {
                    return resultDao.respondInNAndHighlightTheAnswer(userId, questionId);
                }
                case TYPE_WHAT_YOU_HEAR, DESCRIBE_IMAGE -> {
                    return resultDao.getQuestionsResultWithFiles(userId, questionId);
                }
                case SELECT_REAL_ENGLISH_WORD,
                        LISTEN_AND_SELECT_ENGLISH_WORDS,
                        SELECT_THE_BEST_TITLE,
                        SELECT_THE_MAIN_IDEA -> {
                    return questionsWithOptions(user, question);
                }
                default -> throw new BadRequestException("Given question with no question type");
            }
        } else {  log.error(String.format("User with id: %s have not result to question with id: %s ", userId, questionId));
            throw new BadRequestException(String.format("User with id: %s have not result to question with id: %s ", userId, questionId));
        }
    }

    public QuestionCheckResultResponse questionsWithOptions(User user, Question question) {
        QuestionCheckResultResponse questionResults = resultDao.getCheckResult(user.getId(), question.getId());
        questionResults.setOptionList(optionDao.getOptionsByQuestionId(question.getId()));
        List<OptionResponse> userAnswers = new ArrayList<>();
        for (Option o : user.getOptions()) {
            if (o.getQuestion().equals(question)) userAnswers.add(Option.entityToResponse(o));
        }
        questionResults.setOptionFromUser(userAnswers);
        return questionResults;
    }
}
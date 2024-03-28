package com.example.bilingualb10.service.impl;

import com.example.bilingualb10.dto.SimpleResponse;
import com.example.bilingualb10.dto.answerDto.AnswerRequest;
import com.example.bilingualb10.dto.answerDto.AnswerResponse;
import com.example.bilingualb10.entity.*;
import com.example.bilingualb10.globalException.AccessDeniedException;
import com.example.bilingualb10.globalException.BadRequestException;
import com.example.bilingualb10.globalException.NotFoundException;
import com.example.bilingualb10.repository.*;
import com.example.bilingualb10.repository.dao.AnswerDao;
import com.example.bilingualb10.service.AnswerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.ZonedDateTime;
import java.util.List;
import static com.example.bilingualb10.enums.Role.ADMIN;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class AnswerServiceImpl implements AnswerService {
    private final AnswerRepository answerRepository;
    private final UserRepository userRepository;
    private final OptionRepository optionRepository;
    private final QuestionRepository questionRepository;
    private final TestRepository testRepository;
    private final AnswerDao answerDao;

    @Override
    public SimpleResponse saveUserAnswer(List<AnswerRequest> answerRequest) {
        for (AnswerRequest request : answerRequest) {
            Question question = questionRepository.findById(request.getQuestionId()).orElseThrow(() -> {
                log.error(String.format("Question with id: %s is not found !!!", request.getQuestionId()));
                return new NotFoundException(String.format("Question with id: %s is not found !!!", request.getQuestionId()));
            });
            Test test = testRepository.findById(question.getTest().getId()).orElseThrow(() -> {
                log.error(String.format("Test with id: %s not found", question.getTest().getId()));
                return new NotFoundException(String.format("Test with id: %s not found", question.getTest().getId()));
            });
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if (authentication == null || !authentication.isAuthenticated()) {
                throw new AccessDeniedException("Authentication required to create a answers !!!");
            }
            String email = authentication.getName();
            User user = userRepository.getUserByEmail(email).orElseThrow(() -> {
                log.error("User with email is not found !!!");
                return new NotFoundException("User is not found !!!");
            });
            if (user.getRole() == ADMIN) {
                throw new BadRequestException("Authentication required to be USER to create a answers !!!");
            }
            for (Answer a:user.getAnswers()) {
                if (a.getQuestion().getId().equals(request.getQuestionId())) {
                    throw new BadRequestException(String.format("User with id: %s can not pass this test (This user have answer to this test)", test.getId()));
                }
            }
            switch (question.getQuestionType()) {
                case SELECT_REAL_ENGLISH_WORD -> createPassSelectTheRealEnglishWord(test, user, request, question);
                case LISTEN_AND_SELECT_ENGLISH_WORDS ->
                        createPassListenAndSelectRealEnglishWord(test, user, request, question);
                case TYPE_WHAT_YOU_HEAR -> createPassTypeWhatYouHear(test, user, request, question);
                case DESCRIBE_IMAGE -> createPassDescribeImage(user, request, question);
                case RESPOND_AT_LEAST_N_WORDS -> createPassRespondAtLeastNWords(test, user, request, question);
                case HIGHLIGHT_THE_ANSWER -> createPassHighlightTheAnswer(test, user, request, question);
                case SELECT_THE_BEST_TITLE -> createPassSelectTheBestTitle(test, user, request, question);
                case SELECT_THE_MAIN_IDEA -> createPassSelectTheMainIdea(test, user, request, question);
                case RECORD_SAYING_STATEMENT -> createPassRecordSayingStatement(test, user, request, question);
            }
        }
        return new SimpleResponse(
                HttpStatus.OK,
                ("Your answer is successfully approved to check !!!")
        );
    }

    @Override
    public List<AnswerResponse> getAll() {
        return answerDao.getAll();
    }

    private void createPassSelectTheRealEnglishWord(Test test, User user, AnswerRequest answerRequest, Question question) {
        for (Long o : answerRequest.getOptionsId()) {
            Option option = optionRepository.findById(o).orElseThrow(() -> {
                log.error(String.format("Option with id: %s is not found !!!", o));
                return new NotFoundException(String.format("Option with id: %s is not found !!!", o));
            });
            option.getUsers().add(user);
            user.getOptions().add(option);
            optionRepository.save(option);
            userRepository.save(user);
        }
        user.getTests().add(test);
        testRepository.save(test);
        Answer answer = Answer.builder()
                .user(user)
                .question(question)
                .checked(false)
                .questionType(question.getQuestionType())
                .dateOfSubmission(ZonedDateTime.now())
                .build();
        questionRepository.save(question);
        answerRepository.save(answer);
        log.info("Answer is successfully saved !!!");
    }

    private void createPassListenAndSelectRealEnglishWord(Test test, User user, AnswerRequest answerRequest, Question question) {
        for (Long o : answerRequest.getOptionsId()) {
            Option option = optionRepository.findById(o).orElseThrow(() -> {
                log.error(String.format("Option with id: %s is not found !!!", o));
                return new NotFoundException(String.format("Option with id: %s is not found !!!", o));
            });
            option.getUsers().add(user);
            user.getOptions().add(option);
            optionRepository.save(option);
            userRepository.save(user);
        }
        user.getTests().add(test);
        testRepository.save(test);
        Answer answer = Answer
                .builder()
                .user(user)
                .question(question)
                .checked(false)
                .questionType(question.getQuestionType())
                .dateOfSubmission(ZonedDateTime.now())
                .build();
        questionRepository.save(question);
        answerRepository.save(answer);
        log.info("Answer successfully saved !!!");
    }

    private void createPassRespondAtLeastNWords(Test test, User user, AnswerRequest answerRequest, Question question) {
        Answer answer = Answer.builder()
                .user(user)
                .statement(answerRequest.getStatement())
                .question(question)
                .checked(false)
                .questionType(question.getQuestionType())
                .dateOfSubmission(ZonedDateTime.now())
                .count(answerRequest.getCount())
                .build();
        user.getTests().add(test);
        answerRepository.save(answer);
        testRepository.save(test);
        userRepository.save(user);
        log.info("Answer successfully saved !!!");
    }

    private void createPassTypeWhatYouHear(Test test, User user, AnswerRequest answerRequest, Question question) {
        Answer answer = Answer.builder()
                .statement(answerRequest.getStatement())
                .questionType(question.getQuestionType())
                .question(question)
                .checked(false)
                .user(user)
                .dateOfSubmission(ZonedDateTime.now())
                .build();
        user.getTests().add(test);
        answerRepository.save(answer);
        testRepository.save(test);
        userRepository.save(user);
        log.info("Answer is successfully saved !!!");
    }

    private void createPassSelectTheMainIdea(Test test, User user, AnswerRequest answerRequest, Question question) {
        Answer answer = Answer
                .builder()
                .questionType(question.getQuestionType())
                .question(question)
                .checked(false)
                .user(user)
                .dateOfSubmission(ZonedDateTime.now())
                .build();
        if (answerRequest.getOptionsId().size() != 1) {
            throw new BadRequestException("Select only one option !!!");
        }
        for (Long o : answerRequest.getOptionsId()) {
            Option option = optionRepository.findById(o).orElseThrow(() -> {
                log.error(String.format("Option with id: %s is not found !!!", o));
                return new NotFoundException(String.format("Option with id: %s is not found !!!", o));
            });
            option.getUsers().add(user);
            user.getOptions().add(option);
            optionRepository.save(option);
            userRepository.save(user);
        }
        user.getTests().add(test);
        answerRepository.save(answer);
        testRepository.save(test);
        userRepository.save(user);
        log.info("Answer is successfully saved !!!");
    }

    private void createPassDescribeImage(User user, AnswerRequest answerRequest, Question question) {
        Test test = question.getTest();
        Answer answer = Answer
                .builder()
                .questionType(question.getQuestionType())
                .question(question)
                .checked(false)
                .user(user)
                .statement(answerRequest.getStatement())
                .dateOfSubmission(ZonedDateTime.now())
                .build();
        user.getTests().add(test);
        answerRepository.save(answer);
        testRepository.save(test);
        userRepository.save(user);
        log.info("Answer is successfully saved !!!");
    }

    private void createPassSelectTheBestTitle(Test test, User user, AnswerRequest answerRequest, Question question) {
        Answer answer = Answer
                .builder()
                .questionType(question.getQuestionType())
                .question(question)
                .checked(false)
                .user(user)
                .dateOfSubmission(ZonedDateTime.now())
                .build();
        if (answerRequest.getOptionsId().size() != 1) {
            throw new BadRequestException("Select only one option !!!");
        }
        for (Long o : answerRequest.getOptionsId()) {
            Option option = optionRepository.findById(o).orElseThrow(() -> {
                log.error(String.format("Option with id: %s is not found !!!", o));
                return new NotFoundException(String.format("Option with id: %s is not found !!!", o));
            });
            option.getUsers().add(user);
            user.getOptions().add(option);
            optionRepository.save(option);
            userRepository.save(user);
        }
        user.getTests().add(test);
        answerRepository.save(answer);
        testRepository.save(test);
        userRepository.save(user);
        log.info("Answer is successfully saved !!!");
    }

    private void createPassHighlightTheAnswer(Test test, User user, AnswerRequest answerRequest, Question question) {
        Answer answer = Answer
                .builder()
                .questionType(question.getQuestionType())
                .user(user)
                .question(question)
                .checked(false)
                .statement(answerRequest.getStatement())
                .dateOfSubmission(ZonedDateTime.now())
                .build();
        user.getTests().add(test);
        answerRepository.save(answer);
        testRepository.save(test);
        userRepository.save(user);
        log.info("Answer highlight the answer successfully saved !!!");
    }

    private void createPassRecordSayingStatement(Test test, User user, AnswerRequest answerRequest, Question question) {
        Answer answer = Answer
                .builder()
                .questionType(question.getQuestionType())
                .user(user)
                .question(question)
                .checked(false)
                .audioFile(answerRequest.getAudioUrl())
                .dateOfSubmission(ZonedDateTime.now())
                .build();
        user.getTests().add(test);
        answerRepository.save(answer);
        testRepository.save(test);
        userRepository.save(user);
        log.info("Answer successfully saved !!!");
    }
}
package com.example.bilingualb10.repository;

import com.example.bilingualb10.entity.Answer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface AnswerRepository extends JpaRepository<Answer, Long > {
    @Query("select new com.example.bilingualb10.entity.Answer(a.id, a.statement, a.audioFile, a.questionType, a.dateOfSubmission, a.count, a.checked, a.question, a.user, a.result) from Answer a where a.user.id = :userId and a.question.id = :questionId")
    Answer findAnswerByUserIdAndQuestionId(@Param("userId") Long userId,@Param("questionId")Long questionId);
}
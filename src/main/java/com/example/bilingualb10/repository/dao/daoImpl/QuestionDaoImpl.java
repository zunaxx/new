package com.example.bilingualb10.repository.dao.daoImpl;

import com.example.bilingualb10.dto.questionDto.QuestionResponse;
import com.example.bilingualb10.enums.QuestionType;
import com.example.bilingualb10.repository.dao.QuestionDao;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
@Slf4j
@RequiredArgsConstructor
public class QuestionDaoImpl implements QuestionDao {
    private final JdbcTemplate jdbcTemplate;

    @Override
    public QuestionResponse getQuestionById(Long questionId) {
        String sql = """
                SELECT 
                q.id,
                q.question_type,
                q.title,
                q.statement,
                q.correct_answer,
                q.duration,
                q.attempts,
                q.file_url,
                q.passage,
                q.enable
                FROM Questions q WHERE q.id = ?
                """;
        return jdbcTemplate.queryForObject(sql, (rs, rowNum) -> {
            return new QuestionResponse(
                    rs.getLong("id"),
                    QuestionType.valueOf(rs.getString("question_type")),
                    rs.getString("title"),
                    rs.getString("statement"),
                    rs.getString("correct_answer"),
                    rs.getInt("duration"),
                    rs.getInt("attempts"),
                    rs.getString("file_url"),
                    rs.getString("passage"),
                    rs.getBoolean("enable")
            );
        }, questionId);
    }
}
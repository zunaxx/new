package com.example.bilingualb10.repository.dao.daoImpl;

import com.example.bilingualb10.dto.answerDto.AnswerResponse;
import com.example.bilingualb10.repository.dao.AnswerDao;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
@Slf4j
@RequiredArgsConstructor
public class AnswerDaoImpl implements AnswerDao {
    private final JdbcTemplate jdbcTemplate;

    @Override
    public List<AnswerResponse> getAll() {
        String sql = """
                select a.id as answer_id, a.question_type, a.user_id, a.question_id, r.id as result_id
                from answers a left outer join results r on a.id = r.answer_id
                """;
        return jdbcTemplate.query(sql,((rs, rowNum) -> {
            AnswerResponse answerResponse = new AnswerResponse();
            answerResponse.setId(rs.getLong("answer_id"));
            answerResponse.setQuestionType(rs.getString("question_type"));
            answerResponse.setUserId(rs.getLong("user_id"));
            answerResponse.setQuestionId(rs.getLong("question_id"));
            answerResponse.setResultId(rs.getLong("result_id"));
            return answerResponse;
        }));
    }
}
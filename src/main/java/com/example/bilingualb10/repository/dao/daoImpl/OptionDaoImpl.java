package com.example.bilingualb10.repository.dao.daoImpl;

import com.example.bilingualb10.dto.optionDto.OptionResponse;
import com.example.bilingualb10.repository.dao.OptionDao;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import java.sql.Types;
import java.util.List;

@Repository
@Slf4j
@RequiredArgsConstructor
public class OptionDaoImpl implements OptionDao {
    private final JdbcTemplate jdbcTemplate;

    @Override
    public List<OptionResponse> getOptionsByQuestionId(Long questionId) {
        String sql = """
                    SELECT
                    o.id,
                    o.title,
                    o.audio_url,
                    o.is_true
                    FROM options o JOIN questions q ON o.question_id = q.id
                    WHERE o.question_id = ? ORDER BY o.id;
                """;
        Object[] args = {questionId};
        int[] argTypes = {Types.INTEGER};
        return jdbcTemplate.query(sql, args, argTypes, ((rs, rowNum) -> {
            OptionResponse option = new OptionResponse();
            option.setId(rs.getLong("id"));
            option.setTitle(rs.getString("title"));
            option.setAudioUrl(rs.getString("audio_url"));
            option.setIsTrue(rs.getBoolean("is_true"));
            return option;
        }));
    }
}
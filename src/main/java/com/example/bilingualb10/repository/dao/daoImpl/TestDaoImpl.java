package com.example.bilingualb10.repository.dao.daoImpl;

import com.example.bilingualb10.dto.questionDto.QuestionsResponse;
import com.example.bilingualb10.dto.testDto.PassOption;
import com.example.bilingualb10.dto.testDto.PassTestByIdResponse;
import com.example.bilingualb10.dto.testDto.TestResponse;
import com.example.bilingualb10.enums.QuestionType;
import com.example.bilingualb10.repository.dao.TestDao;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import java.sql.Types;
import java.util.*;
import java.util.List;

@Repository
@Slf4j
@RequiredArgsConstructor
public class TestDaoImpl implements TestDao {
    private final JdbcTemplate jdbcTemplate;

    @Override
    public List<TestResponse> getAllTests() {
        String sql = """
                SELECT
                t.id,
                t.title,
                t.description,
                t.enable,
                t.duration
                FROM  tests t
                ORDER BY t.id desc 
                """;
        return jdbcTemplate.query(sql, (rs, rowNum) -> {
            return new TestResponse(
                    rs.getLong("id"),
                    rs.getString("title"),
                    rs.getString("description"),
                    rs.getBoolean("enable"),
                    rs.getInt("duration")
            );
        });
    }

    @Override
    public List<QuestionsResponse> getAllQuestionsByTestId(Long testId) {
        String sql = """
                 SELECT
                 q.id,
                 (SELECT SUM(duration) FROM questions WHERE questions.test_id = ?) AS full_duration,
                 q.title, 
                 q.duration, 
                 q.enable,
                 q.question_type
                 FROM tests t 
                JOIN questions q ON t.id = q.test_id WHERE t.id = ?
                ORDER BY q.id desc 
                 """;
        Object [] args = {testId,testId};
        int [] argTypes = {Types.INTEGER,Types.INTEGER};
        return jdbcTemplate.query(sql,args,argTypes, (rs, rowNum) -> {
            QuestionsResponse response = new QuestionsResponse();
            response.setId(rs.getLong("id"));
            response.setTestDuration(rs.getInt("full_duration"));
            response.setTitle(rs.getString("title"));
            response.setDuration(rs.getInt("duration"));
            response.setEnable(rs.getBoolean("enable"));
            response.setQuestionType(QuestionType.valueOf(rs.getString("question_type")));
            response.setRow_number(rowNum);
            return response;
        });
    }

    @Override
    public List<PassTestByIdResponse> passTest(Long testId) {
        String sql = """
                select q.id,
                       q.file_url,
                       q.statement,
                       q.passage,
                       q.question_type,
                       q.attempts,
                       q.duration,
                       (select array_agg(o.id) as option_id),
                       (select array_agg(o.title) as option_title),
                       (select array_agg(o.audio_url) as option_url),
                       (select array_agg(o.is_true) as is_true)
                from tests t
                         join questions q on t.id = q.test_id
                         left join options o on q.id = o.question_id
                where q.test_id = ?
                group by q.id order by q.id;
                """;
        Object[] args = {testId};
        int[] argTypes = {Types.INTEGER};
        return jdbcTemplate.query(sql, args, argTypes, (rs, rowNum) -> {
            PassTestByIdResponse passTest = new PassTestByIdResponse();
            passTest.setId(rs.getLong("id"));
            passTest.setFileUrl(rs.getString("file_url"));
            passTest.setStatement(rs.getString("statement"));
            passTest.setPassage(rs.getString("passage"));
            passTest.setQuestionType(rs.getString("question_type"));
            passTest.setAttempts(rs.getInt("attempts"));
            passTest.setDuration(rs.getInt("duration"));
            List<Long> optionId = Arrays.asList((Long[]) rs.getArray("option_id").getArray());
            List<String> optionTitles = Arrays.asList((String[]) rs.getArray("option_title").getArray());
            List<String> optionUrls = Arrays.asList((String[]) rs.getArray("option_url").getArray());
            List<Boolean> optionIsTrue = Arrays.asList((Boolean[]) rs.getArray("is_true").getArray());
            List<PassOption> options = buildOptions(optionId, optionTitles, optionUrls, optionIsTrue);
            passTest.setOptionList(options);
            return passTest;
        });
    }

    private List<PassOption> buildOptions(List<Long>ids, List<String> titles, List<String> urls, List<Boolean> isTrue){
        List<PassOption> options = new ArrayList<>();
        for (int i = 0; i < titles.size(); i++) {
            PassOption option = new PassOption();
            option.setId(ids.get(i));
            option.setTitle(titles.get(i));
            option.setAudio_url(urls.get(i));
            option.setIsTrue(isTrue.get(i));
            options.add(option);
        }
        return options;
    }
}
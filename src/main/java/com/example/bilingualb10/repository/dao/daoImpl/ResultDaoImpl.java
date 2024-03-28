package com.example.bilingualb10.repository.dao.daoImpl;

import com.example.bilingualb10.dto.resultDto.QuestionCheckResultResponse;
import com.example.bilingualb10.dto.resultDto.QuestionResultResponse;
import com.example.bilingualb10.dto.resultDto.TestResultResponse;
import com.example.bilingualb10.enums.QuestionType;
import com.example.bilingualb10.repository.dao.ResultDao;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import java.sql.Types;
import java.util.List;

@Repository
@Slf4j
@RequiredArgsConstructor
public class ResultDaoImpl implements ResultDao {
    private final JdbcTemplate jdbcTemplate;

    private final RowMapper<TestResultResponse> testResultResponseRowMapper = (resultSet, rowMapper) -> {
        String userFullName = resultSet.getString("full_name");
        String dateOfSubmission = resultSet.getString("date_of_submission");
        String testName = resultSet.getString("title");
        Integer finalScore = resultSet.getInt("final_score");
        Long userId = resultSet.getLong("user_id");
        Long testId = resultSet.getLong("test_id");
        Boolean checked = getStatus(userId, testId);
        List<QuestionResultResponse> questionResultResponses = getAllQuestionResultsByTestIdAndUserId(testId,userId);
        return new TestResultResponse(userFullName, dateOfSubmission, testName, checked, finalScore, userId, testId, questionResultResponses);
    };
    private final RowMapper<QuestionResultResponse> questionResultResponseRowMapper = (resultSet, rowMapper) -> {
        Long id = resultSet.getLong("id");
        String questionTitle = resultSet.getString("title");
        Integer score = resultSet.getInt("score");
        Boolean checked = resultSet.getBoolean("checked");
        String QuestionTypeString = resultSet.getString("question_type");
        QuestionType questionType = (QuestionTypeString != null) ? QuestionType.valueOf(QuestionTypeString) : null;
        return new QuestionResultResponse(id, questionTitle, score, checked, questionType);
    };

    @Override
    public List<TestResultResponse> getAllTestResults() {
        String sql = """
                select concat(u.first_name,' ', u.last_name) as full_name,
                       (select max(a.date_of_submission) at time zone 'Asia/Bishkek') as date_of_submission,
                       t.title,
                       u.id,
                       t.id,
                       (select sum(r.score)) as final_score
                from results r
                         full join answers a on r.answer_id = a.id
                         join questions q on a.question_id = q.id
                         join users u on a.user_id = u.id
                         join tests t on q.test_id = t.id
                group by t.title, concat(u.first_name,' ', u.last_name), u.id, t.id 
                order by (select max(a.date_of_submission) at time zone 'Asia/Bishkek') desc;
                """;
        return jdbcTemplate.query(sql, ((rs, rowNum) -> {
            TestResultResponse testResult = new TestResultResponse();
            testResult.setUserFullName(rs.getString("full_name"));
            testResult.setDateOfSubmission(rs.getString("date_of_submission"));
            testResult.setTestName(rs.getString("title"));
            testResult.setChecked(getStatus(rs.getLong(4), rs.getLong(5)));
            testResult.setUserId(rs.getLong(4));
            testResult.setTestId(rs.getLong(5));
            testResult.setFinalScore(rs.getInt("final_score"));
            return testResult;
        }));
    }

    @Override
    public TestResultResponse getTestResultById(Long testId, Long userId) {
        String sql = """
                select
                           concat(u.first_name,' ', u.last_name) as full_name,
                           (select max(a.date_of_submission) at time zone 'Asia/Bishkek') as date_of_submission,
                           t.title,
                           (select sum(r.score) as final_score),
                           t.id as test_id,
                           u.id as user_id
                       from results r
                                full join answers a on r.answer_id = a.id
                                join questions q on a.question_id = q.id
                                join users u on a.user_id = u.id
                                join tests t on q.test_id = t.id
                       where t.id = ? and u.id = ?
                       group by t.title, concat(u.first_name,' ', u.last_name), t.id, u.id;
                       """;
        Object[] args = {testId, userId};
        int[] argTypes = {Types.INTEGER, Types.INTEGER};
        return jdbcTemplate.queryForObject(sql, args, argTypes, (rs, rowNum) -> {
            TestResultResponse testResultResponse = new TestResultResponse();
            testResultResponse.setUserFullName(rs.getString("full_name"));
            testResultResponse.setDateOfSubmission(rs.getString("date_of_submission"));
            testResultResponse.setTestName(rs.getString("title"));
            testResultResponse.setFinalScore(rs.getInt("final_score"));
            testResultResponse.setTestId(rs.getLong("test_id"));
            testResultResponse.setUserId(rs.getLong("user_id"));
            testResultResponse.setChecked(getStatus(userId, testId));
            List<QuestionResultResponse> questionResultResponses = getAllQuestionResultsByTestIdAndUserId(testId,userId);
            testResultResponse.setQuestionResultResponseList(questionResultResponses);
            return testResultResponse;
        });
    }

    @Override
    public List<QuestionResultResponse> getAllQuestionResultsByTestIdAndUserId(Long testId, Long userId) {
        String sql = """
                select q.id, q  .title, r.score, r.checked, q.question_type
                     from results r 
                    full join answers a on a.id = r.answer_id
                    join questions q on q.id = a.question_id
                where q.test_id = ? and a.user_id = ?
                order by r.checked
                """;
        List<QuestionResultResponse> questionResultResponses = null;
        questionResultResponses = jdbcTemplate.query(sql, questionResultResponseRowMapper, testId, userId);
        return questionResultResponses;
    }

    @Override
    public List<TestResultResponse> getAllTestResultsLikeUser(Long userId) {
        String sql = """
                    select
                        (select max(a.date_of_submission) at time zone 'Asia/Bishkek') as date_of_submission,
                        t.title,
                        r.checked,
                        (select sum(r.score)) as final_score,
                        t.id as test_id,
                        a.user_id
                    from results r
                            full outer join answers a on a.id = r.answer_id
                             join questions q on q.id = a.question_id
                             join tests t on q.test_id = t.id
                    where a.user_id = ?
                    group by t.title, r.checked, t.id, a.user_id order by r.checked desc 
                """;
        Object[] args = {userId};
        int[] argTypes = {Types.INTEGER};
        return jdbcTemplate.query(sql, args, argTypes, (rs, rowNum) -> {
            TestResultResponse testResult = new TestResultResponse();
            testResult.setDateOfSubmission(rs.getString("date_of_submission"));
            testResult.setTestName(rs.getString("title"));
            testResult.setChecked(rs.getBoolean("checked"));
            testResult.setFinalScore(rs.getInt("final_score"));
            testResult.setTestId(rs.getLong("test_id"));
            testResult.setUserId(rs.getLong("user_id"));
            return testResult;
        });
    }

    @Override
    public List<Long> getAllAnswersOfOneUserForOneTest(Long userId, Long testId) {
        String sql = """
                select a.id as answer_id
                    from answers a
                join questions q on a.question_id = q.id
                join tests t on q.test_id = t.id
                where a.user_id = ? and t.id = ?
                """;
        return jdbcTemplate.queryForList(sql, Long.class, userId, testId);
    }

    @Override
    public QuestionCheckResultResponse getCheckResult(Long userId, Long questionId) {
        String sql = """
                select concat(u.first_name,' ', u.last_name) as full_name,
                       t.title as test_title,
                       q.title as question_title,
                       q.duration,
                       q.question_type,
                       q.passage,
                       r.score
                    from results r
                full join answers a on a.id = r.answer_id
                join users u on a.user_id = u.id
                join questions q on a.question_id = q.id
                join tests t on q.test_id = t.id
                where u.id = ? and q.id = ?
                """;
        Object[] args = {userId, questionId};
        int[] argTypes = {Types.INTEGER, Types.INTEGER};
        return jdbcTemplate.queryForObject(sql, args, argTypes, (rs, rowNum) -> {
            QuestionCheckResultResponse questionResult = new QuestionCheckResultResponse();
            questionResult.setFullName(rs.getString("full_name"));
            questionResult.setTestTitle(rs.getString("test_title"));
            questionResult.setQuestionTitle(rs.getString("question_title"));
            questionResult.setDuration(rs.getInt("duration"));
            questionResult.setQuestionType(rs.getString("question_type"));
            questionResult.setPassage(rs.getString("passage"));
            questionResult.setScore(rs.getInt("score"));
            return questionResult;
        });
    }

    @Override
    public QuestionCheckResultResponse recordSayingStatementCheck(Long userId, Long questionId) {
        String sql = """
                select concat(u.first_name,' ',u.last_name) as full_name,
                       t.title as test_title,
                       q.title as question_title,
                       q.duration as duration,
                       q.question_type,
                       q.statement,
                       q.correct_answer,
                       a.audio_file
                from results r
                full join answers a on r.answer_id = a.id
                join questions q on a.question_id = q.id
                join users u on a.user_id = u.id
                join tests t on q.test_id = t.id
                where u.id = ? and q.id = ?
                """;
        Object[] args = {userId, questionId};
        int[] argTypes = {Types.INTEGER, Types.INTEGER};
        return jdbcTemplate.queryForObject(sql, args, argTypes, (rs, rowNum) -> {
            QuestionCheckResultResponse checkResult = new QuestionCheckResultResponse();
            checkResult.setFullName(rs.getString("full_name"));
            checkResult.setTestTitle(rs.getString("test_title"));
            checkResult.setQuestionTitle(rs.getString("question_title"));
            checkResult.setDuration(rs.getInt("duration"));
            checkResult.setQuestionType(rs.getString("question_type"));
            checkResult.setStatement(rs.getString("statement"));
            checkResult.setCorrectAnswer(rs.getString("correct_answer"));
            checkResult.setAudioFile(rs.getString("audio_file"));
            return checkResult;
        });
    }

    @Override
    public QuestionCheckResultResponse respondInNAndHighlightTheAnswer(Long userId, Long questionId) {
        String sql = """
                select concat(u.first_name,' ', u.last_name) as full_name,
                       t.title as test_title,
                       q.title as question_title,
                       q.duration,
                       q.question_type,
                      q.attempts,
                      q.statement as question_statement,
                      q.passage,
                      q.correct_answer,
                      a.statement as respond,
                      a.count 
                from results r
                         full join answers a on a.id = r.answer_id
                         join users u on a.user_id = u.id
                         join questions q on a.question_id = q.id
                         join tests t on q.test_id = t.id
                where u.id = ? and q.id = ?
                """;
        Object[] args = {userId, questionId};
        int[] argTypes = {Types.INTEGER, Types.INTEGER};
        return jdbcTemplate.queryForObject(sql, args, argTypes, (rs, rowNum) -> {
            QuestionCheckResultResponse checkResultResponse = new QuestionCheckResultResponse();
            checkResultResponse.setFullName(rs.getString("full_name"));
            checkResultResponse.setTestTitle(rs.getString("test_title"));
            checkResultResponse.setQuestionTitle(rs.getString("question_title"));
            checkResultResponse.setDuration(rs.getInt("duration"));
            checkResultResponse.setQuestionType(rs.getString("question_type"));
            checkResultResponse.setAttempts(rs.getInt("attempts"));
            checkResultResponse.setStatement(rs.getString("question_statement"));
            checkResultResponse.setPassage(rs.getString("passage"));
            checkResultResponse.setCorrectAnswer(rs.getString("correct_answer"));
            checkResultResponse.setRespond(rs.getString("respond"));
            checkResultResponse.setCount(rs.getInt("count"));
            return checkResultResponse;
        });
    }

    public QuestionCheckResultResponse getQuestionsResultWithFiles(Long userId, Long questionId) {
        String sql = """
                select concat(u.first_name,' ', u.last_name) as full_name,
                       t.title as test_title,
                       q.title as question_title,
                       q.duration,
                       q.question_type,
                       q.file_url,
                       q.correct_answer,
                       q.attempts,
                       a.statement
                from results r
                         full join answers a on a.id = r.answer_id
                         join users u on a.user_id = u.id
                         join questions q on a.question_id = q.id
                         join tests t on q.test_id = t.id
                where u.id = ? and q.id = ?
                """;
        Object[] args = {userId, questionId};
        int[] argTypes = {Types.INTEGER, Types.INTEGER};
        return jdbcTemplate.queryForObject(sql, args, argTypes, (rs, rowNum) -> {
            QuestionCheckResultResponse resultResponse = new QuestionCheckResultResponse();
            resultResponse.setFullName(rs.getString("full_name"));
            resultResponse.setTestTitle(rs.getString("test_title"));
            resultResponse.setQuestionTitle(rs.getString("question_title"));
            resultResponse.setDuration(rs.getInt("duration"));
            resultResponse.setQuestionType(rs.getString("question_type"));
            resultResponse.setCorrectAnswer(rs.getString("correct_answer"));
            resultResponse.setAudioFile(rs.getString("file_url"));
            resultResponse.setAttempts(rs.getInt("attempts"));
            resultResponse.setRespond(rs.getString("statement"));
            return resultResponse;
        });
    }

    @Override
    public List<Boolean> getAllStatusesByUserIdAndTestId(Long userId, Long testId) {
        String sql = """
                select a.checked
                from answers a
                         join questions q on a.question_id = q.id
                         join tests t on q.test_id = t.id
                where a.user_id = ? and t.id = ?
                """;
        return jdbcTemplate.queryForList(sql, Boolean.class, userId, testId);
    }

    private Boolean getStatus(Long userId, Long testId){
        List<Boolean> getAllStatuses = getAllStatusesByUserIdAndTestId(userId, testId);
        Boolean checked = true;
        for (Boolean status : getAllStatuses){
            if (!status) {
                checked = false;
                break;
            }
        }
        return checked;
    }
}
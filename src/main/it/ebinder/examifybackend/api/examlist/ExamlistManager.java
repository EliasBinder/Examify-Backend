package it.ebinder.examifybackend.api.examlist;

import com.google.gson.JsonObject;
import it.ebinder.examifybackend.api.authentication.AuthManager;
import it.ebinder.examifybackend.database.DatabaseManager;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.List;
import java.util.Map;
import java.util.UUID;

public class ExamlistManager {

    private static JdbcTemplate jdbcTemplate = DatabaseManager.jdbcTemplate;

    public static void getExamlist(String sessionid, JsonObject content){
        String email = AuthManager.getUsernameFromSession(sessionid);
        List<Map<String, Object>> rows = jdbcTemplate.queryForList("SELECT id, title FROM Exam WHERE owner = '" + email + "'");
        for (Map<String, Object> row : rows){
            JsonObject exam = new JsonObject();
            exam.addProperty("name", (String) row.get("title"));
            content.add((String) row.get("id"), exam);
        }
    }

    public static void createExam(String sessionid, String name, JsonObject content) {
        String email = AuthManager.getUsernameFromSession(sessionid);
        if (email == null)
            return;
        String id = UUID.randomUUID().toString();
        jdbcTemplate.update("INSERT INTO Exam(id, title, owner) VALUES('" + id + "', '" + name + "', '" + email + "')");
        //add one question
        jdbcTemplate.update("INSERT INTO Question(title, content, position, exam) VALUES('Question 1', '[{\"insert\":  \"\\n\"}]', '1', '" + id + "')");
        //add one answer type to that question
        jdbcTemplate.update("INSERT INTO AnswerType(position, questionposition, questionexam) VALUES('1', '1', '" + id + "')");
        jdbcTemplate.update("INSERT INTO Text(position, questionposition, questionexam, maxwords) VALUES('1', '1', '" + id + "', NULL)");
        content.addProperty("examID", id);
        content.addProperty("name", name);
    }

    public static void renameExam(String sessionid, String examid, String newname, JsonObject content){
        String email = AuthManager.getUsernameFromSession(sessionid);
        if (email == null)
            return;
        jdbcTemplate.update("UPDATE Exam SET title = '" + newname + "' WHERE id = '" + examid + "' AND owner = '" + email + "'");
        content.addProperty("newName", newname);
    }

    public static void deleteExam(String sessionid, String examid) {
        String email = AuthManager.getUsernameFromSession(sessionid);
        if (email == null)
            return;
        //delete exam
        jdbcTemplate.update("DELETE FROM Exam WHERE id = '" + examid + "' AND owner = '" + email + "'");
        //delete all questions
        jdbcTemplate.update("DELETE FROM Question WHERE exam = '" + examid + "'");
        //delete all answer types
        jdbcTemplate.update("DELETE FROM AnswerType WHERE questionexam = '" + examid + "'");
        //delete all answer types content
        jdbcTemplate.update("DELETE FROM Text WHERE questionexam = '" + examid + "'");
        jdbcTemplate.update("DELETE FROM MultipleChoiceOption WHERE questionexam = '" + examid + "'");
        jdbcTemplate.update("DELETE FROM FileUpload WHERE questionexam = '" + examid + "'");
        jdbcTemplate.update("DELETE FROM Cloze WHERE questionexam = '" + examid + "'");
        jdbcTemplate.update("DELETE FROM AudioRecording WHERE questionexam = '" + examid + "'");
        //delete all attachments
        jdbcTemplate.update("DELETE FROM FileAttachment WHERE questionexam = '" + examid + "'");
        //do not delete holdings to keep submissions of students
    }
}

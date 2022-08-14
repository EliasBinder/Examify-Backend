package it.ebinder.examifybackend.api.examlist;

import com.google.gson.JsonObject;
import it.ebinder.examifybackend.api.authentication.AuthManager;
import it.ebinder.examifybackend.database.DatabaseManager;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.List;
import java.util.Map;

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

}

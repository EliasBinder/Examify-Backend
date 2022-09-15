package it.ebinder.examifybackend.api.holdinglist;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import it.ebinder.examifybackend.api.authentication.AuthManager;

import java.sql.Timestamp;
import java.util.*;

import static it.ebinder.examifybackend.database.DatabaseManager.jdbcTemplate;

public class HoldinglistManager {

    public static void getHoldinglist(String sessionid, JsonObject content){
        String email = AuthManager.getUsernameFromSession(sessionid);
        if (email == null){
            content.addProperty("error", "Invalid session");
            return;
        }

        HashMap<String, JsonObject> exams = new HashMap<>();

        jdbcTemplate.queryForList("SELECT * FROM Holdings WHERE teacher = '" + email + "'").forEach(row -> {
            String examid = (String) row.get("exam");
            JsonObject exam = exams.computeIfAbsent(examid, (k) -> {
                JsonObject newExam = new JsonObject();
                String examName = jdbcTemplate.queryForObject("SELECT title FROM Exam WHERE id = '" + examid + "'", String.class);
                newExam.addProperty("name", examName);
                JsonArray holdings = new JsonArray();
                newExam.add("holdings", holdings);
                return newExam;
            });
            JsonArray holdings = exam.getAsJsonArray("holdings");
            JsonObject holding = new JsonObject();
            holding.addProperty("ref", (String) row.get("referenceid"));
            holding.addProperty("start", ((Timestamp) row.get("start")).getTime());
            holdings.add(holding);
        });

        exams.entrySet().forEach(entry -> content.add(entry.getKey(), entry.getValue()));
    }

}

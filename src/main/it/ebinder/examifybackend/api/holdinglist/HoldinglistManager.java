package it.ebinder.examifybackend.api.holdinglist;

import com.google.gson.JsonObject;
import it.ebinder.examifybackend.api.authentication.AuthManager;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import static it.ebinder.examifybackend.database.DatabaseManager.jdbcTemplate;

public class HoldinglistManager {

    public void getHoldinglist(String sessionid, JsonObject content){
        String email = AuthManager.getUsernameFromSession(sessionid);
        //List<Map<String, Object>> rows = jdbcTemplate.queryForList("SELECT id, title FROM Holdings WHERE owner = '" + email + "'");
        List<Map<String, Object>> rows = new LinkedList<>();
        rows.add(new HashMap<>(){{
            put("name", "IDB Exam");
            //TODO: think about database schema
        }});
        for (Map<String, Object> row : rows){
            JsonObject exam = new JsonObject();
            exam.addProperty("name", (String) row.get("title"));
            content.add((String) row.get("id"), exam);
        }
    }

}

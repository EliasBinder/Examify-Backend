package it.ebinder.examifybackend.api.holdinglist;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import it.ebinder.examifybackend.api.authentication.AuthManager;

import java.util.*;

import static it.ebinder.examifybackend.database.DatabaseManager.jdbcTemplate;

public class HoldinglistManager {

    public static void getHoldinglist(String sessionid, JsonObject content){
        JsonObject exam = new JsonObject();
        exam.addProperty("name", "IDB Exam");
        JsonArray holdings = new JsonArray();
        exam.add("holdings", holdings);

        JsonObject holding1 = new JsonObject();
        holdings.add(holding1);
        Calendar calendar = Calendar.getInstance();
        calendar.set(2022, 5, 14, 9, 35);
        holding1.addProperty("start", calendar.getTimeInMillis());
        holding1.addProperty("ref", "abcd-wxyz");

        JsonObject holding2 = new JsonObject();
        holdings.add(holding2);
        calendar.set(2022, 5, 16, 13, 0);
        holding2.addProperty("start", calendar.getTimeInMillis());
        holding2.addProperty("ref", "efgh-ijkl");

        JsonObject exam2 = new JsonObject();
        exam2.addProperty("name", "DaStAl Exam");
        JsonArray holdings2 = new JsonArray();
        exam2.add("holdings", holdings2);

        JsonObject holding3 = new JsonObject();
        holdings2.add(holding3);
        calendar.set(2022, 10, 2, 17, 0);
        holding3.addProperty("start", calendar.getTimeInMillis());
        holding3.addProperty("ref", "ifsa-somd");

        content.add("1", exam);
        content.add("2", exam2);
    }

}

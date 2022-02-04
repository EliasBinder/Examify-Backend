package it.ebinder.examifybackend.api.authentication;

import com.google.gson.JsonObject;
import it.ebinder.examifybackend.database.DatabaseManager;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.UUID;

public class AuthManager {

    private static JdbcTemplate jdbcTemplate = DatabaseManager.jdbcTemplate;

    public static String getUsernameFromSession(String sessionid){
        return jdbcTemplate.query("SELECT email FROM Teacher WHERE sessionid = '" + sessionid + "'", rs -> {
            if (rs.next())
                return rs.getString(1);
            else
                return null;
        });
    }

    public static boolean login(JsonObject content, String submittedUsername, String submittedPassword){
        String storedHash = jdbcTemplate.query("SELECT password FROM Teacher WHERE email = '" + submittedUsername + "'", rs -> {
            if (rs.next())
                return rs.getString(1);
            else
                return null;
        });
        if (storedHash == null)
            return false;
        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
        if (bCryptPasswordEncoder.matches(submittedPassword, storedHash)){
            String sessionid = UUID.randomUUID().toString();
            jdbcTemplate.update("UPDATE Teacher SET sessionid = '" + sessionid + "' WHERE email = '" + submittedUsername + "'");
            content.addProperty("sessionid", sessionid);
            return true;
        }
        return false;
    }

    public static boolean register(String submittedUsername, String submittedPassword, String submittedFirstname, String submittedLastname){
        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
        String hashedPass = bCryptPasswordEncoder.encode(submittedPassword);
        return jdbcTemplate.update("INSERT INTO Teacher(email, password, firstname, lastname) VALUES('" + submittedUsername + "', '" + hashedPass + "', '" + submittedFirstname + "', '" + submittedLastname + "')") == 1;
    }

}

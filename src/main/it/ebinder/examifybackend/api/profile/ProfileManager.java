package it.ebinder.examifybackend.api.profile;

import com.google.gson.JsonObject;
import it.ebinder.examifybackend.api.authentication.AuthManager;
import it.ebinder.examifybackend.database.DatabaseManager;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.web.bind.annotation.CookieValue;

import javax.servlet.http.HttpSession;

public class ProfileManager {

    private static JdbcTemplate jdbcTemplate = DatabaseManager.jdbcTemplate;

    public static void getProfilePackage(String sessionid, JsonObject target){
        String email = AuthManager.getUsernameFromSession(sessionid);
        SqlRowSet userRow = jdbcTemplate.queryForRowSet("SELECT firstname, lastname, profileimage FROM Teacher WHERE email='" + email + "'");
        userRow.next();
        target.addProperty("firstname", userRow.getString(1));
        target.addProperty("lastname", userRow.getString(2));
        target.addProperty("email", email);
        if (userRow.getString(3) != null) {
            target.addProperty("profileImg", userRow.getString(3));
        }else{
            target.addProperty("profileImg", "none");
        }
    }

    public static void deleteProfileImage(String sessionid){
        String email = AuthManager.getUsernameFromSession(sessionid);
        jdbcTemplate.update("UPDATE Teacher SET profileimage = null WHERE email = '" + email + "'");
    }

    public static void setProfileImage(String sessionid, String newImage){
        String email = AuthManager.getUsernameFromSession(sessionid);
        jdbcTemplate.update("UPDATE Teacher SET profileimage = '" + newImage + "' WHERE email = '" + email + "'");
    }

    public static void setProfileData(String sessionid, String newFirstname, String newLastname){
        String email = AuthManager.getUsernameFromSession(sessionid);
        jdbcTemplate.update("UPDATE Teacher SET firstname = '" + newFirstname + "', lastname = '" + newLastname + "' WHERE email = '" + email + "'");
    }

}

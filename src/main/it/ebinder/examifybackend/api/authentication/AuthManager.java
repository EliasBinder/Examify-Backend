package it.ebinder.examifybackend.api.authentication;

import it.ebinder.examifybackend.database.DatabaseManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import javax.servlet.http.HttpSession;
import java.sql.ResultSet;
import java.sql.SQLException;

public class AuthManager {

    private static JdbcTemplate jdbcTemplate = DatabaseManager.jdbcTemplate;

    public static String getUsernameFromSession(HttpSession session){
        return jdbcTemplate.query("SELECT email FROM Teacher WHERE sessionid = '" + session.getId() + "'", rs -> {
            if (rs.next())
                return rs.getString(1);
            else
                return null;
        });
    }

    public static boolean login(HttpSession session, String submittedUsername, String submittedPassword){
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
            jdbcTemplate.update("UPDATE Teacher SET sessionid = '" + session.getId() + "' WHERE email = '" + submittedUsername + "'");
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

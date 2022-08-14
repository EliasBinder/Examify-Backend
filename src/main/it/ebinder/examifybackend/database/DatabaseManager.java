package it.ebinder.examifybackend.database;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

@Service
public class DatabaseManager {

    public static JdbcTemplate jdbcTemplate;

    @Autowired
    public DatabaseManager(JdbcTemplate template){
        this.jdbcTemplate = template;
        createTables();
    }

    private void createTables(){
        //Teacher Table
        jdbcTemplate.execute("CREATE TABLE IF NOT EXISTS Teacher(" +
                "  email varchar(450) PRIMARY KEY," +
                "  password varchar(450) NOT NULL," +
                "  sessionid varchar(36) DEFAULT NULL," +
                "  firstname varchar(450) DEFAULT ''," +
                "  lastname varchar(450) DEFAULT ''," +
                "  created timestamp DEFAULT now()," +
                "  profileimage text DEFAULT NULL" +
                ")");

        //Exam Table
        jdbcTemplate.execute("CREATE TABLE IF NOT EXISTS Exam(" +
                "  id varchar(36) PRIMARY KEY," +
                "  title varchar(450) NOT NULL," +
                "  owner varchar(450) NOT NULL" + //TODO foreign key
                ")");

        //Question Table
        /*
        jdbcTemplate.execute("CREATE TABLE IF NOT EXISTS Question(" +
                "  id int," +
                "  pos int," +
                "  title varchar(450) NOT NULL," +
                "  content text DEFAULT \"[{\\\"insert\\\": \\\"\\n\\\"}]\"" +
                ")");

        //Answer Table
        jdbcTemplate.execute("CREATE TABLE IF NOT EXISTS Answer(" +
                "  id int," +
                "  pos int" +
                ")");

         */
    }

}

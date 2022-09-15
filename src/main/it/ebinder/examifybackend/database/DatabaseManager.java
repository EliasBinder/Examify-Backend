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
        System.out.println("Creating tables...");
        jdbcTemplate.execute("CREATE TABLE IF NOT EXISTS Teacher(" +
                "    email varchar(255) PRIMARY KEY," +
                "    password varchar(255) NOT NULL," +
                "    sessionid char(36) DEFAULT NULL," +
                "    firstname varchar(255) DEFAULT '' NOT NULL," +
                "    lastname varchar(255) DEFAULT '' NOT NULL," +
                "    created timestamp DEFAULT now() NOT NULL," +
                "    profileimage text DEFAULT NULL" +
                ");" +
                "" +
                "CREATE TABLE IF NOT EXISTS Exam(" +
                "    ID char(36) PRIMARY KEY," +
                "    title varchar(255) NOT NULL," +
                "    owner varchar(255) NOT NULL," +
                "    FOREIGN KEY (owner) REFERENCES Teacher(email)" +
                ");" +
                "" +
                "CREATE TABLE IF NOT EXISTS SharedWith(" +
                "    teacher varchar(255) NOT NULL," +
                "    exam char(36) NOT NULL," +
                "    FOREIGN KEY (teacher) REFERENCES Teacher(email)," +
                "    FOREIGN KEY (exam) REFERENCES Exam(ID)" +
                ");" +
                "" +
                "CREATE TABLE IF NOT EXISTS Question(" +
                "    title varchar(255) NOT NULL," +
                "    content text NOT NULL," +
                "    position int DEFAULT 0 NOT NULL," +
                "    exam char(36) NOT NULL," +
                "    CONSTRAINT PK_position_exam PRIMARY KEY (position, exam)," +
                "    FOREIGN KEY (exam) REFERENCES Exam(ID)" +
                ");" +
                "" +
                "CREATE TABLE IF NOT EXISTS Holding(" +
                "    referenceID char(9) PRIMARY KEY," +
                "    joinTime timestamp NOT NULL," +
                "    startTime timestamp NOT NULL," +
                "    endTime timestamp NOT NULL," +
                "    teacher varchar(255) NOT NULL," +
                "    exam char(36) NOT NULL," +
                "    FOREIGN KEY (teacher) REFERENCES Teacher(email)," +
                "    FOREIGN KEY (exam) REFERENCES Exam(ID)" +
                ");" +
                "" +
                "CREATE TABLE IF NOT EXISTS Participation(" +
                "    studentID int DEFAULT 0 NOT NULL," +
                "    name varchar(255) NOT NULL," +
                "    joinTime timestamp DEFAULT now() NOT NULL," +
                "    holding char(9) NOT NULL," +
                "    CONSTRAINT PK_studentID_holding PRIMARY KEY (studentID, holding)," +
                "    FOREIGN KEY (holding) REFERENCES Holding(referenceID)" +
                ");" +
                "" +
                "CREATE TABLE IF NOT EXISTS AnswerType(" +
                "    position int DEFAULT 0 NOT NULL," +
                "    questionPosition int DEFAULT 0 NOT NULL," +
                "    questionExam char(36) NOT NULL," +
                "    CONSTRAINT PK_position_questionPos_questionExam PRIMARY KEY (position, questionPosition, questionExam)," +
                "    FOREIGN KEY (questionPosition, questionExam) REFERENCES Question(position, exam)" +
                ");" +
                "" +
                "CREATE TABLE IF NOT EXISTS Answer(" +
                "    textOrJson text DEFAULT NULL," +
                "    studentID int DEFAULT 0 NOT NULL," +
                "    holding char(9) NOT NULL," +
                "    position int DEFAULT 0 NOT NULL," +
                "    questionPosition int DEFAULT 0 NOT NULL," +
                "    questionExam char(36) NOT NULL," +
                "    CONSTRAINT PK_studentid_holding_questionPos_questionExam PRIMARY KEY (studentID, holding, position, questionPosition, questionExam)," +
                "    FOREIGN KEY (studentID, holding) REFERENCES Participation(studentID, holding)," +
                "    FOREIGN KEY (position, questionPosition, questionExam) REFERENCES AnswerType(position, questionPosition, questionExam)" +
                ");" +
                "" +
                "CREATE TABLE IF NOT EXISTS Text(" +
                "    position int DEFAULT 0 NOT NULL," +
                "    questionPosition int DEFAULT 0 NOT NULL," +
                "    questionExam char(36) NOT NULL," +
                "    maxWords int DEFAULT NULL CHECK ( maxWords > 0 )," +
                "    CONSTRAINT PK_Text_position_questionPos_questionExam PRIMARY KEY (position, questionPosition, questionExam)," +
                "    FOREIGN KEY (position, questionPosition, questionExam) REFERENCES AnswerType(position, questionPosition, questionExam)" +
                ");" +
                "" +
                "CREATE TABLE IF NOT EXISTS Cloze(" +
                "    position int DEFAULT 0 NOT NULL," +
                "    questionPosition int DEFAULT 0 NOT NULL," +
                "    questionExam char(36) NOT NULL," +
                "    pattern text NOT NULL," +
                "    solution text NOT NULL," +
                "    CONSTRAINT PK_Cloze_position_questionPos_questionExam PRIMARY KEY (position, questionPosition, questionExam)," +
                "    FOREIGN KEY (position, questionPosition, questionExam) REFERENCES AnswerType(position, questionPosition, questionExam)" +
                ");" +
                "" +
                "CREATE TABLE IF NOT EXISTS AudioRecording(" +
                "    position int DEFAULT 0 NOT NULL," +
                "    questionPosition int DEFAULT 0 NOT NULL," +
                "    questionExam char(36) NOT NULL," +
                "    maxLength int DEFAULT NULL CHECK ( maxLength > 0 )," +
                "    CONSTRAINT PK_AudioRecording_position_questionPos_questionExam PRIMARY KEY (position, questionPosition, questionExam)," +
                "    FOREIGN KEY (position, questionPosition, questionExam) REFERENCES AnswerType(position, questionPosition, questionExam)" +
                ");" +
                "" +
                "CREATE TABLE IF NOT EXISTS FileUpload(" +
                "    position int DEFAULT 0 NOT NULL," +
                "    questionPosition int DEFAULT 0 NOT NULL," +
                "    questionExam char(36) NOT NULL," +
                "    maxNumFiles int DEFAULT NULL CHECK ( maxNumFiles > 0 )," +
                "    maxFileSize bigint DEFAULT NULL CHECK ( maxFileSize > 0 )," +
                "    filter varchar(255) DEFAULT NULL," +
                "    CONSTRAINT PK_FileUpload_position_questionPos_questionExam PRIMARY KEY (position, questionPosition, questionExam)," +
                "    FOREIGN KEY (position, questionPosition, questionExam) REFERENCES AnswerType(position, questionPosition, questionExam)" +
                ");" +
                "" +
                "CREATE TABLE IF NOT EXISTS MultipleChoiceOption(" +
                "    position int DEFAULT 0 NOT NULL," +
                "    questionPosition int DEFAULT 0 NOT NULL," +
                "    questionExam char(36) NOT NULL," +
                "    option varchar(255) NOT NULL," +
                "    solution bool NOT NULL," +
                "    id int NOT NULL," +
                "    CONSTRAINT PK_MultipleChoiceOption_id_position_questionPos_questionExam PRIMARY KEY (id, position, questionPosition, questionExam)," +
                "    FOREIGN KEY (position, questionPosition, questionExam) REFERENCES AnswerType(position, questionPosition, questionExam)" +
                ");" +
                "" +
                "CREATE TABLE IF NOT EXISTS FileAnswer(" +
                "    uuid char(36) PRIMARY KEY," +
                "    name varchar(255) NOT NULL," +
                "    mimeType varchar(255) DEFAULT '*/*' NOT NULL," +
                "    created timestamp DEFAULT now() NOT NULL," +
                "    questionPosition int NOT NULL," +
                "    questionExam char(36) NOT NULL," +
                "    studentID int NOT NULL," +
                "    holding char(9) NOT NULL," +
                "    position int NOT NULL," +
                "    FOREIGN KEY (studentID, holding, position, questionPosition, questionExam) REFERENCES Answer(studentID, holding, position, questionPosition, questionExam)" +
                ");" +
                "" +
                "CREATE TABLE IF NOT EXISTS FileAttachment(" +
                "    uuid char(36) PRIMARY KEY," +
                "    name varchar(255) NOT NULL," +
                "    mimeType varchar(255) DEFAULT '*/*' NOT NULL," +
                "    created timestamp DEFAULT now() NOT NULL," +
                "    questionPosition int NOT NULL," +
                "    questionExam char(36) NOT NULL," +
                "    FOREIGN KEY (questionPosition, questionExam) REFERENCES Question(position, exam)" +
                ");");
    }

}

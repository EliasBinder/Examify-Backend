package it.ebinder.examifybackend;

import it.ebinder.examifybackend.database.DatabaseManager;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ExamifyBackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(ExamifyBackendApplication.class, args);
    }

}

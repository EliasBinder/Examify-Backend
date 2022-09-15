package it.ebinder.examifybackend.api.exam;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import it.ebinder.examifybackend.api.authentication.AuthManager;
import it.ebinder.examifybackend.database.DatabaseManager;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.Map;

public class ExamManager {

    private static JdbcTemplate jdbcTemplate = DatabaseManager.jdbcTemplate;

    public static void getPackage(String sessionID, String examID, final JsonObject content){
        ExamAccessType accessType = getExamAccessType(examID, sessionID);
        if (accessType == ExamAccessType.NONE)
            return;

        content.addProperty("title", jdbcTemplate.queryForObject("SELECT title FROM Exam WHERE id = '" + examID + "'", String.class));
        if (accessType == ExamAccessType.READ_WRITE)
            content.addProperty("editable", true);
        else
            content.addProperty("editable", false);
        JsonObject questions = new JsonObject();
        content.add("questions", questions);

        jdbcTemplate.queryForList("SELECT * FROM Question WHERE exam = '" + examID + "' ORDER BY position").forEach(row -> {
            JsonObject question = new JsonObject();
            int questionPosition = (int) row.get("position");
            questions.add(String.valueOf(questionPosition), question);

            question.addProperty("title", (String) row.get("title"));
            question.addProperty("pos", questionPosition);
            question.add("content", new Gson().fromJson((String) row.get("content"), JsonArray.class));

            //get question attachments
            JsonObject attachments = new JsonObject();
            question.add("attachments", attachments);
            //TODO: add attachments

            //get answer types
            JsonObject answerTypes = new JsonObject();
            question.add("answer_types", answerTypes);

            //get all text answer types
            jdbcTemplate.queryForList("SELECT * FROM Text WHERE questionposition = '" + questionPosition + "' AND questionexam = '" + examID + "' ORDER BY position").forEach(row2 -> {
                JsonObject answerType = new JsonObject();
                int answerTypePosition = (int) row2.get("position");
                answerTypes.add(String.valueOf(answerTypePosition-1), answerType);

                answerType.addProperty("type", 0);
                answerType.addProperty("pos", answerTypePosition);
                JsonObject contentAnswerType = new JsonObject();
                answerType.add("content", contentAnswerType);
                contentAnswerType.addProperty("wordlimit", (int) row2.getOrDefault("max_words", -1));
            });

            //get all cloze answer types
            jdbcTemplate.queryForList("SELECT * FROM Cloze WHERE questionposition = '" + questionPosition + "' AND questionexam = '" + examID + "' ORDER BY position").forEach(row2 -> {
                JsonObject answerType = new JsonObject();
                int answerTypePosition = (int) row2.get("position");
                answerTypes.add(String.valueOf(answerTypePosition-1), answerType);

                answerType.addProperty("type", 1);
                answerType.addProperty("pos", answerTypePosition);
                JsonObject contentAnswerType = new JsonObject();
                answerType.add("content", contentAnswerType);
                contentAnswerType.add("pattern", new Gson().fromJson((String) row2.get("pattern"), JsonObject.class));
                contentAnswerType.add("solution", new Gson().fromJson((String) row2.get("solution"), JsonArray.class));
            });

            //get all multiple choice answer types
            jdbcTemplate.queryForList("SELECT * FROM MultipleChoiceOption WHERE questionposition = '" + questionPosition + "' AND questionexam = '" + examID + "' ORDER BY position").forEach(row2 -> {
                int position = (int) row2.get("position");
                JsonObject answerType;

                if (answerTypes.has(String.valueOf(position)))
                    answerType = answerTypes.get(String.valueOf(position)).getAsJsonObject();
                else {
                    answerType = new JsonObject();
                    answerType.addProperty("type", 2);
                    answerType.addProperty("pos", position);
                    JsonObject contentAnswerType = new JsonObject();
                    answerType.add("content", contentAnswerType);
                    contentAnswerType.add("options", new JsonArray());
                    answerTypes.add(String.valueOf(position), answerType);
                }

                JsonArray contentAnswerType = answerType.get("content").getAsJsonObject().get("options").getAsJsonArray();
                JsonObject option = new JsonObject();
                contentAnswerType.add(option);
                option.addProperty("value", (String) row2.get("option"));
                option.addProperty("isCorrect", (boolean) row2.get("solution"));
            });

            //get all audio recording answer types
            jdbcTemplate.queryForList("SELECT * FROM AudioRecording WHERE questionposition = '" + questionPosition + "' AND questionexam = '" + examID + "' ORDER BY position").forEach(row2 -> {
                JsonObject answerType = new JsonObject();
                int answerTypePosition = (int) row2.get("position");
                answerTypes.add(String.valueOf(answerTypePosition-1), answerType);

                answerType.addProperty("type", 3);
                answerType.addProperty("pos", answerTypePosition);
                JsonObject contentAnswerType = new JsonObject();
                answerType.add("content", contentAnswerType);
                contentAnswerType.addProperty("durationLimit", (int) row2.getOrDefault("maxlength", -1));
            });

            //get all file upload answer types
            jdbcTemplate.queryForList("SELECT * FROM FileUpload WHERE questionposition = '" + questionPosition + "' AND questionexam = '" + examID + "' ORDER BY position").forEach(row2 -> {
                JsonObject answerType = new JsonObject();
                int answerTypePosition = (int) row2.get("position");
                answerTypes.add(String.valueOf(answerTypePosition-1), answerType);

                answerType.addProperty("type", 4);
                answerType.addProperty("pos", answerTypePosition);
                JsonObject contentAnswerType = new JsonObject();
                answerType.add("content", contentAnswerType);
                contentAnswerType.addProperty("allowedTypes", (String) row2.getOrDefault("filter", ""));
                contentAnswerType.addProperty("sizelimit", (int) row2.getOrDefault("maxfilesize", -1));
                contentAnswerType.addProperty("numlimit", (int) row2.getOrDefault("maxnumfiles", -1));
            });
        });
    }

    public static ExamAccessType getExamAccessType(String examID, String sessionID){
        String username = AuthManager.getUsernameFromSession(sessionID);
        if (username == null)
            return ExamAccessType.NONE;

        //is owner of exam
        String ownerOfExam = jdbcTemplate.queryForObject("SELECT owner FROM Exam WHERE id = '" + examID + "'", String.class);
        if (ownerOfExam.equals(username))
            return ExamAccessType.READ_WRITE;

        //is shared with user
        jdbcTemplate.query("SELECT * FROM SharedWith WHERE exam = '" + examID + "' AND teacher = '" + username + "'", row -> ExamAccessType.READ_ONLY);

        return ExamAccessType.NONE;
    }

    public static boolean deleteQuestion(String examid, int position){
        //count questions in exam with id examid
        int count = jdbcTemplate.queryForObject("SELECT COUNT(*) FROM Question WHERE exam = '" + examid + "'", Integer.class);
        if (count == 1) //minimum cardinality of 1
            return false;

        jdbcTemplate.update("DELETE FROM Question WHERE exam = '" + examid + "' AND position = '" + position + "'");
        //delete all answer types
        jdbcTemplate.update("DELETE FROM AnswerType WHERE questionexam = '" + examid + "' AND questionposition = '" + position + "'");
        //delete all answer types content
        jdbcTemplate.update("DELETE FROM Text WHERE questionexam = '" + examid + "' AND questionposition = '" + position + "'");
        jdbcTemplate.update("DELETE FROM MultipleChoiceOption WHERE questionexam = '" + examid + "' AND questionposition = '" + position + "'");
        jdbcTemplate.update("DELETE FROM FileUpload WHERE questionexam = '" + examid + "' AND questionposition = '" + position + "'");
        jdbcTemplate.update("DELETE FROM Cloze WHERE questionexam = '" + examid + "' AND questionposition = '" + position + "'");
        jdbcTemplate.update("DELETE FROM AudioRecording WHERE questionexam = '" + examid + "' AND questionposition = '" + position + "'");
        //delete all attachments
        jdbcTemplate.update("DELETE FROM FileAttachment WHERE questionexam = '" + examid + "' AND questionposition = '" + position + "'");
        return true;
    }

    /**
     * Update question title or text
     * @param sessionID
     * @param examID
     * @param content
     */
    public static void update(String sessionID, String examID, JsonObject content){
        ExamAccessType accessType = getExamAccessType(examID, sessionID);
        if (accessType == ExamAccessType.NONE)
            return;

        if (accessType == ExamAccessType.READ_ONLY)
            return;

        //update questions
        for (Map.Entry<String, JsonElement> entry : content.entrySet()) {
            JsonObject question = entry.getValue().getAsJsonObject();

            int questionPosition = Integer.parseInt(entry.getKey());

            if (question.has("title")){
                String title = question.get("title").getAsString();
                jdbcTemplate.update("UPDATE Question SET title = '" + title + "' WHERE exam = '" + examID + "' AND position = '" + questionPosition + "'");
            }
            if (question.has("content")){
                String contentQuestion = question.get("content").toString();
                jdbcTemplate.update("UPDATE Question SET content = '" + contentQuestion + "' WHERE exam = '" + examID + "' AND position = '" + questionPosition + "'");
            }
        }

    }



}

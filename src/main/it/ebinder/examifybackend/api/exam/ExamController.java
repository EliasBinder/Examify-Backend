package it.ebinder.examifybackend.api.exam;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import it.ebinder.examifybackend.messages.Error;
import it.ebinder.examifybackend.messages.Response;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;
import java.util.UUID;

@RestController
public class ExamController {

    @GetMapping (value = "/api/exam/{examID}/getpackage")
    public Response getExamPackage(
            @PathVariable String examID,
            @CookieValue(value = "JSESSIONID") String sessionid
    ){
        Response response = new Response();
        ExamManager.getPackage(sessionid, examID, response.content);
        return response;
    }

    @DeleteMapping (value = "/api/exam/{examID}/questions/{questionID}")
    public Response deleteQuestion(
            @PathVariable String examID,
            @PathVariable int questionPosition,
            @CookieValue(value = "JSESSIONID") String sessionid
    ){
        Response response = new Response();
        ExamAccessType accessType = ExamManager.getExamAccessType(sessionid, examID);
        if (accessType == ExamAccessType.READ_WRITE)
            ExamManager.deleteQuestion(examID, questionPosition);
        else
            return new Error(1, "You don't have access to this exam!");
        return response;
    }

    @PatchMapping (value = "/api/exam/{examID}/setquestionsposition")
    public Response moveQuestion(
            @PathVariable String examID,
            @RequestBody JsonObject bodyJson,
            @CookieValue(value = "JSESSIONID") String sessionid
    ){
        Response response = new Response();

        for (Map.Entry<String, JsonElement> entry : bodyJson.entrySet()){
            String qid = entry.getKey();
            int pos = entry.getValue().getAsInt();
            //TODO: reset positions
        }

        return response;
    }

    @PutMapping (value = "/api/exam/{examID}/questions/{questionID}/addattachment")
    public Response putQuestionAttachment(
            @PathVariable String examID,
            @PathVariable int questionID,
            @RequestParam("file") MultipartFile attachment,
            @CookieValue(value = "JSESSIONID") String sessionid
    ){
        Response response = new Response();
        System.out.println("Uploading file: " + attachment.getOriginalFilename());
        response.content.addProperty("id", UUID.randomUUID().toString());
        //TODO: save file
        return response;
    }

    @DeleteMapping (value = "/api/exam/{examID}/questions/{questionID}/deleteattachment/{attachmentid}")
    public Response deleteQuestionAttachment(
            @PathVariable String examID,
            @PathVariable int questionID,
            @PathVariable String attachmentid,
            @CookieValue(value = "JSESSIONID") String sessionid
    ){
        Response response = new Response();
        //TODO
        return response;
    }

    @PatchMapping (value = "/api/exam/{examID}/questions/{questionID}/setanswertypesposition")
    public Response moveAnswerTypes(
            @PathVariable String examID,
            @PathVariable int questionID,
            @RequestBody JsonObject bodyJson,
            @CookieValue(value = "JSESSIONID") String sessionid
    ){
        Response response = new Response();

        for (Map.Entry<String, JsonElement> entry : bodyJson.entrySet()){
            String aid = entry.getKey();
            int pos = entry.getValue().getAsInt();
            //TODO: reset positions
        }

        return response;
    }

    @PatchMapping (value = "/api/exam/{examID}/questions/{questionID}/answertypes/{answerTypeID}/setproperties")
    public Response setAnswerTypeProperties(
            @PathVariable String examID,
            @PathVariable int questionID,
            @PathVariable int answerTypeID,
            @RequestBody JsonObject bodyJson,
            @CookieValue(value = "JSESSIONID") String sessionid
    ){
        Response response = new Response();
        //TODO
        return response;
    }


    @PatchMapping (value = "/api/exam/{examID}/updatetext")
    public Response updateText(
            @PathVariable String examID,
            @RequestBody JsonObject bodyJson,
            @CookieValue(value = "JSESSIONID") String sessionid
    ){
        Response response = new Response();
        System.out.println("Uodating text:");
        System.out.println(new Gson().toJson(bodyJson));
        ExamManager.update(sessionid, examID, bodyJson);
        return response;
    }
}

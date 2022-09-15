package it.ebinder.examifybackend.api.examlist;

import com.google.gson.JsonObject;
import it.ebinder.examifybackend.messages.Error;
import it.ebinder.examifybackend.messages.Response;
import org.springframework.web.bind.annotation.*;

@RestController
public class ExamlistController {

    @GetMapping (value = "/api/examlist")
    public Response getExamlist(
            @CookieValue(value = "JSESSIONID") String sessionid
    ){
        Response response = new Response();
        ExamlistManager.getExamlist(sessionid, response.content);
        return response;
    }

    @PutMapping (value = "/api/examlist/exam")
    public Response createExam(
            @RequestBody JsonObject bodyJson,
            @CookieValue(value = "JSESSIONID") String sessionid
    ){
        Response response = new Response();
        if (bodyJson.has("name")){
            String name = bodyJson.get("name").getAsString();
            ExamlistManager.createExam(sessionid, name, response.content);
        }else{
            return new Error(2, "Invalid body json! Missing field 'name'!");
        }

        return response;
    }

    @PutMapping (value = "/api/examlist/import")
    public Response importExam(
            @RequestBody JsonObject bodyJson,
            @CookieValue(value = "JSESSIONID") String sessionid
    ){
        Response response = new Response();
        //TODO
        return response;
    }

    @PatchMapping (value = "/api/examlist/exam")
    public Response renameExam(
            @RequestBody JsonObject bodyJson,
            @CookieValue(value = "JSESSIONID") String sessionid
    ){
        Response response = new Response();

        if (bodyJson.has("examID") && bodyJson.has("newName")){
            String examID = bodyJson.get("examID").getAsString();
            String newName = bodyJson.get("newName").getAsString();
            ExamlistManager.renameExam(sessionid, examID, newName, response.content);
        }else{
            return new Error(2, "Invalid body json! Missing field 'examID' or 'newName'!");
        }

        return response;
    }

    @DeleteMapping (value = "/api/examlist/exam")
    public Response deleteExam(
            @RequestBody JsonObject bodyJson,
            @CookieValue(value = "JSESSIONID") String sessionid
    ){
        Response response = new Response();

        if (bodyJson.has("examID")){
            String examID = bodyJson.get("examID").getAsString();
            ExamlistManager.deleteExam(sessionid, examID);
        }else{
            return new Error(2, "Invalid body json! Missing field 'examID'!");
        }

        return response;
    }

}

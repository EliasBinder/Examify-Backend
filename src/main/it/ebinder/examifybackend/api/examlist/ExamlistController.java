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
    public Response createExam(@RequestBody JsonObject bodyJson){
        Response response = new Response();

        if (bodyJson.has("name")){
            String name = bodyJson.get("name").getAsString();
            //TODO: check xss for name and create examid and create first question
            response.content.addProperty("examID", 12345);
            response.content.addProperty("name", name);
        }else{
            return new Error(2, "Invalid body json! Missing field 'name'!");
        }

        return response;
    }

    @PutMapping (value = "/api/examlist/import")
    public Response importExam(@RequestBody JsonObject bodyJson){
        Response response = new Response();
        return response;
    }

    @PatchMapping (value = "/api/examlist/exam")
    public Response renameExam(@RequestBody JsonObject bodyJson){
        Response response = new Response();

        if (bodyJson.has("examID") && bodyJson.has("newName")){
            String examID = bodyJson.get("examID").getAsString();
            String newName = bodyJson.get("newName").getAsString();
            //TODO: check xss for newName and update examid name
            response.content.addProperty("newName", newName);
        }else{
            return new Error(2, "Invalid body json! Missing field 'examID' or 'newName'!");
        }

        return response;
    }

    @DeleteMapping (value = "/api/examlist/exam")
    public Response deleteExam(@RequestBody JsonObject bodyJson){
        Response response = new Response();

        if (bodyJson.has("examID")){
            String examID = bodyJson.get("examID").getAsString();
            //TODO: delete exam
        }else{
            return new Error(2, "Invalid body json! Missing field 'examID'!");
        }

        return response;
    }

}

package it.ebinder.examifybackend.api.sharedexamlist;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import it.ebinder.examifybackend.messages.Error;
import it.ebinder.examifybackend.messages.Response;
import org.springframework.web.bind.annotation.*;

@RestController
public class SharedExamlistController {

    @GetMapping (value = "/api/sharedexamlist")
    public Response getExamlist(
            @CookieValue(value = "JSESSIONID") String sessionid
    ){
        Response response = new Response();

        //---Exam 1
        JsonObject exam1 = new JsonObject();
        exam1.addProperty("name", "Exam 01 2021/22");

        //---Exam 2
        JsonObject exam2 = new JsonObject();
        exam2.addProperty("name", "Exam 02 2021/22");

        //---Exam 3
        JsonObject exam3 = new JsonObject();
        exam3.addProperty("name", "Exam 03 2021/22");

        response.content.add("001", exam1);
        response.content.add("002", exam2);
        response.content.add("003", exam3);

        return response;
    }

    @GetMapping (value = "/api/sharedexamlist/{examID}")
    public Response getSharedWith(
            @PathVariable String examID,
            @CookieValue(value = "JSESSIONID") String sessionid
    ){
        Response response = new Response();
        JsonArray targets = new JsonArray();
        targets.add("eliasbinder@icloud.com");
        targets.add("someuser@gmail.com");
        targets.add("eliasbinder2@gmail.com");
        response.content.add("targets", targets);
        return response;
    }

    @PatchMapping (value = "/api/sharedexamlist/{examID}")
    public Response shareWith(
            @PathVariable String examID,
            @RequestBody JsonObject bodyJson,
            @CookieValue(value = "JSESSIONID") String sessionid
    ){
        Response response = new Response();

        return response;
    }

    @DeleteMapping (value = "/api/sharedexamlist/exam")
    public Response deleteShare(
            @RequestBody JsonObject bodyJson,
            @CookieValue(value = "JSESSIONID") String sessionid
    ){
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

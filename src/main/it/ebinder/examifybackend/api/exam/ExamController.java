package it.ebinder.examifybackend.api.exam;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import it.ebinder.examifybackend.messages.Error;
import it.ebinder.examifybackend.messages.Response;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;
import java.util.Set;
import java.util.UUID;

@RestController
public class ExamController {

    @GetMapping (value = "/api/exam/{examid}/getpackage")
    public Response getExamPackage(@PathVariable String examid){
        Response response = new Response();
        //TODO
        JsonObject examJson = new Gson().fromJson("{\n" +
                "  \"title\": \"IDB Exam Demo\",\n" +
                "  \"editable\": true,\n" +
                "  \"questions\": {\n" +
                "    \"1\": {\n" +
                "      \"title\": \"Question 1\",\n" +
                "      \"pos\": 1,\n" +
                "      \"content\": [{\"insert\":  \"\\n\"}],\n" +
                "      \"attachments\": {},\n" +
                "      \"answer_types\": {\n" +
                "        \"0\": {\n" +
                "          \"pos\": 1,\n" +
                "          \"type\": 0,\n" +
                "          \"content\": {\n" +
                "            \"wordlimit\": -1\n" +
                "          }\n" +
                "        }\n" +
                "      }\n" +
                "    }\n" +
                "  }\n" +
                "}", JsonObject.class);
        response.content = examJson;
        return response;
    }

    @DeleteMapping (value = "/api/exam/{examID}/questions/{questionID}")
    public Response deleteQuestion(
            @PathVariable String examID,
            @PathVariable int questionID
    ){
        Response response = new Response();
        //TODO  (+ check that at least 1 Question always exists)
        return response;
    }

    @PatchMapping (value = "/api/exam/{examID}/setquestionsposition")
    public Response moveQuestion(
            @PathVariable String examID,
            @RequestBody JsonObject bodyJson
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
            @RequestParam("file") MultipartFile attachment
    ){
        Response response = new Response();
        System.out.println("Uploading file: " + attachment.getOriginalFilename());
        response.content.addProperty("id", UUID.randomUUID().toString());
        return response;
    }

    @DeleteMapping (value = "/api/exam/{examID}/questions/{questionID}/deleteattachment/{attachmentid}")
    public Response deleteQuestionAttachment(
            @PathVariable String examID,
            @PathVariable int questionID,
            @PathVariable String attachmentid
    ){
        Response response = new Response();

        return response;
    }

    @PatchMapping (value = "/api/exam/{examID}/questions/{questionID}/setanswertypesposition")
    public Response moveAnswerTypes(
            @PathVariable String examID,
            @PathVariable int questionID,
            @RequestBody JsonObject bodyJson
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
            @RequestBody JsonObject bodyJson
    ){
        Response response = new Response();

        return response;
    }


    @PatchMapping (value = "/api/exam/{examID}/updatetext")
    public Response updateText(
            @PathVariable String examID,
            @RequestBody JsonObject bodyJson
    ){
        Response response = new Response();

        return response;
    }
}

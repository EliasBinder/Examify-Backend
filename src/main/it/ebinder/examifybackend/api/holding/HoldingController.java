package it.ebinder.examifybackend.api.holding;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import it.ebinder.examifybackend.api.holdinglist.HoldinglistManager;
import it.ebinder.examifybackend.api.profile.ProfileManager;
import it.ebinder.examifybackend.messages.Error;
import it.ebinder.examifybackend.messages.Response;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@RestController
public class HoldingController {

    @PostMapping(value = "/api/holding/{participationRef}/participate")
    public Response participate(
            @PathVariable String participationRef,
            @RequestBody JsonObject bodyJson
    ){

        Response response = new Response();
        response.content.addProperty("id", "abcde");
        String detailsJsonStr = "{\n" +
                "    \"title\": \"IDB Exam\",\n" +
                "    \"lecturer\": \"Prof. Max Mustermann\",\n" +
                "    \"name\": \"Elias Binder\",\n" +
                "    \"id\": 19269,\n" +
                "    \"start\": 1643953380000,\n" +
                "    \"end\": 1643960580000\n" +
                "  }";
        JsonObject details = new Gson().fromJson(detailsJsonStr, JsonObject.class);
        response.content.add("details", details);

        ParticipantsManager.addParticipant(participationRef, bodyJson.get("name").getAsString(), bodyJson.get("id").getAsInt());

        return response;
    }


    @GetMapping (value = "/api/holding/{participationRef}/getpackage")
    public Response getPackage(
            @PathVariable String participationRef
    ){
        Response response = new Response();
        String examStr = "{\n" +
                "    \"1\": {\n" +
                "      \"title\": \"Question 1\",\n" +
                "      \"content\": [{\"insert\":  \"Hallo. Das ist ein Test\\n\"}],\n" +
                "      \"attachments\": {\n" +
                "        \"att1-ef-sc-1ad\": {\n" +
                "          \"name\": \"image.png\",\n" +
                "          \"type\": 0,\n" +
                "          \"path\": \"http://localhost:8080/getfile/att1-ef-sc-1ad\"\n" +
                "        }\n" +
                "      },\n" +
                "      \"answer_types\": {\n" +
                "        \"1\": {\n" +
                "          \"type\": 0,\n" +
                "          \"content\": {\n" +
                "            \"wordlimit\": -1\n" +
                "          }\n" +
                "        },\n" +
                "        \"2\": {\n" +
                "          \"type\": 1,\n" +
                "          \"content\": {\n" +
                "            \"pattern\": [\n" +
                "              {\"insert\":\"Demo123\"},\n" +
                "              {\"insert\":{\"input\":true}},\n" +
                "              {\"insert\":\"Hi\\n\"}\n" +
                "            ]\n" +
                "          }\n" +
                "        },\n" +
                "        \"3\": {\n" +
                "          \"type\": 3,\n" +
                "          \"content\": {}\n" +
                "        }\n" +
                "      }\n" +
                "    }\n" +
                "  }";
        JsonObject examJson = new Gson().fromJson(examStr, JsonObject.class);
        response.content = examJson;
        return response;
    }

    @PutMapping (value = "/api/holding/{participationRef}/submit")
    public Response submit(
            @PathVariable String participationRef,
            @RequestBody JsonObject bodyJson
    ){
        Response response = new Response();
        //TODO
        return response;
    }

    @GetMapping (value = "/api/holding/{participationRef}/details")
    public Response getDetails(
            @PathVariable String participationRef,
            @CookieValue(value = "JSESSIONID") String sessionid
    ){
        Response response = new Response();
        response.content.addProperty("exam", "IDB Exam");
        response.content.addProperty("examID", "001");
        response.content.addProperty("lecturer", "Prof. Max Mustermann");
        response.content.addProperty("joinDate", 0);
        response.content.addProperty("startDate", 0);
        response.content.addProperty("endDate", 0);
        return response;
    }

    @GetMapping (value = "/api/holding/{participationRef}/participants")
    public SseEmitter streamParticipants(
            @PathVariable String participationRef,
            @CookieValue(value = "JSESSIONID") String sessionid
    ){
        SseEmitter emitter = new SseEmitter(0L);
        emitter.onCompletion(() -> ParticipantsManager.removeSseEmitter(emitter, participationRef));
        emitter.onError((e) -> ParticipantsManager.removeSseEmitter(emitter, participationRef));
        emitter.onTimeout(() -> ParticipantsManager.removeSseEmitter(emitter, participationRef));
        ParticipantsManager.streamAllParticipants(participationRef, emitter);
        return emitter;
    }

    @PutMapping (value = "/api/holding")
    public Response createHolding(
            @RequestBody JsonObject bodyJson,
            @CookieValue(value = "JSESSIONID") String sessionid
    ){
        if (bodyJson.has("examId") && bodyJson.has("join") && bodyJson.has("start") && bodyJson.has("end")){
            System.out.println(new Gson().toJson(bodyJson));
            Response response = new Response();
            response.content.addProperty("ref", "dfgh-jklq");
            return response;
        }else{
            return new Error(2, "Invalid body json! Missing field 'examId', 'join', 'start' or 'end'");
        }
    }

    @GetMapping(value = "/api/holding/{participationRef}/submission/{participantID}")
    public SseEmitter getSubmission(
            @PathVariable String participationRef,
            @PathVariable String participantID,
            @CookieValue(value = "JSESSIONID") String sessionid
    ){
        SseEmitter emitter = new SseEmitter(0L);
        emitter.onCompletion(() -> SubmissionsManager.removeSseEmitter(emitter, participationRef));
        emitter.onError((e) -> SubmissionsManager.removeSseEmitter(emitter, participationRef));
        emitter.onTimeout(() -> SubmissionsManager.removeSseEmitter(emitter, participationRef));
        SubmissionsManager.streamAllSubmissions(participationRef, participantID, emitter);
        return emitter;
    }


}

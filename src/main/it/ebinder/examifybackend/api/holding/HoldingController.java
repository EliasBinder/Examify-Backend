package it.ebinder.examifybackend.api.holding;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import it.ebinder.examifybackend.messages.Response;
import org.springframework.web.bind.annotation.*;

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

}

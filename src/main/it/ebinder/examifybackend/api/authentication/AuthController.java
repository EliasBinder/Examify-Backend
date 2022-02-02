package it.ebinder.examifybackend.api.authentication;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import it.ebinder.examifybackend.messages.Error;
import it.ebinder.examifybackend.messages.Response;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.UUID;

@RestController
public class AuthController {

    @PostMapping(value = "/api/auth/login")
    public Response login(@RequestBody JsonObject bodyJson, HttpServletResponse servletResponse){
        Response response = new Response();

        if (bodyJson.has("username") && bodyJson.has("password")){
            String submittedUsername = bodyJson.get("username").getAsString();
            String submittedPassword = bodyJson.get("password").getAsString();
            //TODO: checkUsernameAndPassword and retrieve sessionID
            String sessionID = UUID.randomUUID().toString();
            response.content.addProperty("sessionID", sessionID);
        }else{
            return new Error(2, "Invalid body json! Missing field 'username' or 'password'!");
        }

        return response;
    }

}

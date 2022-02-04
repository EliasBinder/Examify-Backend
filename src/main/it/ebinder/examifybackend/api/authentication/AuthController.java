package it.ebinder.examifybackend.api.authentication;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import it.ebinder.examifybackend.messages.Error;
import it.ebinder.examifybackend.messages.Response;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.UUID;

@RestController
public class AuthController {

    @PostMapping (value = "/api/auth/login")
    public Response login(
            HttpSession session,
            @RequestBody JsonObject bodyJson
    ){
        if (bodyJson.has("username") && bodyJson.has("password")){
            String submittedUsername = bodyJson.get("username").getAsString();
            String submittedPassword = bodyJson.get("password").getAsString();
            if (AuthManager.login(session, submittedUsername, submittedPassword)){
                return new Response();
            }else{
                return new Error(3, "Invalid credentials");
            }
        }else{
            return new Error(2, "Invalid body json! Missing field 'username' or 'password'!");
        }
    }

    @GetMapping (value = "/api/auth/status")
    public Response isLoggedIn(HttpSession session){
        Response response = new Response();
        if (AuthManager.getUsernameFromSession(session) != null)
            response.content.addProperty("status", true);
        else
            response.content.addProperty("status", false);
        return response;
    }

    @PutMapping (value = "/api/auth/register")
    public Response register(
            @RequestBody JsonObject bodyJson
    ){
        if (bodyJson.has("username") && bodyJson.has("password") && bodyJson.has("firstname") && bodyJson.has("lastname")){
            String submittedUsername = bodyJson.get("username").getAsString();
            String submittedPassword = bodyJson.get("password").getAsString();
            String submittedFirstname = bodyJson.get("firstname").getAsString();
            String submittedLastname = bodyJson.get("lastname").getAsString();
            if (AuthManager.register(submittedUsername, submittedPassword, submittedFirstname, submittedLastname))
                return new Response();
            return new Error(4, "Failed to register!");
        }else{
            return new Error(2, "Invalid body json! Missing field 'username' or 'password' or 'firstname' or 'lastname'!");
        }
    }

}

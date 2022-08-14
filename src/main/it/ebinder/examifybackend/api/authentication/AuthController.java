package it.ebinder.examifybackend.api.authentication;

import com.google.gson.JsonObject;
import it.ebinder.examifybackend.messages.Error;
import it.ebinder.examifybackend.messages.Response;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

@RestController
public class AuthController {

    @PostMapping (value = "/api/auth/login")
    public Response login(
            @RequestBody JsonObject bodyJson
    ){
        Response response1 = new Response();
        if (bodyJson.has("username") && bodyJson.has("password")){
            String submittedUsername = bodyJson.get("username").getAsString();
            String submittedPassword = bodyJson.get("password").getAsString();
            if (AuthManager.login(response1.content, submittedUsername, submittedPassword)){
                return response1;
            }else{
                return new Error(3, "Invalid credentials");
            }
        }else{
            return new Error(2, "Invalid body json! Missing field 'username' or 'password'!");
        }
    }

    @GetMapping (value = "/api/auth/status")
    public Response isLoggedIn(
            @CookieValue(value = "JSESSIONID", required = false) String sessionid
    ){
        Response response = new Response();
        if (sessionid != null) {
            if (AuthManager.getUsernameFromSession(sessionid) != null)
                response.content.addProperty("status", true);
            else
                response.content.addProperty("status", false);
            return response;
        }else{
            response.content.addProperty("status", false);
            return response;
        }
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

    @GetMapping (value = "/api/auth/logout")
    public Response logout(
            @CookieValue(value = "JSESSIONID", required = false) String sessionid
    ){
        Response response = new Response();
        //TODO
        return response;
    }

}

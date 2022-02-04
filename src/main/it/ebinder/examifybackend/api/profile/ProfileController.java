package it.ebinder.examifybackend.api.profile;

import com.google.gson.JsonObject;
import io.micrometer.core.instrument.util.IOUtils;
import it.ebinder.examifybackend.api.authentication.AuthManager;
import it.ebinder.examifybackend.messages.Error;
import it.ebinder.examifybackend.messages.Response;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

@RestController
public class ProfileController {

    @GetMapping (value = "/api/profile/package")
    public Response getProfilePackage(
            @CookieValue(value = "JSESSIONID") String sessionid
    ){
        Response response = new Response();
        ProfileManager.getProfilePackage(sessionid, response.content);
        return response;
    }

    @DeleteMapping (value = "/api/profile/image")
    public Response deleteProfileImage(
            @CookieValue(value = "JSESSIONID") String sessionid
    ){
        Response response = new Response();
        ProfileManager.deleteProfileImage(sessionid);
        return response;
    }

    @PostMapping (value = "/api/profile/image")
    public Response setProfileImage(
            @CookieValue(value = "JSESSIONID") String sessionid,
            @RequestBody JsonObject bodyJson
    ){
        Response response = new Response();

        if (bodyJson.has("image")){
            String profileImage = bodyJson.get("image").getAsString();
            ProfileManager.setProfileImage(sessionid, profileImage);
        }else{
            return new Error(2, "Invalid body json! Missing field 'image'!");
        }

        return response;
    }

    @PostMapping (value = "/api/profile/data")
    public Response setProfileData(
            @CookieValue(value = "JSESSIONID") String sessionid,
            @RequestBody JsonObject bodyJson
    ){
        Response response = new Response();

        if (bodyJson.has("firstname") && bodyJson.has("lastname")){
            String firstname = bodyJson.get("firstname").getAsString();
            String lastname = bodyJson.get("lastname").getAsString();
            ProfileManager.setProfileData(sessionid, firstname, lastname);
            response.content.addProperty("firstname", firstname);
            response.content.addProperty("lastname", lastname);
        }else{
            return new Error(2, "Invalid body json! Missing field 'firstname' or 'lastname'!");
        }

        return response;
    }


    @PostMapping (value = "/api/profile/password")
    public Response setProfilePassword(
            @CookieValue(value = "JSESSIONID") String sessionid,
            @RequestBody JsonObject bodyJson
    ){
        Response response = new Response();

        if (bodyJson.has("currentPassword") && bodyJson.has("newPassword")){
            String curPass = bodyJson.get("currentPassword").getAsString();
            String newPass = bodyJson.get("newPassword").getAsString();
            if (AuthManager.resetPassword(sessionid, curPass, newPass)){
                return response;
            }else{
                return new Error(5, "Invalid current password!");
            }
        }else{
            return new Error(2, "Invalid body json! Missing field 'currentPassword' or 'newPassword'!");
        }
    }

}

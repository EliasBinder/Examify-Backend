package it.ebinder.examifybackend.api.profile;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import io.micrometer.core.instrument.util.IOUtils;
import it.ebinder.examifybackend.messages.Error;
import it.ebinder.examifybackend.messages.Response;
import org.springframework.web.bind.annotation.*;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.UUID;

@RestController
public class ProfileController {

    @GetMapping (value = "/api/profile/package")
    public Response getProfilePackage() throws FileNotFoundException {
        //TODO
        Response response = new Response();
        response.content.addProperty("firstname", "Elias");
        response.content.addProperty("lastname", "Binder");
        response.content.addProperty("email", "eliasbinder@icloud.com");
        FileInputStream fis = new FileInputStream("/Users/eliasbinder/Desktop/testProfileImg.txt");
        String profImg = IOUtils.toString(fis);
        response.content.addProperty("profileImg", profImg);
        return response;
    }

    @DeleteMapping (value = "/api/profile/image")
    public Response deleteProfileImage(){
        //TODO
        Response response = new Response();
        return response;
    }

    @PostMapping (value = "/api/profile/image")
    public Response setProfileImage(@RequestBody JsonObject bodyJson){
        Response response = new Response();

        if (bodyJson.has("image")){
            String profileImage = bodyJson.get("image").getAsString();
            //TODO: set profile image
        }else{
            return new Error(2, "Invalid body json! Missing field 'image'!");
        }

        return response;
    }

    @PostMapping (value = "/api/profile/data")
    public Response setProfileData(@RequestBody JsonObject bodyJson){
        Response response = new Response();

        if (bodyJson.has("firstname") && bodyJson.has("lastname")){
            String firstname = bodyJson.get("firstname").getAsString();
            String lastname = bodyJson.get("lastname").getAsString();
            //TODO: check for xss and set profile data
            response.content.addProperty("firstname", firstname);
            response.content.addProperty("lastname", lastname);
        }else{
            return new Error(2, "Invalid body json! Missing field 'firstname' or 'lastname'!");
        }

        return response;
    }


    @PostMapping (value = "/api/profile/password")
    public Response setProfilePassword(@RequestBody JsonObject bodyJson){
        Response response = new Response();

        if (bodyJson.has("currentPassword") && bodyJson.has("newPassword")){
            String curPass = bodyJson.get("currentPassword").getAsString();
            String newPass = bodyJson.get("newPassword").getAsString();
            //TODO: check curPass and update to newPass
        }else{
            return new Error(2, "Invalid body json! Missing field 'currentPassword' or 'newPassword'!");
        }

        return response;
    }

}

package it.ebinder.examifybackend.messages;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

public class Response {

    public boolean success = true;
    public JsonObject content = new JsonObject();

    public void setContent(String content){
        Gson gson = new Gson();
        this.content = gson.fromJson(content, JsonObject.class);
    }

}

package it.ebinder.examifybackend.messages;

import com.google.gson.JsonObject;

public class Error extends Response{

    public boolean success = false;
    public long errCode;
    public JsonObject content;

    public Error(long errCode, String errMsg) {
        this.errCode = errCode;
        this.content.addProperty("errMsg", errMsg);
    }
}

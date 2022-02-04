package it.ebinder.examifybackend.messages;

import com.google.gson.JsonObject;

public class Error extends Response{

    public long errCode;

    public Error(long errCode, String errMsg) {
        super();
        super.success = false;
        this.errCode = errCode;
        super.content.addProperty("errMsg", errMsg);
    }
}

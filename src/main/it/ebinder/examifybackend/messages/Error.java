package it.ebinder.examifybackend.messages;

public class Error extends Response{

    public long errCode;

    public Error(long errCode, String errMsg) {
        super();
        super.success = false;
        this.errCode = errCode;
        super.content.addProperty("errMsg", errMsg);
    }
}

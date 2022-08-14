package it.ebinder.examifybackend.api.holdinglist;

import it.ebinder.examifybackend.api.examlist.ExamlistManager;
import it.ebinder.examifybackend.messages.Response;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HoldinglistController {

    @GetMapping(path = "/api/holdinglist")
    public Response getHoldinglist(
            @CookieValue(value = "JSESSIONID") String sessionid
    ){
        Response response = new Response();
        HoldinglistManager.getHoldinglist(sessionid, response.content);
        return response;
    }

}

package it.ebinder.examifybackend.api.holding;

import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;


public class SubmissionsManager {

    public synchronized static void removeSseEmitter(SseEmitter sseEmitter, String participationRef) {
        //TODO
    }

    public synchronized static void streamAllSubmissions(String ref, String participantId, SseEmitter emitter) {
        //TODO
    }
}

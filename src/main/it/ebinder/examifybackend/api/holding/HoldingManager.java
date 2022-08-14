package it.ebinder.examifybackend.api.holding;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class HoldingManager {

    public static HashMap<String, List<SseEmitter>> participantsStreams = new HashMap<>();

    public static HashMap<String, List<Participant>> participantsCache = new HashMap<>();

    public static void addParticipant(String ref, String name, int id){
        Participant participant = new Participant(name, id);
        List<Participant> participants = participantsCache.get(ref);
        if(participants == null){
            participants = new LinkedList<>();
            participantsCache.put(ref, participants);
        }
        participants.add(participant);

        List<SseEmitter> streams = participantsStreams.get(ref);
        if(streams == null){
            streams = new LinkedList<>();
            participantsStreams.put(ref, streams);
        }

        JsonObject toSend = new JsonObject();
        toSend.addProperty("action", "join");
        toSend.add("participant", new Gson().toJsonTree(participant));

        for(SseEmitter stream : streams){
            ExecutorService sseExecutor = Executors.newSingleThreadExecutor();
            sseExecutor.execute(() -> {
                try {
                    stream.send(new Gson().toJson(toSend));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
        }
    }

    public static void streamAllParticipants(String ref, SseEmitter emitter){
        if(participantsCache.get(ref) == null){
            participantsCache.put(ref, new LinkedList<>());
        }
        final List<Participant> participants = participantsCache.get(ref);

        List<SseEmitter> streams = participantsStreams.get(ref);
        if(streams == null){
            streams = new LinkedList<>();
            participantsStreams.put(ref, streams);
        }
        streams.add(emitter);
        ExecutorService sseExecutor = Executors.newSingleThreadExecutor();
        sseExecutor.execute(() -> {
            for (Participant participant: participants){
                JsonObject toSend = new JsonObject();
                toSend.addProperty("action", "join");
                toSend.add("participant", new Gson().toJsonTree(participant));
                try {
                    emitter.send(new Gson().toJson(toSend));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }

}

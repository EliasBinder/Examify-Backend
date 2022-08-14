package it.ebinder.examifybackend.api.holding;

public class Participant {

    private String name;
    private int id;

    public Participant(String name, int id) {
        this.name = name;
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public int getId() {
        return id;
    }
}

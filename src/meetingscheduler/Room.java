package meetingscheduler;

import java.util.UUID;

public class Room {
    private String id;
    private String name;

    public Room(String name) {
        this.id = UUID.randomUUID().toString();
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}

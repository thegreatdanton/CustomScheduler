package meetingscheduler;

import java.util.UUID;

public class Meeting {
    private String id;
    private long startTime;
    private long endTime;
    private Room room;

    public Meeting(long startTime, long endTime, Room room) {
        this.id = UUID.randomUUID().toString();
        this.startTime = startTime;
        this.endTime = endTime;
        this.room = room;
    }

    public String getId() {
        return id;
    }

    public long getStartTime() {
        return startTime;
    }

    public long getEndTime() {
        return endTime;
    }

    public Room getRoom() {
        return room;
    }
}

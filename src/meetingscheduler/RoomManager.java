package meetingscheduler;

import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class RoomManager {
    private Set<Room> roomSet;

    public RoomManager() {
        roomSet = new HashSet<>();
    }

    /**
     * Create a new room for hotel.
     *
     * @param room Room
     * @return
     */
    public boolean createRoom(final Room room) {
        return this.roomSet.add(room);
    }

    /**
     * Helper method to return room in the natural sorting order by name.
     *
     * @return
     */
    public List<Room> getRooms() {
        return this.roomSet.stream()
                .sorted(Comparator.comparing(Room::getName))
                .collect(Collectors.toList());
    }
}

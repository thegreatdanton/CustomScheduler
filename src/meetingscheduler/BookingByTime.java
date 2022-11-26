package meetingscheduler;

import java.util.List;

public class BookingByTime {
    public Meeting book(final long startTime, final long endTime, final Room room, final List<Meeting> meetings) {
        boolean canBook = true;
        for (Meeting m : meetings) {
            if (!(startTime >= m.getEndTime() || endTime <= m.getStartTime())) {
                canBook = false;
            }
        }

        return canBook ? new Meeting(startTime, endTime, room) : null;
    }

    // 1 4
    // 5 6

    // 1 4
    // 2 6
}

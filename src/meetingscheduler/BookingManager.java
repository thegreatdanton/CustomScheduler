package meetingscheduler;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalUnit;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class BookingManager implements Runnable {
    private RoomManager roomManager;
    private BookingByTime bookingByTime;

    private ConcurrentMap<String, List<Meeting>> bookings;
    private Object lock = new Object();

    public BookingManager(RoomManager roomManager, BookingByTime bookingByTime, ConcurrentMap<String, List<Meeting>> bookings) {
        this.roomManager = roomManager;
        this.bookingByTime = bookingByTime;
        this.bookings = bookings;
    }

    public Meeting bookRoom(long startTime, long endTime) {
        Meeting meeting = null;
        System.out.println("Start time: " + Instant.ofEpochMilli(startTime).toString() + "   End time: " + Instant.ofEpochMilli(endTime).toString());
        if (startTime >= endTime) {
            System.out.println("Bad Request");
            return null;
        }

        synchronized (lock){
            List<Room> rooms = roomManager.getRooms();
            for (Room room : rooms) {
                List<Meeting> meetings = bookings.getOrDefault(room.getId(), new ArrayList<>());
                meeting = bookingByTime.book(startTime, endTime, room, meetings);
                if (meeting != null) {
                    if (!bookings.containsKey(meeting.getRoom().getId())) {
                        bookings.put(meeting.getRoom().getId(), new ArrayList<>());
                    }
                    bookings.get(meeting.getRoom().getId()).add(meeting);
                    System.out.println("Meeting room booked, room: " + room.getId() + " meeting Id: " + meeting.getId());
                    break;
                }
            }
        }

        if (meeting == null) {
            System.out.println("No room is available for the given time");
        }
        return meeting;
    }


    public List<Meeting> getMeetingForRoom(final Room room) {
        return this.bookings.getOrDefault(room.getId(), new ArrayList<>());
    }

    @Override
    public void run() {
        Random random = new Random();
        Instant instant = Instant.now();
        for (int i = 0; i < 100; i++) {
            Instant instant1 = instant.plus(random.nextInt(10), ChronoUnit.HOURS);
            long startTime = instant1.toEpochMilli();
            long endTime = instant1.plus(random.nextInt(60), ChronoUnit.MINUTES).toEpochMilli();
            bookRoom(startTime, endTime);
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }
}

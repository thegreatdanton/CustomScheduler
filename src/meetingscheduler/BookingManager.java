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

public class BookingManager implements Runnable{
    private RoomManager roomManager;
    private BookingByTime bookingByTime;

    private Map<String, List<Meeting>> bookings;

    public BookingManager(RoomManager roomManager, BookingByTime bookingByTime, Map<String, List<Meeting>> bookings) {
        this.roomManager = roomManager;
        this.bookingByTime = bookingByTime;
        this.bookings = bookings;
    }

    public Meeting bookRoom(long startTime, long endTime){
        Meeting meeting = null;
        System.out.println("Start time: " + startTime +"   End time: " + endTime);
        if(startTime >= endTime){
            System.out.println("Bad Request");
        }

        List<Room> rooms = roomManager.getRooms();
        synchronized (rooms){
            for(Room room :rooms){
                List<Meeting> meetings = bookings.getOrDefault(room.getId(), new ArrayList<>());
                meeting = bookingByTime.book(startTime, endTime, room, meetings);
                if(meeting != null){
                    if(!bookings.containsKey(meeting.getRoom().getId())){
                        bookings.put(meeting.getRoom().getId(), new ArrayList<>());
                    }
                    bookings.get(meeting.getRoom().getId()).add(meeting);
                    System.out.println("Meeting room booked, room: " + room.getId());
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
        System.out.println(instant.toEpochMilli());
        for(int i=0; i<100; i++){
            Instant instant1 = instant.plus(random.nextInt(10), ChronoUnit.HOURS);
            long startTime = instant1.toEpochMilli();
            System.out.println(startTime);
            long endTime = instant1.plus(random.nextInt(60), ChronoUnit.MINUTES).toEpochMilli();
            System.out.println(endTime);
            bookRoom(startTime, endTime);
            /*try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }*/
        }
    }
}

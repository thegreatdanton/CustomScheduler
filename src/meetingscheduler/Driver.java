package meetingscheduler;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Driver {
    public static void main(String[] args) throws InterruptedException {
        Map<String, List<Meeting>> bookings = new HashMap<>();
        RoomManager roomManager = new RoomManager();
        BookingByTime bookingByTime = new BookingByTime();
        for(int i=0; i<10; i++){
            roomManager.createRoom(new Room("room" + i));
        }
        Thread t1 = new Thread(new BookingManager(roomManager, bookingByTime, bookings));
        //Thread t2 = new Thread(new BookingManager(roomManager, bookingByTime, bookings));

        t1.start();
        //t2.start();
        t1.join();
        //t2.join();
    }
}

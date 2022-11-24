package cache;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class KeyValueStore{


    public static void main(String[] args) throws InterruptedException {
        Random random = new SecureRandom();
        ConcurrentHashMap<String, String> keyValueStore = new ConcurrentHashMap<>();
        ConcurrentHashMap<String, Integer> locations = new ConcurrentHashMap<>();
        List<String> values = new ArrayList<>();

        Thread t1 = new Thread(new CacheWithRandom(random, keyValueStore, locations, values, true));
        Thread t2 = new Thread(new CacheWithRandom(random, keyValueStore, locations, values, false));
        t1.start();
        t2.start();
        t1.join();
        t2.join();
    }


}

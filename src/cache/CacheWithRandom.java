package cache;

import java.util.List;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;

public class CacheWithRandom implements Runnable{
    private static final String mName = "key";
    private final Random random;
    private final ConcurrentHashMap<String, String> keyValueStore ;
    private final ConcurrentHashMap<String, Integer> locations ;

    private final List<String> values;

    private final boolean shouldPut;

    public CacheWithRandom(Random random, ConcurrentHashMap<String, String> keyValueStore, ConcurrentHashMap<String, Integer> locations, List<String> values, boolean shouldPut) {
        this.random = random;
        this.keyValueStore = keyValueStore;
        this.locations = locations;
        this.values = values;
        this.shouldPut = shouldPut;
    }

    public void put(String key, String value){
        if(!keyValueStore.containsKey(key)){
            keyValueStore.put(key, value);
        }
        if(!locations.containsKey(value)){
            synchronized (values){
                locations.put(value, values.size());
                values.add(value);
            }
        }
    }

    public String getValue(String key){
        return keyValueStore.get(key);
    }

    public void remove(String key){
        if(keyValueStore.containsKey(key)){
            String value = keyValueStore.get(key);
            keyValueStore.remove(key);
            synchronized (values){
                if(locations.containsKey(value)){
                    int location = locations.get(value);
                    if(location < values.size() - 1){
                        String lastOne = values.get(values.size() - 1);
                        values.set(location, lastOne);
                        locations.put(lastOne, location);
                    }

                    locations.remove(value);
                    values.remove(values.size() - 1);
                }
            }
        }

    }

    public String getRandom(){
        return values.get(random.nextInt(values.size()));
    }

    @Override
    public void run() {
        for (int i = 1; i <= 100; i++) {
            try {
                if (!shouldPut) {
                    String key = mName + String.valueOf(new Random().nextInt(i));
                    System.out.println("Get for :" + key + " = " + getValue(key) + " at " );
                } else {
                    String key = mName + String.valueOf(i);
                    long ttl = new Random().nextInt(20) * 1000;
                    System.out.println("Put at " + System.currentTimeMillis() + ": " + key + " for eviction at");
                    put(key, key);
                }
                Thread.sleep(new Random().nextInt(5) * 100);
            } catch (InterruptedException ex) {
                try {
                    throw ex;
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }
}

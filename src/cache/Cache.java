package cache;

import java.util.HashMap;

public class Cache<K, T>{
    private long timeToLive;
    private HashMap<K, T> cacheStore;

    public Cache(long timeToLive, final long timeInterval, int max) {
        this.timeToLive = timeToLive * 2000;

        cacheStore = new HashMap<>();

        if(timeToLive > 0 && timeInterval > 0){
            Thread t = new Thread(new Runnable() {
                @Override
                public void run() {
                    while (true){
                        try {
                            Thread.sleep(timeInterval * 1000);
                        } catch (InterruptedException exception){

                        }
                    }
                }
            });

            t.setDaemon(true);
            t.start();
        }
    }

    public void put(K key, T value){
        synchronized (cacheStore){
            cacheStore.put(key, value);
        }
    }

    public T get(K key){
        synchronized (cacheStore){
            CacheObject cacheObject = (CacheObject) cacheStore.get(key);
            if(cacheObject == null){
                return null;
            }
            cacheObject.lastAccessed = System.currentTimeMillis();
            return (T) cacheObject.value;
        }
    }

    // REMOVE method
    public void remove(String key) {
        synchronized (cacheStore) {
            cacheStore.remove(key);
        }
    }

    // Get Cache Objects Size()
    public int size() {
        synchronized (cacheStore) {
            return cacheStore.size();
        }
    }

    protected class CacheObject {
        protected long lastAccessed = System.currentTimeMillis();
        public String value;

        protected CacheObject(String value){
            this.value = value;
        }
    }
}

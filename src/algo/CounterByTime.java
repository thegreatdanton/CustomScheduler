package algo;

import java.util.Random;
import java.util.TreeMap;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

public class CounterByTime implements Runnable {
    private static final int timeWindow = 10;
    private final ConcurrentHashMap<String, Node> map;

    private final long[] timeStamps;
    private final int[] hits;
    private int totalCount;

    private final boolean readForKey;
    private final boolean readTotal;
    private final boolean hit;


    public CounterByTime(boolean readForKey, boolean readTotal, boolean hit,
                         ConcurrentHashMap<String, Node> map, long[] timeStamps, int[] hits) {
        this.map = map;
        this.timeStamps = timeStamps;
        this.hits = hits;
        this.readForKey = readForKey;
        this.readTotal = readTotal;
        this.hit = hit;
        this.totalCount = 0;

    }

    public void count(String key, long timestamp) {
        map.putIfAbsent(key, new Node(timeWindow));
        final int index = Math.toIntExact(timestamp % timeWindow);
        Node node = map.get(key);
        if (node.timeStamps[index] != timestamp) {
            node.timeStamps[index] = timestamp;
            node.hits[index] = 1;
        } else {
            node.hits[index]++;
        }

        System.out.println("hit :" + key + " , timestamp " + timestamp);
        totalCount++;

        timeStamps[index] = timestamp;
        hits[index] = totalCount;
        System.out.println("total count :" + totalCount + " , timestamp " + timestamp);
    }

    public int getCount(String key, long timestamp) {
        if (map.containsKey(key)) {
            Node node = map.get(key);
            for (int i = 0; i < node.hits.length; i++) {
                if (node.timeStamps[i] != 0 && timestamp >= node.timeStamps[i]) {
                    System.out.println("count for " + key + " at timestamp " + timestamp + " is " + node.hits[i]);
                    return node.hits[i];
                }
            }
        }
        System.out.println("count for " + key + " at timestamp " + timestamp + " is " + 0);
        return 0;
    }

    public int getAllCount(long timestamp) {
        for (int i = 0; i < hits.length; i++) {
            if (timeStamps[i] != 0 && timestamp >= timeStamps[i]) {
                System.out.println("total count at timestamp " + timestamp + " is " + hits[i]);
                return hits[i];
            }
        }
        System.out.println("total count at timestamp " + timestamp + " is " + 0);
        return 0;
    }

    @Override
    public void run() {
        String keyPrefix = "key:";
        for (int i = 1; i <= 10; i++) {
            try {
                if (hit) {
                    String key = keyPrefix + String.valueOf(new Random().nextInt(i));
                    count(key, System.currentTimeMillis()/1000);
                    Thread.sleep(new Random().nextInt(5) * 1000);
                }
                else if(readForKey) {
                    String key = keyPrefix + String.valueOf(new Random().nextInt(i));
                    getCount(key, System.currentTimeMillis()/1000);
                    Thread.sleep(new Random().nextInt(5) * 1000);
                }
                else if(readTotal) {
                    getAllCount(System.currentTimeMillis()/1000);
                    Thread.sleep(new Random().nextInt(5) * 1000);
                }
            } catch (InterruptedException ex) {
                try {
                    throw ex;
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }


    class Node {
        private final long[] timeStamps;
        private final int[] hits;

        public Node(int rangeInSeconds) {
            timeStamps = new long[rangeInSeconds];
            hits = new int[rangeInSeconds];
        }
    }

    public static void main(String[] args) throws InterruptedException {

        final long[] timeStamps = new long[10];
        final int[] hits = new int[10];
        ConcurrentHashMap<String, Node> map = new ConcurrentHashMap<>();
        TreeMap<Integer, AtomicInteger> treeMap = new TreeMap<>();
        CounterByTime counterByTime = new CounterByTime(false, false, true, map, timeStamps, hits);
        long time1 = System.currentTimeMillis()/1000;
        counterByTime.count("key1", time1);
        counterByTime.count("key2", time1);
        Thread.sleep(10000);
        long time2 = System.currentTimeMillis()/1000;
        counterByTime.count("key1", time2);
        counterByTime.count("key2", time1);
        Thread.sleep(10000);

        counterByTime.getCount("key1", time1);
        counterByTime.getCount("key1", time2);

        counterByTime.getAllCount(time1);
        counterByTime.getAllCount(time2);
        Thread.sleep(5000);
        long time3 = System.currentTimeMillis()/1000;
        counterByTime.count("key1", time3);
        Thread.sleep(2000);
        counterByTime.getAllCount(time3);



    }

}

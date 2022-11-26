package algo;

import java.util.Optional;
import java.util.Random;
import java.util.TreeMap;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

public class CounterByTimeThreadSafe implements Runnable{
    private static final int timeWindow = 10;
    private final ConcurrentHashMap<String, Node> map;

    private final long[] timeStamps;
    private final AtomicInteger[] hits;
    private AtomicInteger totalCount;

    private final boolean readForKey;
    private final boolean readTotal;
    private final boolean hit;


    public CounterByTimeThreadSafe(boolean readForKey, boolean readTotal, boolean hit,
                                   ConcurrentHashMap<String, Node> map, long[] timeStamps, AtomicInteger[] hits) {
        this.map = map;
        this.timeStamps = timeStamps;
        this.hits = hits;
        this.readForKey = readForKey;
        this.readTotal = readTotal;
        this.hit = hit;
        this.totalCount = new AtomicInteger(0);

    }

    public void count(String key, long timestamp) {
        map.putIfAbsent(key, new Node(timeWindow));
        final int index = Math.toIntExact(timestamp % timeWindow);
        Node node = map.get(key);
        if (node.timeStamps[index] != timestamp) {
            node.timeStamps[index] = timestamp;
            node.hits[index] = new AtomicInteger(1);
        } else {
            node.hits[index].incrementAndGet();
        }

        System.out.println("hit :" + key + " , timestamp " + timestamp);
        totalCount.incrementAndGet();

        if (timeStamps[index] != timestamp) {
            timeStamps[index] = timestamp;
        }
        hits[index] = new AtomicInteger(totalCount.get());
        System.out.println("total count :" + totalCount.get() + " , timestamp " + timestamp);
    }

    public int getCount(String key, long timestamp) {
        if (map.containsKey(key)) {
            Node node = map.get(key);
            for (int i = 0; i < node.hits.length; i++) {
                if (node.timeStamps[i] != 0 && timestamp >= node.timeStamps[i]) {
                    System.out.println("count for " + key + " at timestamp " + timestamp + " is " + Optional.of(node.hits[i].get()).orElse(0));
                    return Optional.of(node.hits[i].get()).orElse(0);
                }
            }
        }
        System.out.println("count for " + key + " at timestamp " + timestamp + " is " + 0);
        return 0;
    }

    public int getAllCount(long timestamp) {
        int index = 0;
        long temp = Long.MIN_VALUE;
        for (int i = 0; i < hits.length; i++) {
            if (timeStamps[i] != 0 && timestamp >= timeStamps[i]) {
                temp = Math.max(temp, timeStamps[i]);
                if(temp != Long.MIN_VALUE){
                    index = i;
                }
            }
        }
        if(temp != Long.MIN_VALUE){
            System.out.println("total count at timestamp " + timestamp +
                    " is " + Optional.of(hits[index].get()).orElse(0));
            return Optional.of(hits[index].get()).orElse(0);
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
                    String key = keyPrefix + new Random().nextInt(i);
                    count(key, System.currentTimeMillis()/1000);
                    Thread.sleep(new Random().nextInt(5) * 1000);
                }
                else if(readForKey) {
                    String key = keyPrefix + new Random().nextInt(i);
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
        private final AtomicInteger[] hits;

        public Node(int rangeInSeconds) {
            timeStamps = new long[rangeInSeconds];
            hits = new AtomicInteger[rangeInSeconds];
        }
    }

    public static void main(String[] args) throws InterruptedException {

        final long[] timeStamps = new long[10];
        final AtomicInteger[] hits = new AtomicInteger[10];
        ConcurrentHashMap<String, Node> map = new ConcurrentHashMap<>();

        CounterByTimeThreadSafe counterByTime1 = new CounterByTimeThreadSafe(false, false, true, map, timeStamps, hits);
        CounterByTimeThreadSafe counterByTime2 = new CounterByTimeThreadSafe(true, false, false, map, timeStamps, hits);
        CounterByTimeThreadSafe counterByTime3 = new CounterByTimeThreadSafe(false, true, false, map, timeStamps, hits);

        Thread t1 = new Thread(counterByTime1);
        Thread t2 = new Thread(counterByTime2);
        Thread t3 = new Thread(counterByTime3);

        t1.start();
        t2.start();
        t3.start();
        t1.join();
        t2.join();
        t3.join();
    }

}

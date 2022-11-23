package blockingqueue;

import java.util.Deque;
import java.util.LinkedList;

public class BoundedBlockingQueue<T> {
    private final Deque<T> deque;
    private final int size;
    private final Object lock;

    public BoundedBlockingQueue(int size) {
        this.size = size;
        this.deque = new LinkedList<T>();
        lock = new Object();
    }

    // When queue is full,
    // block enqueue thread,
    // add thread to full waiting list
    public void enqueue(T element) throws InterruptedException {
        synchronized (lock){
            while (deque.size() == size){
                lock.wait();
            }
            deque.addLast(element);
            lock.notifyAll();
        }
    }

    // When queue is empty, we block dequeue thread,
    // and add thread to empty waiting list
    public T dequeue() throws InterruptedException {
        T val = null;
        synchronized (lock){
            while (deque.isEmpty()){
                lock.wait();
            }
            val = deque.removeFirst();
            lock.notifyAll();
        }
        return val;
    }

    public int size() {
        return deque.size();
    }
}

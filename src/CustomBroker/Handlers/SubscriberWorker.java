package CustomBroker.Handlers;

import CustomBroker.Models.Message;
import CustomBroker.Models.Topic;
import CustomBroker.Models.TopicSubscriber;
import lombok.NonNull;
import lombok.SneakyThrows;

@NonNull
public class SubscriberWorker implements Runnable{
    private final Topic topic;
    private final TopicSubscriber topicSubscriber;

    public SubscriberWorker(@NonNull final  Topic topic, @NonNull final TopicSubscriber topicSubscriber){
        this.topic = topic;
        this.topicSubscriber = topicSubscriber;
    }

    @SneakyThrows
    @Override
    public void run() {
        synchronized (topicSubscriber){
            do{
                int currOffset = topicSubscriber.getOffset().get();
                while(currOffset >= topic.getMessages().size()){
                    topicSubscriber.wait();
                }

                Message message = topic.getMessages().get(currOffset);
                topicSubscriber.getSubscriber().consume(message);

                // We cannot just increment here since subscriber offset can be reset while it is consuming. So, after
                // consuming we need to increase only if it was previous one.
                topicSubscriber.getOffset().compareAndSet(currOffset, currOffset + 1);
            } while (true);
        }
    }

    synchronized public void wakeUpIfNeeded(){
        synchronized (topicSubscriber){
            topicSubscriber.notify();
        }
    }
}

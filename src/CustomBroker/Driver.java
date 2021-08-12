package CustomBroker;

import CustomBroker.Models.Message;
import CustomBroker.Models.Topic;

public class Driver {
    public static void main(String[] args) throws InterruptedException{
        final MessageBroker messageBroker = new MessageBroker();
        final Topic topic1 = messageBroker.CreateTopic("topic1");
        final Topic topic2 = messageBroker.CreateTopic("topic2");

        final SleepingSubscriber subscriber1 = new SleepingSubscriber("subscriber1", 10000);
        final SleepingSubscriber subscriber2 = new SleepingSubscriber("subscriber2", 10000);

        messageBroker.subscribe(subscriber1, topic1);
        messageBroker.subscribe(subscriber2, topic2);

        final SleepingSubscriber subscriber3 = new SleepingSubscriber("subscriber3", 5000);
        messageBroker.subscribe(subscriber3, topic2);
        messageBroker.publish(topic1, new Message("message1"));
        messageBroker.publish(topic1, new Message("message2"));

        messageBroker.publish(topic2, new Message("message3"));
        Thread.sleep(15000);

        messageBroker.publish(topic2, new Message("message4"));
        messageBroker.publish(topic1, new Message("message5"));

        messageBroker.resetOffset(topic1, subscriber1, 0);
    }
}

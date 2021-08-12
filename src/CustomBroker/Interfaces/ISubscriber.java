package CustomBroker.Interfaces;

import CustomBroker.Models.Message;

public interface ISubscriber {
    String getId();
    void consume(Message message) throws InterruptedException;
}

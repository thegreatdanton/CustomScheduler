package CustomBroker.Models;

import CustomBroker.Interfaces.ISubscriber;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;

import java.util.concurrent.atomic.AtomicInteger;

@AllArgsConstructor
@Getter
public class TopicSubscriber {
    private final AtomicInteger offset;
    private final ISubscriber subscriber;

    public TopicSubscriber(@NonNull final ISubscriber subscriber){
        this.subscriber = subscriber;
        this.offset = new AtomicInteger(0);
    }
}

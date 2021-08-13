package CustomBroker;

import CustomBroker.Handlers.TopicHandler;
import CustomBroker.Interfaces.ISubscriber;
import CustomBroker.Models.Message;
import CustomBroker.Models.Topic;
import CustomBroker.Models.TopicSubscriber;
import lombok.NonNull;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class MessageBroker {
    private final Map<String, TopicHandler> topicHandlerMap;

    public MessageBroker(){
        this.topicHandlerMap = new HashMap<>();
    }

    public Topic CreateTopic(@NonNull final String topicName){
        final Topic topic = new Topic(topicName, UUID.randomUUID().toString());
        TopicHandler topicHandler = new TopicHandler(topic);
        topicHandlerMap.put(topic.getTopicId(), topicHandler);
        System.out.println("Created topic: "+topic.getTopicName());
        return topic;
    }

    public void subscribe(@NonNull final ISubscriber subscriber, @NonNull final Topic topic){
        topic.addSubscriber(new TopicSubscriber(subscriber));
        System.out.println(subscriber.getId() + " subscribed to topic: " + topic.getTopicName());
    }

    public void publish(@NonNull final Topic topic, @NonNull final Message message){
        topic.addMessage(message);
        System.out.println(message.getMessage() + " published to topic: " + topic.getTopicName());
        new Thread(() -> topicHandlerMap.get(topic.getTopicId()).publish()).start();
    }

    public void resetOffset(@NonNull final Topic topic, @NonNull final ISubscriber subscriber, @NonNull final Integer newOffset){
        for(TopicSubscriber topicSubscriber: topic.getSubscribers()){
            topicSubscriber.getOffset().set(newOffset);
            System.out.println(topicSubscriber.getSubscriber().getId()+ " offset reset to: "+ newOffset);
            new Thread(() -> topicHandlerMap.get(topic.getTopicId()).startSubscriberWorker(topicSubscriber)).start();
            break;
        }
    }
}

package MessageChannelTest.mock;

import net.blockdynasty.economy.core.domain.services.courier.Courier;
import net.blockdynasty.economy.core.domain.services.courier.Message;
import net.blockdynasty.economy.engine.MessageChannel.Subscriber;

public class PublisherMock implements Courier {
    private Subscriber subscriber;
    public PublisherMock(Subscriber subscriber) {
        this.subscriber = subscriber;
    }
    @Override
    public void sendUpdateMessage(Message message) {
        subscriber.processMessage(message.toJsonString());
    }
}

package server.fake;

import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.SimpMessagingTemplate;

public class TestSimpMessagingTemplate extends SimpMessagingTemplate {
    public TestSimpMessagingTemplate() {
        super(new MessageChannel() {
            @Override
            public boolean send(Message<?> message, long timeout) {
                return false;
            }
        });
    }
}

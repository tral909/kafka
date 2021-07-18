package chat;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class SpringChatConsumer {

	@KafkaListener(groupId = "app", topics = "uuu")
	public void readChatMessages(ConsumerRecord<String, String> record) {
		System.out.println("New messages in chat: " + record.value());
	}
}

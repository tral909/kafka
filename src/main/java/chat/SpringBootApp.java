package chat;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.kafka.annotation.EnableKafka;

import java.io.BufferedReader;
import java.io.InputStreamReader;

@SpringBootApplication
@EnableKafka
public class SpringBootApp implements CommandLineRunner {

	@Autowired
	private SpringChatProducer chatProducer;

	public static void main(String[] args) {
		SpringApplication.run(SpringBootApp.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		final String topic = "uuu";

		System.out.println("Enter your name: ");
		BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
		String name = reader.readLine();

		System.out.println("Enter messages: ");

		String text;
		while (!(text = reader.readLine()).isBlank()) {
			chatProducer.send(topic, name, text);
		}
	}

}

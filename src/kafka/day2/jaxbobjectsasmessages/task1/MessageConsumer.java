package kafka.day2.jaxbobjectsasmessages.task1;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.TopicPartition;

import kafka.day2.jaxbobjectsasmessages.domain.Employee;

public class MessageConsumer {

	public static void main(String[] args) {
		
		String groupId = "group1";
		
		System.out.println("Consumer groupId = "+groupId);

		Properties properties = new Properties();
		properties.setProperty("bootstrap.servers", "localhost:9092");
		properties.setProperty("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
		properties.setProperty("value.deserializer", "kafka.day2.jaxbobjectsasmessages.deserializer.EmployeeDeserializer");
		properties.setProperty("group.id", groupId);
		
		//groupid property is meant to share the messages between consumers which have same group id
		// it acts like a loan balancer. A message sent to a consumer will not be sent to another
		// consumer with same group id

		KafkaConsumer<String, Employee> consumer = new KafkaConsumer<String, Employee>(properties);
		
		List<TopicPartition> partitionList = new ArrayList<>();
		
		int partitionNumber = Integer.parseInt(args[0]);
		
		partitionList.add(new TopicPartition("emp-topic-with4", partitionNumber));

		consumer.assign(partitionList);

		while (true) {
			ConsumerRecords<String, Employee> records = consumer.poll(Duration.ofSeconds(3));

			records.forEach(record -> {
				System.out.println("Recieved |" + record.key() + " : " + record.value() + " | partition =" + record.partition());

			});

		}

	}

}

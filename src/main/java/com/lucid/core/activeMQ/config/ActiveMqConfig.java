package com.lucid.core.activeMQ.config;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.lucid.core.activeMQ.service.ActiveMqServiceImpl;
import com.lucid.core.email.entity.EmailContent;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;


@Configuration
@ComponentScan(basePackageClasses = ActiveMqServiceImpl.class)
public class ActiveMqConfig {


//    ConnectionFactory connectionFactory = new ActiveMQConnectionFactory("tcp://localhost:61616");
//
//    // Create a connection and start it
//    Connection connection;

    public void SendMessage(String destinationPath, EmailContent emailContent) {
//        try {
//            connection = connectionFactory.createConnection();
//            Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
//            connection.start();
//
//            Destination destination = session.createQueue(destinationPath);
//            MessageProducer producer = session.createProducer(destination);
//
//            // Create a TextMessage and serialize the EmailContent object as JSON
//            ObjectMapper objectMapper = new ObjectMapper();
//            String emailContentJson = objectMapper.writeValueAsString(emailContent);
//            TextMessage message = session.createTextMessage(emailContentJson);
//
//            producer.send(message);
//
//            // Clean up resources
//            producer.close();
//            session.close();
//            connection.close();
//
//        } catch (JMSException | JsonProcessingException e) {
//            throw new RuntimeException(e);
//        }
    }




    //@JmsListener(destination = "lucidMail-Queue")
    public void receiveMessage(String jsonContent,EmailContent Content) {
        try {
            // You can deserialize the JSON content into an EmailContent object using Jackson ObjectMapper
            ObjectMapper objectMapper = new ObjectMapper();
            EmailContent emailContent = objectMapper.readValue(jsonContent, EmailContent.class);

            // Process the emailContent here
            System.out.println("Received message: " + emailContent);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}

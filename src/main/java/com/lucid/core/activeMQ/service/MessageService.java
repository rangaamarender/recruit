package com.lucid.core.activeMQ.service;


import org.springframework.stereotype.Service;

@Service
public class MessageService {

    /*private final JmsTemplate jmsTemplate;

    public MessageService(ConnectionFactory connectionFactory) {
        this.jmsTemplate = new JmsTemplate((jakarta.jms.ConnectionFactory) connectionFactory);
    }

    public void sendMessage(String destinationPath, EmailContent emailContent) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            String emailContentJson = objectMapper.writeValueAsString(emailContent);
            jmsTemplate.convertAndSend(destinationPath, emailContentJson);
        } catch (com.fasterxml.jackson.core.JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    @JmsListener(destination = "lucidMail-Queue")
    public void receiveMessage(String jsonContent) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            EmailContent emailContent = objectMapper.readValue(jsonContent, EmailContent.class);
            // Process the emailContent here
            System.out.println("Received message: " + emailContent);
        } catch (JsonMappingException e) {
            throw new RuntimeException(e);
        } catch (com.fasterxml.jackson.core.JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }*/
}

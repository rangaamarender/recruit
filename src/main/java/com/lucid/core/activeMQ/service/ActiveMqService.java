package com.lucid.core.activeMQ.service;

import com.lucid.core.email.entity.EmailContent;


public interface ActiveMqService {
    void sendMessagesToQueue(EmailContent emailContent);

    void receiveQueueMessage(EmailContent emailContent);
}

package com.lucid.core.activeMQ.service;

import com.lucid.core.activeMQ.config.ActiveMqConfig;
import com.lucid.core.email.entity.EmailContent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class ActiveMqServiceImpl implements ActiveMqService{


    @Autowired
    private ActiveMqConfig activeMqConfig;

    @Override
    public void sendMessagesToQueue(EmailContent emailContent) {
        activeMqConfig.SendMessage("lucidMail-Queue", emailContent);
    }


    @Override
    public void receiveQueueMessage(EmailContent emailContent) {
        activeMqConfig.receiveMessage("lucidMail-Queue", emailContent);
    }


}

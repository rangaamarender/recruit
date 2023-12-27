package com.lucid.core.activeMQ.controller;
import com.lucid.core.activeMQ.service.ActiveMqService;
import com.lucid.core.email.entity.EmailContent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;



@Controller
@RequestMapping("/api/activeMq")
public class ActiveMqController {

    @Autowired
    private ActiveMqService activeMqService;

    @PostMapping("/sendMessage")
    public void sendEmailToQueue() {
        EmailContent emailContent = new EmailContent();
        activeMqService.sendMessagesToQueue(emailContent);
        // You can add additional logic or response handling here if needed
    }


    @GetMapping("/receveMessage")
    public void receveEmailFromQueue(){
        EmailContent emailContent=new EmailContent();
        activeMqService.receiveQueueMessage(emailContent);
    }

}

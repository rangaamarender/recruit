package com.lucid.core.email.controller;

import com.lucid.core.email.entity.EmailContent;
import com.lucid.core.email.service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/email")
public class EmailController {

    @Autowired
    private  EmailService emailService;

    @PostMapping("/send")
    public ResponseEntity<Object> sendEmail(@RequestBody EmailContent emailContent) {
       return emailService.sendEmail(emailContent)?ResponseEntity.ok("Email Sent successfully"):new ResponseEntity<>("mail not sent , internal error",HttpStatus.INTERNAL_SERVER_ERROR);
    }


    @PostMapping("/sendMessage")
    public void sendEmailToQueue() {
        EmailContent emailContent = new EmailContent();
        emailService.sendMessagesToQueue(emailContent);
        // You can add additional logic or response handling here if needed
    }


    @GetMapping("/receveMessage")
        public void receveEmailFromQueue(){
        EmailContent emailContent=new EmailContent();
        emailService.receiveQueueMessage(emailContent);
    }
}

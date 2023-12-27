package com.lucid.core.email.service;


import com.lucid.core.base.BaseService;
import com.lucid.core.email.entity.EmailContent;
import com.sendgrid.Response;

public interface EmailService extends BaseService {
     boolean sendEmail(EmailContent emailContent);

     EmailContent resendEmail(EmailContent emailContent);

     void sendMessagesToQueue(EmailContent emailContent);

     void receiveQueueMessage(EmailContent emailContent);
}

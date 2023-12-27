package com.lucid.core.email.service;
import com.lucid.core.activeMQ.config.ActiveMqConfig;
import com.lucid.core.base.BaseServiceImpl;
import com.lucid.core.email.entity.EmailContent;
import com.lucid.core.email.repo.EmailContentRepository;
import com.lucid.core.email.sendgrid.SendGridMailHelper;
import com.lucid.util.Strings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;



@Service
public class EmailServiceImpl extends BaseServiceImpl implements EmailService {

    private static final Logger log = LoggerFactory.getLogger(EmailServiceImpl.class);

    @Autowired
    private EmailContentRepository emailContentRepository;

    @Autowired
    private SendGridMailHelper sendGridService;

    @Autowired
    private ActiveMqConfig activeMQConfig;


    @Override
    public boolean sendEmail(EmailContent emailContent) {
        try {
            sendGridService.sendMail(emailContent);
            emailContent.setStatus("CLOSED");
            log.info("Email sent successfully");
            emailContentRepository.save(emailContent);
            sendMessagesToQueue(emailContent);
            return true;
        } catch (Exception e) {
            log.error("sending email failed");
            emailContent.setStatus("OPEN");
            if (!Strings.isNullOrEmpty(emailContent.getEmailContentId())) {
                emailContent.setResendCount(emailContent.getResendCount() + 1);
            }
            e.printStackTrace();
            emailContentRepository.save(emailContent);
            return false;
        }
    }

    @Override
    public EmailContent resendEmail(EmailContent emailContent) {
        log.info("trying to resendEmail");
        try{
            sendGridService.sendMail(emailContent);
            emailContent.setStatus("CLOSED");
            log.info("Email sent successfully");
        }
        catch (Exception e){
            log.error("sending email failed");
            emailContent.setStatus("OPEN");
            if(!Strings.isNullOrEmpty(emailContent.getEmailContentId())){
                emailContent.setResendCount(emailContent.getResendCount()+1);
            }
        }
        return emailContent;
    }




    @Override
    public void sendMessagesToQueue(EmailContent emailContent) {
        activeMQConfig.SendMessage("lucidMail-Queue", emailContent);
    }


    @Override
    public void receiveQueueMessage(EmailContent emailContent) {
        activeMQConfig.receiveMessage("lucidMail-Queue", emailContent);
    }
}
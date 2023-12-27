/**
 * All Rights Reserved. Private and Confidential. May not be disclosed without
 * permission.
 */
package com.lucid.core.email;
import com.lucid.core.email.entity.EmailContent;
import com.lucid.core.email.repo.EmailContentRepository;
import com.lucid.core.exception.InvalidDataException;
import com.sendgrid.Method;
import com.sendgrid.Request;
import com.sendgrid.Response;
import com.sendgrid.SendGrid;
import com.sendgrid.helpers.mail.Mail;
import com.sendgrid.helpers.mail.objects.Content;
import com.sendgrid.helpers.mail.objects.Email;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

/**
 * @author sgutti
 * @date Nov 6, 2018 11:59:17 PM
 */
@Component(value = EmailHelper.NAME)
public class EmailHelper {

    SendGrid sendGrid = new SendGrid("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");
    public static final String NAME = "emailHelper";
    // --------------------------------------------------------- Class Variables
    // ----------------------------------------------------- Static Initializers
    // ------------------------------------------------------ Instance Variables
    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private EmailContentRepository emailContentRepository;


    private EmailHelper() {
        super();
    }

    /**
     *
     * @param emailContent
     * @throws MessagingException
     */
    public void sendEmail(EmailContent emailContent) throws MessagingException {
        final MimeMessage mimeMessage = this.mailSender.createMimeMessage();
        final MimeMessageHelper msg = new MimeMessageHelper(mimeMessage,
                "UTF-8");
        msg.setTo(emailContent.getTo());
        msg.setFrom(emailContent.getFrom());
        msg.setSubject(emailContent.getSubject());
        msg.setText(emailContent.getBody(), true);
        if (emailContent.getCc() != null && emailContent.getCc().length > 0) {
            msg.setCc(emailContent.getCc());
        }
        mailSender.send(mimeMessage);
    }

    public void sendEmailWithAttachment(EmailContent emailContent,MultipartFile attachment) throws MessagingException, IOException {
        final MimeMessage mimeMessage = this.mailSender.createMimeMessage();
        final MimeMessageHelper msg = new MimeMessageHelper(mimeMessage, true,"UTF-8");
        msg.setTo(emailContent.getTo());
        msg.setFrom(emailContent.getFrom());
        msg.setSubject(emailContent.getSubject());
        msg.setText(emailContent.getBody(), true);
        if (emailContent.getCc() != null && emailContent.getCc().length > 0) {
            msg.setCc(emailContent.getCc());
        }
        byte[] attachmentData = attachment.getBytes();
        ByteArrayResource mailAttachment = new ByteArrayResource(attachmentData);
        msg.addAttachment(attachment.getOriginalFilename(),mailAttachment);
        mailSender.send(mimeMessage);
    }





}

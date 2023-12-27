package com.lucid.core.email.sendgrid;


import com.lucid.core.email.entity.EmailContent;
import com.lucid.core.exception.ApplicationException;
import com.lucid.core.exception.ErrorCodes;
import com.sendgrid.Method;
import com.sendgrid.Request;
import com.sendgrid.Response;
import com.sendgrid.SendGrid;
import com.sendgrid.helpers.mail.Mail;
import com.sendgrid.helpers.mail.objects.Content;
import com.sendgrid.helpers.mail.objects.Email;
import com.sendgrid.helpers.mail.objects.Personalization;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.Objects;

@Service
public class SendGridMailHelper {

    private static final Logger LOGGER = LoggerFactory.getLogger(SendGridMailHelper.class);

    @Autowired(required = false)
    private SendGrid sendGrid;

    public void sendMail(EmailContent emailContent) throws Exception {
        if (Objects.isNull(sendGrid)) {
            LOGGER.info("SendGrid not configured to send emails");
            throw new ApplicationException(ErrorCodes.RESOURCE_NOT_FOUND_404, "SendGrid not configured to send emails");
        }
        Mail mail = new Mail();
        mail.setFrom(new Email(emailContent.getFrom()));
        Personalization personalization = new Personalization();
        personalization.setSubject(emailContent.getSubject());
        //set to list
        emailContent.getToList().forEach(s -> {
            personalization.addTo(new Email(s));
        });
        //set cc list
        emailContent.getCcList().forEach(s -> {
            personalization.addCc(new Email(s));
        });
        //add personalization
        mail.addPersonalization(personalization);
        //add contentBody
        /*(multipart/mixed): You can include multiple parts in a single email,
         such as both plain text and HTML content or text and attachments.
         This content type is used to structure complex email messages.*/
        Content content = new Content("multipart/mixed", emailContent.getBody());
        mail.addContent(content);

        Request request = new Request();
        request.setMethod(Method.POST);
        request.setEndpoint("mail/send");
        request.setBody(mail.build());
        Response response = sendGrid.api(request);
        LOGGER.info(response.getBody() + "........" + response.getStatusCode());
        if(!String.valueOf(response.getStatusCode()).startsWith("2")){
            throw new ApplicationException(String.valueOf(response.getStatusCode()),"Internal server error");
        }
    }
}

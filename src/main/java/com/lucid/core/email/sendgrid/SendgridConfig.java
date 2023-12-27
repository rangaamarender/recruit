package com.lucid.core.email.sendgrid;

import com.sendgrid.SendGrid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

@Configuration
public class SendgridConfig {

    private static final Logger LOGGER = LoggerFactory.getLogger(SendgridConfig.class);

    @Autowired
    private Environment env;

    @Bean
    public SendGrid configureSendGrid(){
          if(env.containsProperty("spring.sendgrid.api-key") && !env.getProperty("spring.sendgrid.api-key").isEmpty()){
              return new SendGrid(env.getProperty("spring.sendgrid.api-key"));
          }
          LOGGER.error("sendgrid api key not found to configure sendgrid");
          return null;
    }

}

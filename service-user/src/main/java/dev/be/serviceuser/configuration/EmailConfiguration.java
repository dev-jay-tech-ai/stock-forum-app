package dev.be.serviceuser.configuration;

import dev.be.serviceuser.utils.EmailValidatorUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import java.util.Properties;

public class EmailConfiguration {
    @Configuration
    public static class EmailConfig {

        @Value("smtp.gmail.com")
        private String host;

        @Value("587")
        private int port;

        @Value("devjaytechai@gmail.com")
        private String username;

        @Value("wdzv cpun kplh gkoy")
        private String password;

        @Value("true")
        private boolean auth;

        @Value("true")
        private boolean starttlsEnable;

        @Value("true")
        private boolean starttlsRequired;

        @Value("5000")
        private int connectionTimeout;

        @Value("5000")
        private int timeout;

        @Value("5000")
        private int writeTimeout;

        @Bean
        public JavaMailSenderImpl javaMailSender() {
            JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
            mailSender.setHost(host);
            mailSender.setPort(port);
            mailSender.setUsername(username);
            mailSender.setPassword(password);
            mailSender.setDefaultEncoding("UTF-8");
            mailSender.setJavaMailProperties(getMailProperties());

            return mailSender;
        }

        private Properties getMailProperties() {
            Properties properties = new Properties();
            properties.put("mail.smtp.auth", auth);
            properties.put("mail.smtp.starttls.enable", starttlsEnable);
            properties.put("mail.smtp.starttls.required", starttlsRequired);
            properties.put("mail.smtp.connectiontimeout", connectionTimeout);
            properties.put("mail.smtp.timeout", timeout);
            properties.put("mail.smtp.writetimeout", writeTimeout);

            return properties;
        }

        @Bean
        public EmailValidatorUtils emailValidator() {
            return new EmailValidatorUtils();
        }
    }
}

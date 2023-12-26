package web.multitask.app.config;

import java.util.Properties;

import javax.mail.Session;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.github.cdimascio.dotenv.Dotenv;

@Configuration
public class EmailConfig {

    Dotenv dotenv;

    public EmailConfig(Dotenv dotenv) {
        this.dotenv = dotenv;
    }
    
    @Bean
    public Session setSession() {
        Properties properties = new Properties();
        String enabled = dotenv.get("EMAIL_STARTTLS_ENABLE");
        String email = dotenv.get("EMAIL_CORREO");
        String password = dotenv.get("EMAIL_PASSWORD");
        String port = dotenv.get("EMAIL_PORT");
        String host = dotenv.get("EMAIL_HOST");
        properties.put("mail.smtp.host", host);
        properties.put("mail.smtp.starttls.enable", enabled);
        properties.put("mail.smtp.port", port);
        properties.put("mail.smtp.mail.sender", email);
        properties.put("mail.smtp.password", password);
        properties.put("mail.smtp.user", email);
        properties.put("mail.smtp.auth", enabled);
        properties.put("mail.smtp.ssl.trust", "smtp.gmail.com"); 
        properties.put("mail.smtp.socketFactory.port", port);
        properties.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        properties.put("mail.smtp.ssl.protocols", "TLSv1.2");
        return Session.getDefaultInstance(properties);
    }

}

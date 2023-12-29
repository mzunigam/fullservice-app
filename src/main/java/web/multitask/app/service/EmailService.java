package web.multitask.app.service;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.Objects;
import java.util.Properties;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.mail.BodyPart;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import org.json.JSONObject;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import io.github.cdimascio.dotenv.Dotenv;
import web.multitask.app.model.EmailRequest;
import web.multitask.app.utils.CommonUtils;

@Service
public class EmailService {

    Dotenv dotenv;
    Session session;
    CommonUtils commonUtils;

    public EmailService(Dotenv dotenv, Session session, CommonUtils commonUtils) {
        this.dotenv = dotenv;
        this.session = session;
        this.commonUtils = commonUtils;
    }

    public MimeMessage htmlMessage(EmailRequest request) throws UnsupportedEncodingException {
        try {
            String email = dotenv.get("EMAIL_CORREO");
            MimeMessage message = new MimeMessage(session);
            message.setFrom(new InternetAddress(email, "FULLSERVICE APPLICATION"));
            message.setSender(new InternetAddress(email, "FULLSERVICE APPLICATION"));
            message.setRecipients(MimeMessage.RecipientType.TO,
                    buildRecipients(new JSONObject().put("to", request.getTo()), "to"));
            if (request.getCc() != null) {
                message.setRecipients(MimeMessage.RecipientType.CC,
                        buildRecipients(new JSONObject().put("cc", request.getCc()), "cc"));
            }
            if (request.getBcc() != null) {
                message.setRecipients(MimeMessage.RecipientType.BCC,
                        buildRecipients(new JSONObject().put("bcc", request.getBcc()), "bcc"));
            }
            message.setSubject(request.getSubject());
            MimeBodyPart texto = new MimeBodyPart();
            texto.setContent(request.getBody(), "text/html; charset=utf-8");
            MimeMultipart multiParte = new MimeMultipart();
            multiParte.addBodyPart(texto);
            try {
                String file_folder = dotenv.get("FILE_FOLDER");
                MultipartFile[] files = request.getFiles();
                for (MultipartFile file : files) {
                    BodyPart adjunto = new MimeBodyPart();
                    String fileName = file.getOriginalFilename();
                    convertByteArrayToFile(file.getBytes(), fileName, file_folder);
                    FileDataSource fds = new FileDataSource(file_folder +"/"+ fileName);
                    adjunto.setDataHandler(new DataHandler(fds));
                    adjunto.setFileName(fds.getName());
                    multiParte.addBodyPart(adjunto);
                }
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
            message.setContent(multiParte);
            return message;
        } catch (MessagingException e) {
            System.out.println(e.getMessage());
            return null;
        }
    }

    public void convertByteArrayToFile (byte[] byteArray, String fileName, String file_folder){
        try (FileOutputStream fos = new FileOutputStream(file_folder +"/"+ fileName)) {
            fos.write(byteArray);
        }catch(Exception e){
            System.out.println(e.getMessage());
        }
    }

    public MimeMessage simpleMessage(EmailRequest request) throws UnsupportedEncodingException {
        try {
            MimeMessage message = new MimeMessage(session);
            String email = dotenv.get("EMAIL_CORREO");
            message.setFrom(new InternetAddress(email, "FULLSERVICE APPLICATION"));
            message.setSender(new InternetAddress(email, "FULLSERVICE APPLICATION"));
            message.setRecipients(MimeMessage.RecipientType.TO,
                    buildRecipients(new JSONObject().put("to", request.getTo()), "to"));
            message.setSubject(request.getSubject());
            message.setText(request.getBody());
            return message;
        } catch (MessagingException e) {
            e.printStackTrace();
            return null;
        }
    }

    public JSONObject send(MimeMessage message) {
        try {
            Transport t = session.getTransport("smtp");
            String email = dotenv.get("EMAIL_CORREO");
            String password = dotenv.get("EMAIL_PASSWORD");
            t.connect(email, password);
            t.sendMessage(message, message.getAllRecipients());
            t.close();
//            CommonUtils.deleteAllFiles();
            boolean deletes = commonUtils.deleteAllFiles();
            if(!deletes){
                System.out.println("Error al eliminar los archivos");
            }
            return new JSONObject().put("message", "OK").put("status", true);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return new JSONObject().put("message", e.getMessage()).put("status", false);
        }

    }

    public InternetAddress[] buildRecipients(JSONObject json, String type) {
        String[] recipients = json.optString(type).split(",");
        InternetAddress[] addresses = new InternetAddress[recipients.length];
        Stream.of(recipients).map(recipient -> {
            try {
                return new InternetAddress(recipient);
            } catch (AddressException e) {
                System.out.println(e.getMessage());
                return null;
            }
        }).filter(Objects::nonNull).collect(Collectors.toList()).toArray(addresses);
        return addresses;
    }

}
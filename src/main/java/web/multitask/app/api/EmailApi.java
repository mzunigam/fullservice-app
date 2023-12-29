package web.multitask.app.api;

import javax.mail.internet.MimeMessage;

import io.github.cdimascio.dotenv.Dotenv;
import org.json.JSONObject;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RestController;
import web.multitask.app.model.EmailRequest;
import web.multitask.app.mysql.EmailMysql;
import web.multitask.app.service.EmailService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;


@RestController
@RequestMapping("/email")

public class EmailApi {
    private final Dotenv dotenv;
    private final EmailService emailService;
    private final EmailMysql emailMysql;

    public EmailApi(EmailService emailService, EmailMysql emailMysql, Dotenv dotenv) {
        this.emailService = emailService;
        this.emailMysql = emailMysql;
        this.dotenv = dotenv;
    }

    @RequestMapping(path = "/public/simple", method = RequestMethod.POST, consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<?> simple(@ModelAttribute EmailRequest request) {
        try {
            MimeMessage message = emailService.simpleMessage(request);
            JSONObject response = emailService.send(message);
            return ResponseEntity.ok(response.toMap());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(new JSONObject().put("message", e.getMessage()).put("status", false).toMap());
        }
    }

    @RequestMapping(path = "/private/full", method = RequestMethod.POST, consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<?> full(@ModelAttribute EmailRequest request) {
        try {
            MimeMessage message = emailService.htmlMessage(request);
            JSONObject response = emailService.send(message);
            return ResponseEntity.ok(response.toMap());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(new JSONObject().put("message", e.getMessage()).put("status", false).toMap());
        }
    }

    @RequestMapping(path = "/service/confirmation", method = RequestMethod.POST, consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<?> template(@ModelAttribute EmailRequest request) {
        try {
            String URL =  dotenv.get("URL_CONFIRMATION");
            assert URL != null;
            JSONObject extra = request.getExtra();
            JSONObject response = emailMysql.getHTMLTemplate(extra);
            if(response.getBoolean("status")){
                JSONObject data = response.getJSONArray("data").getJSONObject(0);
                String html = data.getString("html");
                html = html.replaceAll("TOKEN", extra.getString("token"));
                html = html.replaceAll("URL", URL);
                request.setBody(html);
                MimeMessage message = emailService.htmlMessage(request);
                JSONObject responseEmail = emailService.send(message);
                return ResponseEntity.ok(responseEmail.toMap());
            }else{
                return ResponseEntity.internalServerError().body(new JSONObject().put("message", response.getString("message")).put("status", false).toMap());
            }
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(new JSONObject().put("message", e.getMessage()).put("status", false).toMap());
        }
    }

}
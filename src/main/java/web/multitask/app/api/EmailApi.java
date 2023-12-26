package web.multitask.app.api;

import javax.mail.internet.MimeMessage;
import org.json.JSONObject;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RestController;
import web.multitask.app.model.EmailRequest;
import web.multitask.app.service.EmailService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;


@RestController
@RequestMapping("/email")

public class EmailApi {

    private final EmailService emailService;

    public EmailApi(EmailService emailService) {
        this.emailService = emailService;
    }

    @RequestMapping(path = "/public/simple",method= RequestMethod.POST, consumes = { MediaType.MULTIPART_FORM_DATA_VALUE })
    public String simple(@ModelAttribute EmailRequest request) {
        try {
            MimeMessage message = emailService.simpleMessage(request);
            JSONObject response = emailService.send(message);
            return response.toString();
        } catch (Exception e) {
            return new JSONObject().put("message", e.getMessage()).put("status", false).toString();
        }
    }

    @RequestMapping(path = "/private/full",method= RequestMethod.POST, consumes = { MediaType.MULTIPART_FORM_DATA_VALUE })
    public String full(@ModelAttribute EmailRequest request) {
        try {
            MimeMessage message = emailService.htmlMessage(request);
            JSONObject response = emailService.send(message);
            return response.toString();
        } catch (Exception e) {
            return new JSONObject().put("message", e.getMessage()).put("status", false).toString();
        }
    }

}
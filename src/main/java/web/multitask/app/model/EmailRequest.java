package web.multitask.app.model;

import org.json.JSONObject;
import org.springframework.web.multipart.MultipartFile;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class EmailRequest {
    private String to;
    private String cc;
    private String bcc;
    private String subject;
    private String body;
    private MultipartFile[] files;
    private JSONObject extra;
}
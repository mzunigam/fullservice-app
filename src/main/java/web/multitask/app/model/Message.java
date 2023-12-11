package web.multitask.app.model;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Getter
public class Message implements java.io.Serializable{
    private String user;
    private String content;
}
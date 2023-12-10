package web.multitask.app.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class Response implements java.io.Serializable{
    private String user;
    private String project;
    private String content;
    private String date;
}
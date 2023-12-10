package web.multitask.app.controller;

import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import web.multitask.app.model.Message;
import web.multitask.app.model.Response;

import java.util.Date;

@Controller
public class MessageController {

    @MessageMapping("/websocket/{project}/{user}")
    @SendTo("/topic/message/{project}/{user}")
    public Response envio(@PathVariable("project") String project, @PathVariable("user") String user,
            Message message) {
        return new Response(user, project,message.getContent(), new Date().toString());
    }

}
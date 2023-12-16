package web.multitask.app.controller;


import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import web.multitask.app.model.Message;
import web.multitask.app.model.Response;

import java.util.Date;

@Controller
public class MessageController {

    @MessageMapping("/websocket/{project}/{topic}")
    @SendTo("/topic/message/{project}/{topic}")
    public Response envio(@PathVariable("project") String project, @PathVariable("topic") String topic,
                          Message message) {
        return new Response(message.getUser(), project, topic, message.getContent(), new Date().toString());
    }

}
package web.multitask.app.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.messaging.MessageSecurityMetadataSourceRegistry;
import org.springframework.security.config.annotation.web.socket.AbstractSecurityWebSocketMessageBrokerConfigurer;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketAuthorizationSecurityConfig extends AbstractSecurityWebSocketMessageBrokerConfigurer {
    @Override
    protected void configureInbound(final MessageSecurityMetadataSourceRegistry messages) {
        //onConnect
        messages.simpMessageDestMatchers("/websocket").permitAll();
        //onSubscribe
        messages.simpSubscribeDestMatchers("/topic/**").authenticated();
    }

    @Override
    protected boolean sameOriginDisabled() {
        return true;
    }
}
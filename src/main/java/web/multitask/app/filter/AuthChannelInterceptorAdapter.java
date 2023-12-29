package web.multitask.app.filter;

import org.jetbrains.annotations.NotNull;
import org.json.JSONObject;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import web.multitask.app.repository.UserRespository;
import web.multitask.app.utils.JWTokenUtil;

import java.util.Objects;

@Component
public class AuthChannelInterceptorAdapter implements ChannelInterceptor {

    private final JWTokenUtil jwtTokenUtil;
    private final UserRespository userRepo;

    public AuthChannelInterceptorAdapter(JWTokenUtil jwtTokenUtil, UserRespository userRepo) {
        this.jwtTokenUtil = jwtTokenUtil;
        this.userRepo = userRepo;
    }

    @Override
    public Message<?> preSend(@NotNull final Message<?> message, @NotNull final MessageChannel channel) {
        final StompHeaderAccessor accessor = MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);

        assert accessor != null;
        if (StompCommand.CONNECT == accessor.getCommand()) {
//            final String authorization = Objects.requireNonNull(accessor.getHeader("nativeHeaders")).toString();
            LinkedMultiValueMap<String, String> map = (LinkedMultiValueMap<String, String>) accessor.getHeader("nativeHeaders");
            String authorization = Objects.requireNonNull(Objects.requireNonNull(map).get("Authorization")).get(0);
            assert authorization != null;
            final String token = authorization.split(" ")[1];
            if(token != null && jwtTokenUtil.validateToken(token)){
                JSONObject jsonToken = new JSONObject(jwtTokenUtil.getDataToken(token));
                UserDetails userDetails = userRepo.findByUsername(jsonToken.getString("username"));
                final UsernamePasswordAuthenticationToken user = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                accessor.setUser(user);
            }

        }
        return message;
    }
}
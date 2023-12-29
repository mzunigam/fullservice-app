package web.multitask.app.filter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.jetbrains.annotations.NotNull;
import org.json.JSONObject;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import io.jsonwebtoken.io.IOException;
import web.multitask.app.repository.UserRespository;
import web.multitask.app.utils.JWTokenUtil;

import java.util.Enumeration;

@Component
@Order(1)
public class JWTokenFilter extends OncePerRequestFilter {

    private JWTokenUtil jwtTokenUtil = null;

    private UserRespository userRepo = null;

    public JWTokenFilter(JWTokenUtil jwtTokenUtil, UserRespository userRepo) {
        this.jwtTokenUtil = jwtTokenUtil;
        this.userRepo = userRepo;
    }

    public JWTokenFilter() {
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, @NotNull HttpServletResponse response, @NotNull FilterChain chain)
            throws ServletException, IOException, java.io.IOException {
        response.setContentType("application/json");

//        final String queryAuthorization = request.getParameter(HttpHeaders.AUTHORIZATION);
        final String Authorization = request.getHeader(HttpHeaders.AUTHORIZATION);

        String token;

        try {
            token = Authorization.split(" ")[1];
        } catch (Exception e) {
            token = null;
        }

        if (token == null || token.isEmpty()) {
//            SecurityContextHolder.getContext().setAuthentication(null);
            chain.doFilter(request, response);
        } else {
            if (jwtTokenUtil.validateToken(token)) {
                JSONObject jsonToken = new JSONObject(jwtTokenUtil.getDataToken(token));
                UserDetails userDetails = userRepo.findByUsername(jsonToken.getString("username"));
                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                        userDetails,
                        null, userDetails.getAuthorities());
                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authentication);
                chain.doFilter(request, response);
            } else {
                response.sendError(401, "Invalid Token");
            }
        }
    }
}
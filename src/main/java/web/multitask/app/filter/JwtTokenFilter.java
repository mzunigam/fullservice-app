package web.multitask.app.filter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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
import web.multitask.app.utils.JwtTokenUtil;

@Component
@Order(1)
public class JwtTokenFilter extends OncePerRequestFilter {

    private JwtTokenUtil jwtTokenUtil = null;

    private UserRespository userRepo = null;

    public JwtTokenFilter(JwtTokenUtil jwtTokenUtil, UserRespository userRepo) {
        this.jwtTokenUtil = jwtTokenUtil;
        this.userRepo = userRepo;
    }

    public JwtTokenFilter() {
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws ServletException, IOException, java.io.IOException {

        final String header = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (request.getRequestURI().startsWith("/token")) {
            chain.doFilter(request, response);
        } else {
            if (header == null || !header.startsWith("Bearer ")) {
                response.sendError(403, "Access Denied");
                chain.doFilter(request, response);
            } else {
                String token = header.split(" ")[1];
                if (jwtTokenUtil.validateToken(token)) {
                    JSONObject jsonToken = new JSONObject(jwtTokenUtil.getDataToken(token));
                    UserDetails userDetails = userRepo.findByUsername(jsonToken.getString("username"));
                    UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                    authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                    response.setStatus(200);
                    chain.doFilter(request, response);
                } else {
                    response.sendError(401, "Invalid Token");
                    chain.doFilter(request, response);
                }
            }
        }

    }

}
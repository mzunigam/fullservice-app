package web.multitask.app.utils;

import java.io.Serializable;
import java.math.BigInteger;
import java.util.Date;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;
import web.multitask.app.model.User;
@Component
public class JWTokenUtil implements Serializable{

    @Value("${app.jwtSecret}")
    private String jwtSecret;

    public String generateToken(User user, BigInteger ms) {
        if(ms == null){
            ms = BigInteger.valueOf(3600000);
        }
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + ms.longValue());
        JSONObject json = new JSONObject();
        json.put("username", user.getUsername());
        return Jwts.builder()
                .setSubject(json.toString()) 
                .setIssuedAt(new Date())
                .setExpiration(expiryDate)
                .signWith(Keys.hmacShaKeyFor(jwtSecret.getBytes()))
                .compact();
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(Keys.hmacShaKeyFor(jwtSecret.getBytes()))
                    .build()
                    .parseClaimsJws(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public String getDataToken(String token) {
        try {
            return Jwts.parserBuilder()
                    .setSigningKey(Keys.hmacShaKeyFor(jwtSecret.getBytes()))
                    .build()
                    .parseClaimsJws(token)
                    .getBody().getSubject();
        }catch (Exception e){
            System.out.println(e.getMessage());
            return null;
        }
    }

    public boolean isTokenExpired(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(Keys.hmacShaKeyFor(jwtSecret.getBytes()))
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getExpiration()
                .before(new Date());
    }

}
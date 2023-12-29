package web.multitask.app.api;


import org.json.JSONObject;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import web.multitask.app.model.User;
import web.multitask.app.repository.UserRespository;
import web.multitask.app.utils.JWTokenUtil;
import java.math.BigInteger;
import java.util.Objects;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RestController
@RequestMapping("/token")
@CrossOrigin
class JWTokenApi {

    private final JWTokenUtil jwtTokenUtil;
    private final UserRespository userRepo;

    public JWTokenApi(JWTokenUtil jwtTokenUtil, UserRespository userRepo) {
        this.jwtTokenUtil = jwtTokenUtil;
        this.userRepo = userRepo;
    }

    @PostMapping("/authenticate")
    public ResponseEntity<?> createAuthenticationToken(@RequestBody String authenticationRequest) {
        JSONObject response;
        JSONObject json = new JSONObject(authenticationRequest);
        String username = json.getString("username");
        UserDetails userDetails = userRepo.findByUsername(username);
        if (!Objects.equals(userDetails.getPassword(), json.getString("password"))) {
            response = new JSONObject().put("token", "").put("message", "Invalid Credentials").put("status", false);
            return ResponseEntity.status(401).body(response.toMap());
        } else {
            return ResponseEntity.ok(new JSONObject().put("token", jwtTokenUtil.generateToken((User) userDetails, json.optBigInteger("ms", BigInteger.valueOf(3600000)))).put("message", "Generated").put("status", true).toMap());
        }
    }

    @PostMapping("/validate")
    public ResponseEntity<?> validateToken(@RequestBody String token) {
        JSONObject response;
        JSONObject json = new JSONObject(token);
        if (jwtTokenUtil.validateToken(json.getString("token"))) {
            String dataToken = jwtTokenUtil.getDataToken(json.getString("token"));
            if (dataToken == null) {
                response = new JSONObject().put("message", "Invalid Token").put("status", false);
            } else {
                boolean isTokenExpired = jwtTokenUtil.isTokenExpired(json.getString("token"));
                if (isTokenExpired) {
                    response = new JSONObject().put("message", "Expired Token").put("status", false);
                    return ResponseEntity.status(403).body(response.toMap());
                }
                try {
                    UserDetails userDetails = userRepo.findByUsername(new JSONObject(dataToken).getString("username"));
                    if (userDetails.getUsername() == null) {
                        response = new JSONObject().put("message", "Invalid Token").put("status", false);
                    } else {
                        response = new JSONObject().put("message", "Valid Token").put("status", true);
                    }
                } catch (Exception e) {
                    response = new JSONObject().put("message", "Invalid Token").put("status", false);
                }
            }
        } else {
            response = new JSONObject().put("message", "Invalid Token").put("status", false);
        }
        if (response.getBoolean("status")) {
            return ResponseEntity.ok(response.toMap());
        } else {
            return ResponseEntity.status(401).body(response.toMap());
        }
    }

    @PostMapping("/service/authenticate")
    public ResponseEntity<?> generateToken(@RequestBody String token) {
        JSONObject json = new JSONObject(token);
        UserDetails userDetails = userRepo.findByUsername(json.getString("username"));
        if (userDetails == null) {
            return ResponseEntity.status(401).body(new JSONObject().put("token", "").put("message", "Invalid Credentials").put("status", false).toMap());
        } else if (userDetails.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("SERVICE"))) {
            return ResponseEntity.ok(new JSONObject().put("token", jwtTokenUtil.generateToken((User) userDetails, json.optBigInteger("ms", BigInteger.valueOf(3600000)))).put("message", "Generated").put("status", true).toMap());
        } else {
            return ResponseEntity.status(401).body(new JSONObject().put("token", "").put("message", "Invalid Credentials").put("status", false).toMap());
        }
    }

}
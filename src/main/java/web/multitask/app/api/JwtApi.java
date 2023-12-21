package web.multitask.app.api;


import org.json.JSONObject;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import web.multitask.app.model.User;
import web.multitask.app.repository.UserRespository;
import web.multitask.app.utils.JwtTokenUtil;

import java.util.Objects;


@RestController
@RequestMapping("/token")
@CrossOrigin
class JwtApi {

    private final JwtTokenUtil jwtTokenUtil;
    private final UserRespository userRepo;
    public JwtApi(JwtTokenUtil jwtTokenUtil, UserRespository userRepo) {
        this.jwtTokenUtil = jwtTokenUtil;
        this.userRepo = userRepo;
    }

    @PostMapping("/authenticate")
    public String createAuthenticationToken(@RequestBody String authenticationRequest) {
        JSONObject json = new JSONObject(authenticationRequest);
        String username = json.getString("username");
        UserDetails userDetails = userRepo.findByUsername(username);
        if(!Objects.equals(userDetails.getPassword(), json.getString("password"))){
            return new JSONObject().put("token", "").put("message", "Invalid Credentials").put("status", false).toString();
        }else{
            return new JSONObject().put("token", jwtTokenUtil.generateToken((User) userDetails)).put("message", "Generated").put("status", true).toString();
        }
    }

}
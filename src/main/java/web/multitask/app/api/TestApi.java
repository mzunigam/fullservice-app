package web.multitask.app.api;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController 
@RequestMapping("test")
public class TestApi {
    
    @RequestMapping("/user")
    public String test() {
        return "user";
    }

    @RequestMapping("/admin")
    public String test2() {
        return "admin";
    }
}

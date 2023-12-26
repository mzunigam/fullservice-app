package web.multitask.app.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.github.cdimascio.dotenv.Dotenv;
import web.multitask.app.utils.DotEnvUtil;

@Configuration
public class DotEnvConfig {

    @Bean
    Dotenv getDotEnvPath() {
        return Dotenv.configure().directory(DotEnvUtil.getDotEnvPath("fullservice")).load();
    }
    
}

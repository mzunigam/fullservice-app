package web.multitask.app.config;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import web.multitask.app.filter.JWTokenFilter;

@Configuration
public class FilterConfig {

    @Bean
    FilterRegistrationBean<JWTokenFilter> filterRegistrationBean() {
        FilterRegistrationBean<JWTokenFilter> registrationBean = new FilterRegistrationBean<>();
        registrationBean.setFilter(new JWTokenFilter());
        registrationBean.addUrlPatterns("/**");
        registrationBean.setOrder(1);
        return registrationBean;
    }

}
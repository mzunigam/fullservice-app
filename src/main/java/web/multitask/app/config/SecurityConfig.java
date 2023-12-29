package web.multitask.app.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;
import web.multitask.app.filter.JWTokenFilter;
import web.multitask.app.repository.UserRespository;
import web.multitask.app.utils.JWTokenUtil;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final UserRespository userRepo;
    private final JWTokenUtil jwtTokenUtil;

    public SecurityConfig(UserRespository userRepo, JWTokenUtil jwtTokenUtil) {
        this.userRepo = userRepo;
        this.jwtTokenUtil = jwtTokenUtil;
    }

    @Bean
    AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration)
            throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userRepo::findByUsername);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.cors(AbstractHttpConfigurer::disable).csrf(AbstractHttpConfigurer::disable)
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
                .authorizeHttpRequests(
                        authorizeRequests -> authorizeRequests
                                .antMatchers("/security/**").hasAnyAuthority("ADMIN")
                                .regexMatchers(".*/private/.*").hasAnyAuthority("ADMIN","USER")
                                .regexMatchers(".*/public/.*").permitAll()
                                .regexMatchers(".*/service/.*").hasAnyAuthority("ADMIN","SERVICE")
                                .antMatchers(HttpMethod.GET, "/**").permitAll()
//                                .antMatchers(HttpMethod.POST, "/**").permitAll()
                                .antMatchers("/token/**").permitAll());
//                                .anyRequest()
//                                .authenticated());
        http.addFilterBefore(new JWTokenFilter(jwtTokenUtil, userRepo), UsernamePasswordAuthenticationFilter.class);
    }

    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    CorsFilter corsFilter() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration config = new CorsConfiguration();
//        config.setAllowCredentials(true);
        config.addAllowedOrigin("*");
        config.addAllowedHeader("*");
        config.addAllowedMethod("*");
        source.registerCorsConfiguration("/**", config);
        return new CorsFilter(source);
    }

}
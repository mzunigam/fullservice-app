package web.multitask.app.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import  web.multitask.app.model.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

public interface UserRespository extends JpaRepository<User, Long> {
    
    UserDetails findByUsername(String username)
            throws UsernameNotFoundException;

}
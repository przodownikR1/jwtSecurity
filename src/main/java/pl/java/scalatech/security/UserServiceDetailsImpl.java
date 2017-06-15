package pl.java.scalatech.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import lombok.extern.slf4j.Slf4j;
import pl.java.scalatech.annotation.SecurityComponent;
import pl.java.scalatech.repository.UserRepository;

@SecurityComponent
@Slf4j
public class UserServiceDetailsImpl implements UserDetailsService {
    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException, DataAccessException {
        UserSec loaded = new UserSec(
                userRepository.findByLogin(username).orElseThrow(() -> new UsernameNotFoundException(
                        "login.not.exitst")));
        log.info("userName {}  -> loadUserByUserName  : {}  ", username, loaded);
        return loaded;

    }

}
package pl.java.scalatech.populator;

import java.util.ArrayList;
import java.util.List;

import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;

import lombok.RequiredArgsConstructor;
import pl.java.scalatech.annotation.SecurityComponent;
import pl.java.scalatech.entity.Role;
import pl.java.scalatech.entity.User;
import pl.java.scalatech.repository.UserRepository;

@SecurityComponent
@RequiredArgsConstructor
public class SecurityPolulator implements CommandLineRunner {
    
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        Role userRole= new Role("user");
        List<Role> roles = new ArrayList<>();
        roles.add(userRole);
        User user = new User("slawek", passwordEncoder.encode("password"),true, roles);
        userRepository.save(user);

    }

}

package ar.com.tacsutn.grupo1.eventapp;

import ar.com.tacsutn.grupo1.eventapp.models.Authority;
import ar.com.tacsutn.grupo1.eventapp.models.AuthorityName;
import ar.com.tacsutn.grupo1.eventapp.models.User;
import ar.com.tacsutn.grupo1.eventapp.repositories.AuthorityRepository;
import ar.com.tacsutn.grupo1.eventapp.repositories.UserRepository;
import ar.com.tacsutn.grupo1.eventapp.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class DBSeeder implements CommandLineRunner {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private AuthorityRepository authorityRepository;
    @Autowired
    private UserService userService;

    public DBSeeder(UserRepository userRepository, AuthorityRepository authorityRepository) {
        this.userRepository = userRepository;
        this.authorityRepository = authorityRepository;
    }

    @Override
    public void run(String... args) {
        Authority authority1 = new Authority();
        authority1.setId("1");
        authority1.setName(AuthorityName.ROLE_USER);
        Authority authority2 = new Authority();
        authority2.setId("2");
        authority2.setName(AuthorityName.ROLE_ADMIN);
        List<Authority> userAuthorities = Collections.singletonList(authority1);
        List<Authority> adminAuthorities = Collections.singletonList(authority2);
        User adminUser = new User("admin", "admin", "admin", "admin", "admin@admin.com", true, new Date(), adminAuthorities);
        adminUser.setId("1");
        User userUser = new User("user", "user", "user", "user", "enabled@user.com", true, new Date(), userAuthorities);
        userUser.setId("2");
        this.userRepository.deleteAll();
        this.authorityRepository.deleteAll();
        List<Authority> authorities = Arrays.asList(authority1,authority2);
        this.authorityRepository.saveAll(authorities);
        userService.create(userUser);
        userService.createAdmin(adminUser);
    }
}

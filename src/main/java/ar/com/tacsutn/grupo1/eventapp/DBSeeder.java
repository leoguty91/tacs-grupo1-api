package ar.com.tacsutn.grupo1.eventapp;

import ar.com.tacsutn.grupo1.eventapp.models.Authority;
import ar.com.tacsutn.grupo1.eventapp.models.AuthorityName;
import ar.com.tacsutn.grupo1.eventapp.models.User;
import ar.com.tacsutn.grupo1.eventapp.repositories.AuthorityRepository;
import ar.com.tacsutn.grupo1.eventapp.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

@Component
public class DBSeeder implements CommandLineRunner {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private AuthorityRepository authorityRepository;

    public DBSeeder(UserRepository userRepository, AuthorityRepository authorityRepository) {
        this.userRepository = userRepository;
        this.authorityRepository = authorityRepository;
    }
    /*
    *

-- INSERT INTO USER (USERNAME, PASSWORD, FIRSTNAME, LASTNAME, EMAIL, ENABLED, LASTPASSWORDRESETDATE) VALUES ("admin', '$2a$08$lDnHPz7eUkSi6ao14Twuau08mzhWrL4kyZGGU5xfiGALO/Vxd5DOi', 'admin', 'admin', 'admin@admin.com', 1, PARSEDATETIME('01-01-2016', 'dd-MM-yyyy'));
-- INSERT INTO USER (USERNAME, PASSWORD, FIRSTNAME, LASTNAME, EMAIL, ENABLED, LASTPASSWORDRESETDATE) VALUES ('user', '$2a$08$UkVvwpULis18S19S5pZFn.YHPZt3oaqHZnDwqbCW9pft6uFtkXKDC', 'user', 'user', 'enabled@user.com', 1, PARSEDATETIME('01-01-2016','dd-MM-yyyy'));
-- INSERT INTO USER (USERNAME, PASSWORD, FIRSTNAME, LASTNAME, EMAIL, ENABLED, LASTPASSWORDRESETDATE) VALUES ('disabled', '$2a$08$UkVvwpULis18S19S5pZFn.YHPZt3oaqHZnDwqbCW9pft6uFtkXKDC', 'user', 'user', 'disabled@user.com', 0, PARSEDATETIME('01-01-2016','dd-MM-yyyy'));
--
        this.username = username;
        this.password = password;
        this.firstname = firstname;
        this.lastname = lastname;
        this.email = email;
        this.enabled = enabled;
        this.lastPasswordResetDate = lastPasswordResetDate;
        this.authorities = authorities;



-- INSERT INTO AUTHORITY (ID, NAME) VALUES (1, 'ROLE_USER');
-- INSERT INTO AUTHORITY (ID, NAME) VALUES (2, 'ROLE_ADMIN');
--COMO CATZO LO GUARDO
-- INSERT INTO USER_AUTHORITY (USER_ID, AUTHORITY_ID) VALUES (1, 1);
-- INSERT INTO USER_AUTHORITY (USER_ID, AUTHORITY_ID) VALUES (1, 2);
-- INSERT INTO USER_AUTHORITY (USER_ID, AUTHORITY_ID) VALUES (2, 1);
-- INSERT INTO USER_AUTHORITY (USER_ID, AUTHORITY_ID) VALUES (3, 1);



    * */

    @Override
    public void run(String... args) throws Exception {
        Authority authority1 = new Authority();
        authority1.setId("1");
        authority1.setName(AuthorityName.ROLE_USER);
        Authority authority2 = new Authority();
        authority2.setId("2");
        authority2.setName(AuthorityName.ROLE_ADMIN);
        User adminUser = new User("admin", "$2a$08$lDnHPz7eUkSi6ao14Twuau08mzhWrL4kyZGGU5xfiGALO/Vxd5DOi", "admin", "admin", "admin@admin.com", true, new Date(2016, 1, 1), Arrays.asList(authority1, authority2));
        adminUser.setId("1");
        adminUser.setTelegramUserId(100);
        User userUser = new User("user", "$2a$08$UkVvwpULis18S19S5pZFn.YHPZt3oaqHZnDwqbCW9pft6uFtkXKDC", "user", "user", "enabled@user.com", true, new Date(2016, 1, 1), Arrays.asList(authority1));
        userUser.setId("2");
        userUser.setTelegramUserId(101);
        User disabledUser = new User("disabled", "$2a$08$UkVvwpULis18S19S5pZFn.YHPZt3oaqHZnDwqbCW9pft6uFtkXKDC", "user", "user", "disabled@user.com", false, new Date(2016, 1, 1), Arrays.asList(authority1));
        disabledUser.setId("3");
        disabledUser.setTelegramUserId(102);
        this.userRepository.deleteAll();
        this.authorityRepository.deleteAll();
        List<Authority> authorities = Arrays.asList(authority1,authority2);
        this.authorityRepository.saveAll(authorities);
        List<User> users = Arrays.asList(adminUser, userUser, disabledUser);
        this.userRepository.saveAll(users);
    }
}

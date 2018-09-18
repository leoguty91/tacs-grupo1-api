package ar.com.tacsutn.grupo1.eventapp;

import ar.com.tacsutn.grupo1.eventapp.models.*;
import ar.com.tacsutn.grupo1.eventapp.repositories.AuthorityRepository;
import ar.com.tacsutn.grupo1.eventapp.repositories.EventRepository;
import ar.com.tacsutn.grupo1.eventapp.services.EventListService;
import ar.com.tacsutn.grupo1.eventapp.services.UserService;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class BootstrapData implements InitializingBean {

    private final UserService userService;
    private final EventListService listService;
    private final EventRepository eventRepository;
    private final AuthorityRepository authorityRepository;

    @Autowired
    private BootstrapData(
            UserService userService,
            EventListService listService,
            EventRepository eventRepository,
            AuthorityRepository authorityRepository) {

        this.userService = userService;
        this.listService = listService;
        this.eventRepository = eventRepository;
        this.authorityRepository = authorityRepository;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        Authority adminAuthority = new Authority();
        adminAuthority.setName(AuthorityName.ROLE_ADMIN);
        authorityRepository.save(adminAuthority);

        List<Authority> adminAuthorities = Collections.singletonList(adminAuthority);

        Authority userAuthority = new Authority();
        userAuthority.setName(AuthorityName.ROLE_USER);
        authorityRepository.save(userAuthority);

        List<Authority> userAuthorities = Collections.singletonList(userAuthority);

        User admin = new User(
            "admin",
            "admin",
            "admin",
            "admin",
            "admin@admin.com",
            true,
            new Date(),
            adminAuthorities
        );

        User user = new User(
            "user",
            "user",
            "user",
            "user",
            "user@user.com",
            true,
            new Date(),
            userAuthorities
        );

        userService.createAdmin(admin);
        userService.create(user);

        EventId eventId1 = new EventId("EventId1");
        eventRepository.save(eventId1);

        EventList list1 = new EventList("list1", admin);
        list1.setEvents(Collections.singletonList(eventId1));
        listService.save(list1);

        EventList list2 = new EventList("list2", admin);
        list2.setEvents(Collections.singletonList(eventId1));
        listService.save(list2);

        EventList list3 = new EventList("list3", user);
        list3.setEvents(Collections.singletonList(eventId1));
        listService.save(list3);

        EventList list4 = new EventList("list4", user);
        list4.setEvents(Collections.singletonList(eventId1));
        listService.save(list4);



    }
}

package ar.com.tacsutn.grupo1.eventapp.services;

//import ar.com.tacsutn.grupo1.eventapp.BootstrapData;
import ar.com.tacsutn.grupo1.eventapp.models.EventId;
import ar.com.tacsutn.grupo1.eventapp.models.EventList;
import ar.com.tacsutn.grupo1.eventapp.models.User;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

@RunWith(SpringRunner.class)
@SpringBootTest
public class EventServiceTest {
//    @MockBean
//    private BootstrapData bootstrapData;

    @Mock
    private User user1;

    @Autowired
    private EventService eventService;

    private EventId event1, event2, event3;

    private EventList eventList;

    @Before
    public void before() {
        setEvents();
        setEventList();
    }

    @Test
    public void canGetById() {
        EventId actualEvent = eventService.getById(event1.getId()).orElseThrow(NoSuchElementException::new);

        assertEquals(event1, actualEvent);
    }

    @Test
    public void shouldNotGetEventIfNotExists() {
        assertFalse(eventService.getById("4").isPresent());
    }

    @Test
    public void canDeleteEventFromList() {
        eventService.removeEvent(eventList, event1);

        assertArrayEquals(new EventId[]{}, eventList.getEvents().toArray());
    }

    @Test
    public void shouldNotDeleteEventIfNotExists() {
        eventService.removeEvent(eventList, event2);

        assertEquals(1, eventList.getEvents().size());
    }

    @Ignore
    @Test
    public void canFindAllEventsBetweenDates() {
        addAllEvents();

        Date from = dateGeneratorFromToday(-10000);
        Date to = dateGeneratorFromToday(1000);

        long eventsCount = eventService.getTotalEventsBetween(from, to);

        assertEquals(4, eventsCount);
    }

    @Test
    public void shouldNotFindEventsIfNotInRange() {
        addAllEvents();

        Date from = dateGeneratorFromToday(100);
        Date to = dateGeneratorFromToday(1000);

        long eventsCount = eventService.getTotalEventsBetween(from, to);

        assertEquals(0, eventsCount);
    }

    private void setEvents() {
        event1 = new EventId("1");
        event2 = new EventId("2");
        event3 = new EventId("3");

        eventService.save(event1);
    }

    private void setEventList() {
        List<EventId> events = new ArrayList<>();

        events.add(event1);

        eventList = new EventList("Test", user1);

        eventList.setEvents(events);
    }

    private void addAllEvents() {
        eventService.save(event2);
        eventService.save(event3);
    }

    private Date dateGeneratorFromToday(long days) {
        return new Date(System.currentTimeMillis() + TimeUnit.DAYS.toMillis(days));
    }
}

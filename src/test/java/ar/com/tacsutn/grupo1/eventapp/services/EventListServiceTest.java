package ar.com.tacsutn.grupo1.eventapp.services;

//import ar.com.tacsutn.grupo1.eventapp.BootstrapData;
import ar.com.tacsutn.grupo1.eventapp.client.EventbriteClient;
import ar.com.tacsutn.grupo1.eventapp.models.Event;
import ar.com.tacsutn.grupo1.eventapp.models.EventId;
import ar.com.tacsutn.grupo1.eventapp.models.EventList;
import ar.com.tacsutn.grupo1.eventapp.models.User;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

import static org.junit.Assert.*;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.anything;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

@RunWith(SpringRunner.class)
@SpringBootTest
public class EventListServiceTest {
//    @MockBean
//    private BootstrapData bootstrapData;

    @Autowired
    private UserService userService;

    private User user1, user2;

    @Autowired
    private EventService eventService;

    private EventId event1, event2;

    @Autowired
    private EventListService listService;

    private EventList eventList1, eventList2, eventList3;

    @Autowired
    private EventbriteClient eventbriteClient;


    @Before
    public void before() {
        setUsers();
        setEvents();
        setLists();
    }

    @Test
    public void canGetEventsList() {
        EventList result = listService.getById(eventList1.getId()).orElseThrow(NoSuchElementException::new);

        assertEquals(eventList1.getId(), result.getId());
    }

//    @Test
//    public void shouldNotGetEventsListIfNotExists() {
//        assertFalse(listService.getById(-500L).isPresent());
//    }

    @Test
    @Ignore
    public void canGetAllEventLists() {
        listService.getLists().getContent().iterator().forEachRemaining(e -> System.out.println(e.getName()));
        assertArrayEquals(new EventList[]{eventList1, eventList3}, listService.getLists().getContent().toArray());
    }

    @Test
    public void canGetAllEventListsForAUser() {
        EventList[] eventsA= new EventList[]{eventList1};
        EventList[] eventsB =    listService.getListsByUser(user1).getContent().toArray(eventsA);
        assertEquals(eventsA.length,eventsB.length);
        assertEquals(eventsA[0].getId(),eventsB[0].getId());
    }

    @Test
    public void canAddEventList() {
        listService.save(eventList2);

        assertEquals(2L, eventsCount(user1));
    }

    @Test
    public void shouldNotAddEventListIfExists() {
        listService.save(eventList1);

        assertEquals(1L, eventsCount(user1));
    }

    @Test
    public void canDeleteEventList() {
        listService.delete(user1, eventList1.getId());

        assertEquals(0L, eventsCount(user1));
    }
//
//    @Test(expected = NoSuchElementException.class)
//    public void shouldNotDeleteListIfNotExists() {
//        listService.delete(user1, -2000L);
//    }

    @Test(expected = NoSuchElementException.class)
    public void shouldNotDeleteListIfNotOwner() {
        listService.delete(user1, eventList3.getId());
    }

    @Test
    public void canChangeEventListName() {
        listService.rename(user1, eventList1.getId(), "TestRename");

        EventList renamedList = listService.getById(eventList1.getId()).orElseThrow(NoSuchElementException::new);

        assertEquals("TestRename", renamedList.getName());
    }

//    @Test(expected = NoSuchElementException.class)
//    public void shouldNotChangeListNameIfNotExists() {
//        listService.rename(user1, -2000L, "TestRename");
//    }

    @Test(expected = NoSuchElementException.class)
    public void shouldNotChangeListNameIfNotOwner() {
        listService.rename(user1, eventList3.getId(), "TestRename");
    }

    @Test
    public void canGetCommonEventsFromTwoDifferentEventList() throws IOException {
        mockEvent();

        List<Event> commonEvents = getCommonEvents(eventList1, eventList3);

        assertEquals(event1.getId(), commonEvents.get(0).getId());
    }

    @Test
    public void gettingCommonEventsIsAssociative() throws IOException {
        mockEvent();

        List<Event> commonEvents = getCommonEvents(eventList3, eventList1);

        assertEquals(event1.getId(), commonEvents.get(0).getId());
    }

    @Test
    public void canGetCommonEventsEqualToAllIfSameEventList() throws IOException {
        mockEvent();

        List<Event> commonEvents = getCommonEvents(eventList1, eventList1);

        assertEquals(eventList1.getEvents().size(), commonEvents.size());
    }

    @Test
    public void shouldNotFindCommonEventsIfThereAreNoCommonEvents() {
        listService.save(eventList2);

        List<Event> commonEvents = getCommonEvents(eventList1, eventList2);

        assertTrue(commonEvents.isEmpty());
    }

//    @Test(expected = NoSuchElementException.class)
//    public void shouldNotFindCommonEventsIfFirstIdDoesNotExist() {
//        getCommonEvents(-2000L, eventList1.getId());
//    }
//
//    @Test(expected = NoSuchElementException.class)
//    public void shouldNotFindCommonEventsIfSecondIdDoesNotExist() {
//        getCommonEvents(eventList1.getId(), -4000L);
//    }
//
//    @Test(expected = NoSuchElementException.class)
//    public void shouldNotFindCommonEventsIfNeitherIdsDoesNotExist() {
//        getCommonEvents(-2000L, -4000L);
//    }
//
//    @Test
//    public void canFindUserCountInterestedInEvent() {
//        int interestedCount = eventService.getTotalUsersByEventId("0");
//
//        assertEquals(2, interestedCount);
//    }
//
//    @Test
//    public void shouldBe0IfNoUsersAreInterestedInEvent() {
//        int interestedCount = eventService.getTotalUsersByEventId("foo");
//
//        assertEquals(0, interestedCount);
//    }

    private void setUsers() {
        user1 = new User("JohnDoemann1", "1234", "John", "Doemann", "john.doemann@test.com", true, new Date(), null);
        user2 = new User("JanetDoemann2", "1234", "Janet", "Doemann", "janet.doemann@test.com", true, new Date(), null);

        userService.save(user1);
        userService.save(user2);
    }

    private void setEvents() {
        event1 = new EventId("0");
        event2 = new EventId("1");

        eventService.save(event1);
        eventService.save(event2);
    }

    private void setLists() {
        eventList1 = new EventList("Test1", user1);
        eventList2 = new EventList("Test2", user1);
        eventList3 = new EventList("Test3", user2);

        List<EventId> list1 = new ArrayList<>(), list2 = new ArrayList<>(), list3 = new ArrayList<>();

        list1.add(event1);
        list2.add(event2);
        list3.add(event1);

        eventList1.setEvents(list1);
        eventList2.setEvents(list2);
        eventList3.setEvents(list3);

        listService.save(eventList1);
        listService.save(eventList3);
    }

    private long eventsCount(User user) {
        return listService.getTotalEventListByUserId(user.getId());
    }

    private List<Event> getCommonEvents(EventList eventList1, EventList eventList2) {
        return listService.getCommonEvents(eventList1.getId(), eventList2.getId(), PageRequest.of(0, 50)).getContent();
    }

    private List<Event> getCommonEvents(String id1, String id2) {
        return listService.getCommonEvents(id1, id2, PageRequest.of(0, 50)).getContent();
    }

    private void mockEvent() throws IOException {
        RestTemplate restTemplate = eventbriteClient.getRestTemplate();
        MockRestServiceServer mockServer = MockRestServiceServer.bindTo(restTemplate).build();

        mockServer.expect(anything())
                .andExpect(method(HttpMethod.GET))
                .andRespond(withSuccess(getJson(), MediaType.APPLICATION_JSON));
    }

    private byte[] getJson() throws IOException {
        String jsonPath = getPath();
        return Files.readAllBytes(new File(jsonPath).toPath());
    }

    private String getPath() {
        String filename = "sample-event-response.json";
        String[] arr = this.getClass().getName().split("\\.");
        arr[arr.length - 1] = filename;

        Path testPath = Paths.get("src", "test", "java");
        Path path = Paths.get(testPath.toString(), arr);

        return path.toString();
    }
}

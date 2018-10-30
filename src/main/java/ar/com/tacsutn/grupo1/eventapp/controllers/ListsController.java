package ar.com.tacsutn.grupo1.eventapp.controllers;

import ar.com.tacsutn.grupo1.eventapp.client.EventbriteClient;
import ar.com.tacsutn.grupo1.eventapp.models.*;
import ar.com.tacsutn.grupo1.eventapp.services.EventListService;
import ar.com.tacsutn.grupo1.eventapp.services.EventService;
import ar.com.tacsutn.grupo1.eventapp.services.SessionService;
import ar.com.tacsutn.grupo1.eventapp.swagger.ApiPageable;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;


@RestController
@RequestMapping("/api/v1")
@Api(tags = "Lists", description = "list resources")
public class ListsController {

    private final EventListService eventListService;
    private final SessionService sessionService;
    private final EventService eventService;
    private final EventbriteClient eventbriteClient;

    @Autowired
    public ListsController(
            EventListService eventListService,
            SessionService sessionService,
            EventService eventService,
            EventbriteClient eventbriteClient) {

        this.eventListService = eventListService;
        this.sessionService = sessionService;
        this.eventService = eventService;
        this.eventbriteClient = eventbriteClient;
    }

    @GetMapping("/lists/{list_id}")
    @PreAuthorize("hasRole('USER')")
    public EventList get(@PathVariable String list_id, HttpServletRequest request) {
        User user = sessionService.getAuthenticatedUser(request);
        return eventListService.getById(user, list_id).orElseThrow(() ->
            new ResourceNotFoundException("List not found.")
        );
    }

    @PostMapping("/lists")
    @PreAuthorize("hasRole('USER')")
    public EventList create(
            @RequestBody EventList eventList,
            HttpServletRequest request) {

        User user = sessionService.getAuthenticatedUser(request);
        return eventListService.save(user, eventList);
    }

    @PutMapping("/lists/{list_id}")
    @PreAuthorize("hasRole('USER')")
    public EventList update(
            @RequestBody EventList eventList,
            HttpServletRequest request) {

        User user = sessionService.getAuthenticatedUser(request);
        EventList list = eventListService
                .getById(user, eventList.getId())
                .orElseThrow(() -> new ResourceNotFoundException("List not found."));

        list.setName(eventList.getName());
        return eventListService.save(list);
    }

    @DeleteMapping("/lists/{list_id}")
    @PreAuthorize("hasRole('USER')")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(
            @PathVariable String list_id,
            HttpServletRequest request) {

        User user = sessionService.getAuthenticatedUser(request);

        try {
            eventListService.delete(user, list_id);
        } catch (NoSuchElementException e) {
            throw new ResourceNotFoundException("List not found.");
        }
    }

    /**
     * Returns all lists of a user.
     */
    @GetMapping("/lists")
    @PreAuthorize("hasRole('USER')")
    @ApiPageable
    public RestPage<EventList> getAll(
            @ApiIgnore Pageable pageable,
            HttpServletRequest request) {

        User user = sessionService.getAuthenticatedUser(request);
        Page<EventList> list = eventListService.getListsByUser(user, pageable);
        return new RestPage<>(list);
    }

    /**
     * Returns all events in a list.
     * @param list_id event list identifier.
     */
    @GetMapping("/lists/{list_id}/events")
    @PreAuthorize("hasRole('USER')")
    @ApiPageable
    public RestPage<Event> getEvents(
            @PathVariable String list_id,
            @ApiIgnore Pageable pageable,
            HttpServletRequest request) {

        User user = sessionService.getAuthenticatedUser(request);
         Optional<EventList> eventList = eventListService.getById(user, list_id);
         Page<EventId> eventIdPage = eventListService.getListEvents(eventList, pageable);

        List<Event> list = eventIdPage.getContent()
            .parallelStream()
            .flatMap(event -> eventbriteClient.getEvent(event.getId())
                .map(Stream::of)
                .orElseGet(Stream::empty))
            .collect(Collectors.toList());

        Page<Event> eventPage = new PageImpl<>(list, pageable, eventIdPage.getTotalElements());

        return new RestPage<>(eventPage);
    }

    @PostMapping("/lists/{list_id}/events")
    @PreAuthorize("hasRole('USER')")
    public EventList addEvent(
            @PathVariable String list_id,
            @RequestBody EventId eventId,
            HttpServletRequest request) {

        User user = sessionService.getAuthenticatedUser(request);
        EventList eventList = eventListService.getById(user, list_id)
                .orElseThrow(() -> new ResourceNotFoundException("List not found."));
        eventService.save(eventId);
        eventList.getEvents().add(eventId);
        return eventListService.save(eventList);
    }

    @DeleteMapping("/lists/{list_id}/events/{event_id}")
    @PreAuthorize("hasRole('USER')")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void removeEvent(@PathVariable String list_id,
                            @PathVariable String event_id,
                            HttpServletRequest request) {

        User user = sessionService.getAuthenticatedUser(request);
        EventList eventList = eventListService.getById(user, list_id)
                .orElseThrow(() -> new ResourceNotFoundException("List not found."));
        EventId event = eventService.getById(event_id)
                .orElseThrow(() -> new ResourceNotFoundException("Event not found."));
        eventService.removeEvent(eventList, event);
        eventListService.save(eventList);
    }
}

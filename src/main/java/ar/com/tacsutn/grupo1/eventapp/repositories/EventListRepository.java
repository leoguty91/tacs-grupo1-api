package ar.com.tacsutn.grupo1.eventapp.repositories;

import ar.com.tacsutn.grupo1.eventapp.models.Event;
import ar.com.tacsutn.grupo1.eventapp.models.EventId;
import ar.com.tacsutn.grupo1.eventapp.models.EventList;
import ar.com.tacsutn.grupo1.eventapp.models.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface EventListRepository extends MongoRepository<EventList, String> {

    Page<EventList> findAll(Pageable pageable);

    Page<EventList> findAllByUser(User user, Pageable pageable);

    long countAllByUserId(String user_id);

    List<EventList> findAllByEvents(EventId eventId);
}

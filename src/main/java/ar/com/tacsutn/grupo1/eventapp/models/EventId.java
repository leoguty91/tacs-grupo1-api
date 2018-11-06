package ar.com.tacsutn.grupo1.eventapp.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;
import java.util.List;

@Document
public class EventId {

    @Id
    private String id;

    @JsonIgnore
    private List<EventList> eventLists;

    @CreatedDate
    private Date createdTime;

    public EventId() {

    }

    public EventId(String id) {
        this.id = id;
        this.createdTime = new Date();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<EventList> getEventLists() {
        return eventLists;
    }

    public void setEventLists(List<EventList> eventLists) {
        this.eventLists = eventLists;
    }


    public Date getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(Date createdTime) {
        this.createdTime = createdTime;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof EventId) {
            return id.equals(((EventId) obj).id);
        }
        return super.equals(obj);
    }
}

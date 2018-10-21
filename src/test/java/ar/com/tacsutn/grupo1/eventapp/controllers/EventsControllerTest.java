package ar.com.tacsutn.grupo1.eventapp.controllers;

import org.junit.Ignore;
import org.junit.Test;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class EventsControllerTest extends ControllerTest {
    @WithMockUser(roles = "USER")
    @Transactional
    @DirtiesContext
    @Test
    public void canGetEventsWithAddress() throws Exception {
        this.getMockMvc()
                .perform(get("/api/v1/events/?address=argentina"))
                .andExpect(status().isOk());
    }

    @WithMockUser(roles = "USER")
    @Transactional
    @DirtiesContext
    @Test
    public void canGetEventsWithFrom() throws Exception {
        this.getMockMvc()
                .perform(get("/api/v1/events/?from=2018-09-25T18:00:00"))
                .andExpect(status().isOk());
    }

    @WithMockUser(roles = "USER")
    @Transactional
    @DirtiesContext
    @Test
    public void canGetEventsWithPrice() throws Exception {
        this.getMockMvc()
                .perform(get("/api/v1/events/?price=100"))
                .andExpect(status().isOk());
    }

    @WithMockUser(roles = "USER")
    @Transactional
    @DirtiesContext
    @Test
    public void canGetEventsWithQ() throws Exception {
        this.getMockMvc()
                .perform(get("/api/v1/events/?q=something"))
                .andExpect(status().isOk());
    }

    @WithMockUser(roles = "USER")
    @Transactional
    @DirtiesContext
    @Test
    public void canGetEventsWithTo() throws Exception {
        this.getMockMvc()
                .perform(get("/api/v1/events/?to=2018-12-16T00:00:00"))
                .andExpect(status().isOk());
    }

    @WithMockUser(roles = "USER")
    @Transactional
    @DirtiesContext
    @Test
    public void canGetEventsWithUnknownFilterFromAPI() throws Exception {
        this.getMockMvc()
                .perform(get("/api/v1/events/?categories=entretenimiento"))
                .andExpect(status().isOk());
    }

    @WithMockUser(roles = "USER")
    @Transactional
    @DirtiesContext
    @Test
    public void canGetEventsWithNonExistentFilter() throws Exception {
        this.getMockMvc()
                .perform(get("/api/v1/events/?shouldnotwork=shouldnotwork"))
                .andExpect(status().isOk());
    }

    @WithMockUser(roles = "ADMIN")
    @Transactional
    @DirtiesContext
    @Test
    public void canGetTotalUsers() throws Exception {
        this.getMockMvc()
                .perform(get("/api/v1/events/0/total_users"))
                .andExpect(status().isOk());
    }

    @Ignore
    @WithMockUser(roles = "USER")
    @Transactional
    @DirtiesContext
    @Test
    public void shouldNotGetTotalUsersIfNotAdmin() throws Exception {
        this.getMockMvc()
                .perform(get("/api/v1/events/0/total_users"))
                .andExpect(status().isForbidden());
    }

    @Ignore
    @WithMockUser(roles = "ADMIN")
    @Transactional
    @DirtiesContext
    @Test
    public void shouldNotGetTotalUsersIfEventDoesNotExist() throws Exception {
        this.getMockMvc()
                .perform(get("/api/v1/events/8000/total_users"))
                .andExpect(status().isNotFound());
    }

    @WithMockUser(roles = "ADMIN")
    @Transactional
    @DirtiesContext
    @Test
    public void canGetTotalEventsFromADateRange() throws Exception {
        this.getMockMvc()
                .perform(get("/api/v1/events/total_events?from=1970-01-01T00:00:00&to=2070-01-01T00:00:00"))
                .andExpect(status().isOk());
    }

    @WithMockUser(roles = "ADMIN")
    @Transactional
    @DirtiesContext
    @Test
    public void canGetTotalEventsFromBeginningOfTimes() throws Exception {
        this.getMockMvc()
                .perform(get("/api/v1/events/total_events"))
                .andExpect(status().isOk());
    }

    @WithMockUser(roles = "USER")
    @Transactional
    @DirtiesContext
    @Test
    public void shouldNotGetTotalEventsIfNotAdmin() throws Exception {
        this.getMockMvc()
                .perform(get("/api/v1/events/total_events"))
                .andExpect(status().isForbidden());
    }
}

package ar.com.tacsutn.grupo1.eventapp.controllers;

import org.junit.Test;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.annotation.DirtiesContext;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class AlarmsControllerTest extends ControllerTest {
    @WithMockUser(roles = "USER")
    @DirtiesContext
    @Test
    public void canPostAlarm() throws Exception {
        this.getMockMvc()
                .perform(post("/api/v1/alarms")
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .content("{ \"address\": \"argentina\", \"keyword\": \"x\", \"name\": \"x\", \"price\": \"100\", \"startDateFrom\": \"2018-10-08T22:54:28.201Z\", \"startDateTo\": \"2018-10-08T22:54:28.201Z\" }"))
                .andExpect(status().isOk());
    }

    @WithMockUser(roles = "USER")
    @DirtiesContext
    @Test
    public void canGetAlarm() throws Exception {
        this.getMockMvc()
                .perform(get("/api/v1/alarms/1").accept(MediaType.parseMediaType("application/json;charset=UTF-8")))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("alarm1"))
                .andExpect(jsonPath("$.id").value("1"));

    }

    @WithMockUser(roles = "USER")
    @DirtiesContext
    @Test
    public void shouldNotGetAlarmIfNotExists() throws Exception {
        this.getMockMvc()
                .perform(get("/api/v1/alarms/1000"))
                .andExpect(status().isNotFound());
    }

    @WithMockUser(roles = "USER")
    @DirtiesContext
    @Test
    public void canPutAlarm() throws Exception {
        this.getMockMvc()
                .perform(put("/api/v1/alarms/1")
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .content("{ \"address\": \"argentina\", \"keyword\": \"x\", \"name\": \"x\", \"price\": \"100\", \"startDateFrom\": \"2018-10-08T22:54:28.201Z\", \"startDateTo\": \"2018-10-08T22:54:28.201Z\" }"))
                .andExpect(status().isOk());
    }

    @WithMockUser(roles = "USER")
    @DirtiesContext
    @Test
    public void shouldNotPutAlarmIfNotExists() throws Exception {
        this.getMockMvc()
                .perform(put("/api/v1/alarms/1000")
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .content("{ \"address\": \"argentina\", \"keyword\": \"x\", \"name\": \"x\", \"price\": \"100\", \"startDateFrom\": \"2018-10-08T22:54:28.201Z\", \"startDateTo\": \"2018-10-08T22:54:28.201Z\" }"))
                .andExpect(status().isNotFound());
    }

    @WithMockUser(roles = "USER")
    @DirtiesContext
    @Test
    public void canDeleteAlarm() throws Exception {
        this.getMockMvc()
                .perform(delete("/api/v1/alarms/1"))
                .andExpect(status().isNoContent());
    }

    @WithMockUser(roles = "USER")
    @DirtiesContext
    @Test
    public void shouldNotDeleteAlarmIfNotExists() throws Exception {
        this.getMockMvc()
                .perform(delete("/api/v1/alarms/1000"))
                .andExpect(status().isNotFound());
    }

    @WithMockUser(roles = "USER")
    @DirtiesContext
    @Test
    public void canFetchAlarmPagedEvents() throws Exception {
        this.getMockMvc()
                .perform(get("/api/v1/alarms/1/fetch"))
                .andExpect(status().isOk());
    }

    @WithMockUser(roles = "USER")
    @DirtiesContext
    @Test
    public void canFetchAlarmEvents() throws Exception {
        this.getMockMvc()
                .perform(get("/api/v1/alarms/1/fetch?page=1"))
                .andExpect(status().isOk());
    }

    @WithMockUser(roles = "USER")
    @DirtiesContext
    @Test
    public void shouldNotFetchAlarmIfDoesNotExist() throws Exception {
        this.getMockMvc()
                .perform(get("/api/v1/alarms/10000/fetch"))
                .andExpect(status().isNotFound());
    }

    @WithMockUser(roles = "USER")
    @DirtiesContext
    @Test
    public void shouldNotFetchAlarmEventsIfPageDoesNotExist() throws Exception {
        this.getMockMvc()
                .perform(get("/api/v1/alarms/1/fetch/?page=100000000"))
                .andExpect(status().isNotFound());

    }

    @WithMockUser(roles = "USER")
    @DirtiesContext
    @Test
    public void canGetTodayAlarms() throws Exception {
        this.getMockMvc()
                .perform(get("/api/v1/alarms/today"))
                .andExpect(status().isOk());
    }

    @WithMockUser(roles = "USER")
    @DirtiesContext
    @Test
    public void canGetTodayAlarmsSortedDesc() throws Exception {
        this.getMockMvc()
                .perform(get("/api/v1/alarms/today/?sort=desc"))
                .andExpect(status().isOk());
    }

    @WithMockUser(roles = "USER")
    @DirtiesContext
    @Test
    public void canGetTodayAlarmsSortedAsc() throws Exception {
        this.getMockMvc()
                .perform(get("/api/v1/alarms/today/?sort=asc"))
                .andExpect(status().isOk());
    }
}

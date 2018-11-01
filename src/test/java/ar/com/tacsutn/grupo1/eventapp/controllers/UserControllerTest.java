package ar.com.tacsutn.grupo1.eventapp.controllers;

import org.junit.Test;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.annotation.DirtiesContext;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.hamcrest.Matchers.is;

public class UserControllerTest extends ControllerTest {
  @WithMockUser(roles = "USER")
  @DirtiesContext
  @Test
  public void canPostUser() throws Exception {
    this.getMockMvc()
            .perform(post("/api/v1/users")
                    .contentType(MediaType.APPLICATION_JSON_UTF8)
                    .content("{\"email\": \"a@a.com\", \"firstname\": \"Nombre5\", \"lastAccess\": \"2018-10-08T01:03:50.801Z\", \"lastname\": \"Apellido5\", \"username\": \"user5\", \"password\": \"123456\"}"))
            .andExpect(status().isOk());
  }

  @WithMockUser(roles = "USER")
  @DirtiesContext
  @Test
  public void shouldNotPostExistingUser() throws Exception {
    this.getMockMvc()
            .perform(post("/api/v1/users")
                    .contentType(MediaType.APPLICATION_JSON_UTF8)
                    .content("{\"email\": \"a@a.com\", \"firstname\": \"Nombre5\", \"lastAccess\": \"2018-10-08T01:03:50.801Z\", \"lastname\": \"Apellido5\", \"username\": \"JohnDoemann1\", \"password\": \"123456\"}"))
            .andExpect(status().is4xxClientError());
  }

  @WithMockUser(roles = "ADMIN")
  @DirtiesContext
  @Test
  public void canGetUser() throws Exception {
    this.getMockMvc()
            .perform(get("/api/v1/users/1"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.username").value("JohnDoemann1"))
            .andExpect(jsonPath("$.id").value("1"))
            .andExpect(jsonPath("$.firstname").value("John"))
            .andExpect(jsonPath("$.lastname").value("Doemann"));

  }

  @WithMockUser(roles = "USER")
  @DirtiesContext
  @Test
  public void shouldNotGetUserIfNotAdmin() throws Exception {
    this.getMockMvc()
            .perform(get("/api/v1/users/1"))
            .andExpect(status().isForbidden());

  }

  @WithMockUser(roles = "ADMIN")
  @DirtiesContext
  @Test
  public void shouldNotGetUserIfDoesNotExist() throws Exception {
    this.getMockMvc()
            .perform(get("/api/v1/users/500"))
            .andExpect(status().isNotFound());

  }

  @WithMockUser(roles = "ADMIN")
  @DirtiesContext
  @Test
  public void canGetTotalAlarms() throws Exception {
    this.getMockMvc()
            .perform(get("/api/v1/users/2/total_alarms"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.totalAlarms", is(1)));
  }

  @WithMockUser(roles = "USER")
  @DirtiesContext
  @Test
  public void shouldNotGetTotalAlarmsIfNotAdmin() throws Exception {
    this.getMockMvc()
            .perform(get("/api/v1/users/2/total_alarms"))
            .andExpect(status().isForbidden());

  }

  @WithMockUser(roles = "ADMIN")
  @DirtiesContext
  @Test
  public void shouldNotGetTotalAlarmsIfUserDoesNotExist() throws Exception {
    this.getMockMvc()
            .perform(get("/api/v1/users/500/total_alarms"))
            .andExpect(status().isNotFound());

  }

  @WithMockUser(roles = "ADMIN")
  @DirtiesContext
  @Test
  public void canGetTotalEvents() throws Exception {
    this.getMockMvc()
            .perform(get("/api/v1/users/2/total_lists"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.totalLists").value("1"));
  }

  @WithMockUser(roles = "USER")
  @DirtiesContext
  @Test
  public void shouldNotGetTotalEventsIfNotAdmin() throws Exception {
    this.getMockMvc()
            .perform(get("/api/v1/users/1/total_lists"))
            .andExpect(status().isForbidden());
  }

  @WithMockUser(roles = "ADMIN")
  @DirtiesContext
  @Test
  public void shouldNotGetTotalEventsIfUserDoesNotExist() throws Exception {
    this.getMockMvc()
            .perform(get("/api/v1/users/500/total_lists"))
            .andExpect(status().isNotFound());
  }
}

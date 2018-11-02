package ar.com.tacsutn.grupo1.eventapp.controllers;

import ar.com.tacsutn.grupo1.eventapp.EventAppApplication;
import ar.com.tacsutn.grupo1.eventapp.repositories.AuthorityRepository;
import ar.com.tacsutn.grupo1.eventapp.repositories.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import javax.servlet.Filter;

import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {EventAppApplication.class})
public class AuthenticationControllerTest extends ControllerTest {
  @Autowired
  private UserRepository userRepository;

  @Autowired
  private AuthorityRepository authorityRepository;

  @Autowired
  private WebApplicationContext webApplicationContext;

  @Autowired
  private Filter springSecurityFilterChain;

  private MockMvc mockMvc;

  @Before
  public void before() {
    super.before();
    mockMvc = MockMvcBuilders
            .webAppContextSetup(webApplicationContext)
            .addFilters(springSecurityFilterChain)
            .apply(springSecurity())
            .build();
  }

  @DirtiesContext
  @Test
  public void canLogin() throws Exception {
    mockMvc.perform(
            post("/api/v1/login")
                    .contentType(MediaType.APPLICATION_JSON_UTF8)
                    .content("{\"username\": \"user\", \"password\": \"user\"}"))
            .andExpect(status().isOk());
  }

  @DirtiesContext
  @Test
  public void shouldNotLoginWithWrongUser() throws Exception {
    mockMvc.perform(
            post("/api/v1/login")
                    .contentType(MediaType.APPLICATION_JSON_UTF8)
                    .content("{\"username\": \"wrong\", \"password\": \"wrong\"}"))
            .andExpect(status().isUnauthorized());
  }

  @DirtiesContext
  @Test
  public void shouldNotLoginWithWrongPassword() throws Exception {
    mockMvc.perform(
            post("/api/v1/login")
                    .contentType(MediaType.APPLICATION_JSON_UTF8)
                    .content("{\"username\": \"user\", \"password\": \"wrong\"}"))
            .andExpect(status().isUnauthorized());
  }

  @DirtiesContext
  @Test
  public void canRefresh() throws Exception {
    MockHttpServletResponse loginResponse = mockMvc.perform(
            post("/api/v1/login")
                    .contentType(MediaType.APPLICATION_JSON_UTF8)
                    .content("{\"username\": \"user\", \"password\": \"user\"}")).andReturn().getResponse();

    mockMvc.perform(
            get("/api/v1/refresh")
                    .header("Authorization", getBearerToken(loginResponse)))
            .andExpect(status().isOk());
  }

  @DirtiesContext
  @Test
  public void shouldNotRefreshWhenNotLogged() throws Exception {
    mockMvc.perform(
            get("/api/v1/refresh"))
            .andExpect(status().isUnauthorized());
  }

  private String getBearerToken(MockHttpServletResponse response) throws Exception {
    ObjectMapper objectMapper = new ObjectMapper();

    return "Bearer " + objectMapper.readTree(response.getContentAsString()).get("token").asText();
  }
}

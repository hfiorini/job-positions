package com.fetcher.positions.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fetcher.positions.entity.request.ImportRequest;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class PositionControllerTest {

    @Autowired
    private WebApplicationContext context;

    private MockMvc mvc;

    @Before
    public void setup() {
        mvc = MockMvcBuilders
                .webAppContextSetup(context)
                .apply(springSecurity())
                .build();
    }

    @WithMockUser(username = "user_guest", password = "secret", roles = "USER")
    @Test
    public void givenRequestOnFindByEndpointWhenParamsAndUrlIsOkThenShouldSucceedWith200() throws Exception {
        mvc.perform(get("/api/v1/positions/findBy")
                .contentType(MediaType.APPLICATION_JSON)
                .param("type", "Full Time")
                .param("description", "Scrum Master")
                .param("location", "Remote")
        )
                .andExpect(status().isOk());

    }

    @WithMockUser(username = "invalid_user", password = "wrong_password", roles = "INVALID_ROL")
    @Test
    public void givenRequestOnFindByEndpointWhenParamsAndUrlIsOkButCredentialsAreInvalidThenShouldBeForbiden403() throws Exception {
        mvc.perform(get("/api/v1/positions/findBy")
                .contentType(MediaType.APPLICATION_JSON)
                .param("type", "Full Time")
                .param("description", "Scrum Master")
                .param("location", "Remote")
        )
                .andExpect(status().is(403));

    }

    @WithMockUser(username = "user_guest", password = "secret", roles = "USER")
    @Test
    public void givenRequestOnFindByEndpointWhenUrlIsWrongThenShouldReturn4XXError() throws Exception {
        mvc.perform(get("/api/v1/positions/findBy/wrong_url")
                .contentType(MediaType.APPLICATION_JSON)
                .param("type", "Full")
                .param("description", "Scrum Master")
                .param("location", "Remote")
        )
                .andExpect(status().is4xxClientError());

    }

    @WithMockUser(username = "user_guest", password = "secret", roles = "USER")
    @Test
    public void givenRequestOnImportEndpointWhenUrlIsWrongThenShouldReturn4XXError() throws Exception {
        mvc.perform(post("/api/v1/positions/import/wrong_url")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError());

    }

    @WithMockUser(username = "user_guest", password = "secret", roles = "USER")
    @Test
    public void givenRequestOnImportEndpointWhenBodyIsNotPresentThenShouldReturn4XXError() throws Exception {
        mvc.perform(post("/api/v1/positions/import")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError());

    }

    @WithMockUser(username = "user_admin", password = "secret", roles = "ADMIN")
    @Test
    public void givenRequestOnImportEndpointWhenBodyIsPresentThenShouldReturnSuccessWith200() throws Exception {
        ImportRequest body = new ImportRequest();
        body.setCount(50);
        body.setUrl("https://jobs.github.com/positions.json");
        ObjectMapper Obj = new ObjectMapper();

        mvc.perform(post("/api/v1/positions/import")
                .contentType(MediaType.APPLICATION_JSON).content(Obj.writeValueAsString(body)))
                .andExpect(status().isCreated());

    }

    @WithMockUser(username = "user_guest", password = "secret", roles = "USER")
    @Test
    public void givenRequestOnImportEndpointWhenBodyIsPresentButCredentialsAreWrongThenShouldReturnErrorWith403() throws Exception {
        ImportRequest body = new ImportRequest();
        body.setCount(50);
        body.setUrl("https://jobs.github.com/positions.json");
        ObjectMapper Obj = new ObjectMapper();

        mvc.perform(post("/api/v1/positions/import")
                .contentType(MediaType.APPLICATION_JSON).content(Obj.writeValueAsString(body)))
                .andExpect(status().is(403));

    }
}

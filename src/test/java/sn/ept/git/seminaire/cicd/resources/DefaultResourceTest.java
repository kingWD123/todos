package sn.ept.git.seminaire.cicd.resources;

import lombok.extern.slf4j.Slf4j;
import static org.hamcrest.Matchers.is;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import sn.ept.git.seminaire.cicd.utils.UrlMapping;


import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Slf4j
@WebMvcTest(DefaultResource.class)
class DefaultResourceTest  {

    @Autowired
    protected MockMvc mockMvc;

    @Autowired
    private DefaultResource defaultResource;
    @Test
    void indexShouldReturnWelcomeMessage() throws Exception {
        mockMvc.perform(get(UrlMapping.Default.INDEX)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", is(DefaultResource.WELCOME_MSG)));
    }

    @Test
    void healthShouldReturnServiceStatus() throws Exception {
        mockMvc.perform(get(UrlMapping.Default.HEALTH)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", is(DefaultResource.HEALTH_MSG)));
    }


}
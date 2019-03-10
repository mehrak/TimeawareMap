package com.project.datastore;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.hamcrest.Matchers.equalTo;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class DataStoreControllerTest {
    @Autowired
    private MockMvc mvc;

    @Test
    public void testIndexPage() throws Exception {
        mvc.perform(MockMvcRequestBuilders.get("/").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string(equalTo("Greetings from Karan Mehra!")));
    }

    @Test
    public void testPutUri() throws Exception {
        mvc.perform(MockMvcRequestBuilders.put("/put/key/k1").content("v1").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    public void testGetUri() throws Exception {
        mvc.perform(MockMvcRequestBuilders.get("/get/key/k1").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(content().string(equalTo("No value found for key k1")));
    }

    @Test
    public void testGetWhenUri() throws Exception {
        mvc.perform(MockMvcRequestBuilders.get("/get/key/k1/when/2019-03-09T17:09:36.000").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(content().string(equalTo("No value found for key k1")));
    }

    @Test
    public void testRemoveUri() throws Exception {
        mvc.perform(MockMvcRequestBuilders.delete("/remove/key/k1").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }
}

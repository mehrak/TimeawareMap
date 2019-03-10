package com.project.datastore;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import java.net.URI;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class DataStoreControllerIntegrationTest {
    @LocalServerPort
    private int port;

    private URL base;

    @Autowired
    private TestRestTemplate template;

    @Before
    public void setUp() throws Exception {
        this.base = new URL("http://localhost:" + port + "/");
    }

    @Test
    public void testIndex() {
        ResponseEntity<String> response = template.getForEntity(base.toString(),
                String.class);
        assertThat(response.getBody(), equalTo("Greetings from Karan Mehra!"));
    }

    @Test
    public void testPutAndGet() {
        template.put(URI.create(base.toString().concat("/put/key/k1")), "v1");
        ResponseEntity<String> response = template.getForEntity(base.toString().concat("/get/key/k1"), String.class);
        assertThat(response.getBody(), equalTo("v1"));
    }

    @Test
    public void testGetWhen() throws Exception {
        LocalDateTime t1 = LocalDateTime.now();
        template.put(URI.create(base.toString().concat("/put/key/k2")), "v1");

        ResponseEntity<String> response = template.getForEntity(base.toString().concat("/get/key/k2"), String.class);
        assertThat(response.getBody(), equalTo("v1"));

        Thread.sleep(100);
        LocalDateTime t2 = LocalDateTime.now();

        template.put(URI.create(base.toString().concat("/put/key/k2")), "v2");
        Thread.sleep(100);
        LocalDateTime t3 = LocalDateTime.now();

        response = template.getForEntity(base.toString().concat("/get/key/k2/when/")
                .concat(t1.format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS"))), String.class);
        assertThat(response.getBody(), equalTo("No value found for key k2"));

        response = template.getForEntity(base.toString().concat("/get/key/k2/when/")
                .concat(t2.format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS"))), String.class);
        assertThat(response.getBody(), equalTo("v1"));

        response = template.getForEntity(base.toString().concat("/get/key/k2/when/")
                .concat(t3.format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS"))), String.class);
        assertThat(response.getBody(), equalTo("v2"));
    }

    @Test
    public void testRemove() throws Exception {
        template.put(URI.create(base.toString().concat("/put/key/k3")), "v1");

        ResponseEntity<String> response = template.getForEntity(base.toString().concat("/get/key/k3"), String.class);
        assertThat(response.getBody(), equalTo("v1"));

        Thread.sleep(100);
        LocalDateTime t1 = LocalDateTime.now();

        template.put(URI.create(base.toString().concat("/put/key/k3")), "v2");
        Thread.sleep(100);
        LocalDateTime t2 = LocalDateTime.now();
        template.delete(URI.create(base.toString().concat("/remove/key/k3")));

        response = template.getForEntity(base.toString().concat("/get/key/k3"), String.class);
        assertThat(response.getBody(), equalTo("No value found for key k3"));

        template.put(URI.create(base.toString().concat("/put/key/k3")), "v3");
        Thread.sleep(100);
        LocalDateTime t3 = LocalDateTime.now();
        template.delete(URI.create(base.toString().concat("/remove/key/k3")));

        response = template.getForEntity(base.toString().concat("/get/key/k3"), String.class);
        assertThat(response.getBody(), equalTo("No value found for key k3"));

        response = template.getForEntity(base.toString().concat("/get/key/k3/when/")
                .concat(t1.format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS"))), String.class);
        assertThat(response.getBody(), equalTo("v1"));

        response = template.getForEntity(base.toString().concat("/get/key/k3/when/")
                .concat(t2.format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS"))), String.class);
        assertThat(response.getBody(), equalTo("v2"));

        response = template.getForEntity(base.toString().concat("/get/key/k3/when/")
                .concat(t3.format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS"))), String.class);
        assertThat(response.getBody(), equalTo("v3"));
    }
}

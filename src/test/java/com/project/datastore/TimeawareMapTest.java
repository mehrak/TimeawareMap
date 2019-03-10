package com.project.datastore;

import com.project.datastore.util.TimeawareMap;
import com.project.datastore.util.TimeawareMapImpl;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDateTime;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

@RunWith(SpringRunner.class)
@SpringBootTest
public class TimeawareMapTest {

    @Test
    public void testTimeawareMap() throws InterruptedException {
        TimeawareMap<String, Object> map = new TimeawareMapImpl<>();
        Object o1 = new Object();
        Object o2 = new Object();
        Object o3 = new Object();

        LocalDateTime t1 = LocalDateTime.now();
        assertNull(map.put("key1", o1));
        assertEquals(o1, map.get("key1"));
        assertNull(map.getWhen("key1", t1.minusSeconds(1)));
        assertEquals(map.get("key1"), map.getWhen("key1", LocalDateTime.now()));

        Thread.sleep(5000);//Wait 5s

        LocalDateTime t2 = LocalDateTime.now();
        assertEquals(o1, map.put("key1", o2));
        assertEquals(o2, map.get("key1"));
        assertEquals(o1, map.getWhen("key1", t2.minusSeconds(1)));

        Thread.sleep(5000);//Wait 5s

        LocalDateTime t3 = LocalDateTime.now();
        assertEquals(o2, map.remove("key1"));
        assertNull(map.get("key1"));
        assertEquals(o1, map.getWhen("key1", t2.minusSeconds(1)));
        assertEquals(o2, map.getWhen("key1", t3.minusSeconds(1)));
        assertNull(map.put("key1", o3));

        Thread.sleep(5000);//Wait 5s
        LocalDateTime t4 = LocalDateTime.now();
        assertEquals(o3, map.remove("key1"));
        assertNull(map.get("key1"));
        assertEquals(o1, map.getWhen("key1", t2.minusSeconds(1)));
        assertEquals(o2, map.getWhen("key1", t3.minusSeconds(1)));
        assertEquals(o3, map.getWhen("key1", t4.minusSeconds(1)));
    }
}

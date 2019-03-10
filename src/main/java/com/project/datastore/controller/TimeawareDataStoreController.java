package com.project.datastore.controller;

import com.project.datastore.service.TimeawareDataStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
public class TimeawareDataStoreController {
    @Autowired
    private TimeawareDataStore<String, Object> dataStore;

    @RequestMapping("/")
    public String index() {
        return "Greetings from Karan Mehra!";
    }

    @PutMapping(value="/put/key/{key}")
    public ResponseEntity<Object> put(@PathVariable("key") String key, @RequestBody String val) {
        Object prevVal = dataStore.put(key, val);
        return new ResponseEntity<>(prevVal, HttpStatus.OK);
    }

    @GetMapping(value="/get/key/{key}")
    public ResponseEntity<Object> get(@PathVariable("key") String key) {
        Object val = dataStore.get(key);
        if (val == null) {
            return new ResponseEntity<>("No value found for key " + key, HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(val, HttpStatus.OK);
    }

    @GetMapping(value="/get/key/{key}/when/{time}")
    public ResponseEntity<Object> getWhen(@PathVariable("key") String key,
                          @PathVariable("time")
                          @DateTimeFormat(pattern="yyyy-MM-dd'T'HH:mm:ss.SSS") LocalDateTime time) {
        Object val = dataStore.getWhen(key, time);
        if (val == null) {
            return new ResponseEntity<>("No value found for key " + key, HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(val, HttpStatus.OK);
    }

    @DeleteMapping(value="/remove/key/{key}")
    public ResponseEntity<Object> remove(@PathVariable("key") String key) {
        Object val = dataStore.remove(key);
        if (val == null) {
            return new ResponseEntity<>("No value found for key " + key, HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(val, HttpStatus.OK);
    }
}

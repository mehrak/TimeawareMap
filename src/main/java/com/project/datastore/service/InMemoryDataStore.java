package com.project.datastore.service;

import com.project.datastore.util.TimeawareMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class InMemoryDataStore<K, V> implements TimeawareDataStore<K, V> {
    @Autowired
    private TimeawareMap<K, V> timeawareMap;

    @Override
    public V get(K key) {
        return timeawareMap.get(key);
    }

    @Override
    public V getWhen(K key, LocalDateTime time) {
        return timeawareMap.getWhen(key, time);
    }

    @Override
    public V put(K key, V val) {
        return timeawareMap.put(key, val);
    }

    @Override
    public V remove(K key) {
        return timeawareMap.remove(key);
    }
}

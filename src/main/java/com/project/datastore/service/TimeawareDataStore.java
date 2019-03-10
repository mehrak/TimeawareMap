package com.project.datastore.service;

import java.time.LocalDateTime;

public interface TimeawareDataStore<K, V> {
    V get(K key);

    V getWhen(K key, LocalDateTime time);

    V put(K key, V val);

    V remove(K key);
}

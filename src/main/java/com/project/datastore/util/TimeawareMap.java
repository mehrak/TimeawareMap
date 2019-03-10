package com.project.datastore.util;

import java.time.LocalDateTime;
import java.util.Map;

/**
 * A map implementation able to provide past values
 *
 * @param <K>
 * @param <V>
 */
public interface TimeawareMap<K, V> extends Map<K,V> {
    /**
     *  Returns the value to which the specified key <b>WAS</b> mapped,
     *  or {@code null} if this map did not contain any mapping for the key at that time.
     *
     *  @param key the key whose associated value is to be returned
     *  @param time the date at which the associated value was mapped
     *
     *  @return the value to which the specified key was mapped at the time, or
     *  {@code null} if this map contained no mapping for the key
     *
     *  @throws ClassCastException if the key is of an inappropriate type for this map
     *  (<a href="{@docRoot}/java/util/Collection.html#optional-restrictions">optional</a>)
     *  @throws NullPointerException if the specified key is null and this map
     *  does not permit null keys
     */
    public V getWhen(K key, LocalDateTime time);
}

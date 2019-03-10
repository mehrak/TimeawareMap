package com.project.datastore.util;

import org.springframework.stereotype.Component;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Component
public class TimeawareMapImpl<K,V> implements TimeawareMap<K,V> {
    // stores live key-value pair along with the timestamp it was added or updated
    private final Map<K, TreeMap<Long, V>> liveMap = new HashMap<>();

    // archives the removed key-value pair along with their temporal info
    private final Map<K, TreeMap<Long, V>> archiveMap = new HashMap<>();

    /**
     *  Returns the value to which the specified key was mapped at the specified time, if no such value exists
     *  then returns the value to which the specified key was mapped at the greatest time less than the
     *  specified time, if no such value exists then returns null.
     *
    */
    @Override
    public V getWhen(K key, LocalDateTime time) {
        Optional<V> val = getVal(key, time, liveMap);

        if(!val.isPresent()) {
            val = getVal(key, time, archiveMap);
        }

        return val.orElse(null);
    }

    /**
     *  Returns the sum of the size of both liveMap and archivedMap.
     *
     */
    @Override
    public int size() {
        return this.keySet().size();
    }

    /**
     *  Returns the empty iff both liveMap and archivedMap are empty.
     *
     */
    @Override
    public boolean isEmpty() {
        return liveMap.isEmpty() && archiveMap.isEmpty();
    }

    /**
     *  Returns true if either liveMap or archivedMap contains the key.
     *
     */
    @Override
    public boolean containsKey(Object key) {
        return liveMap.containsKey(key) || archiveMap.containsKey(key);
    }

    /**
     *  Returns true if either liveMap or archivedMap contains the value.
     *
     */
    @Override
    public boolean containsValue(Object value) {
        return Stream.of(liveMap.values().stream(), archiveMap.values().stream())
                .flatMap(tmap -> tmap)
                .anyMatch(tmap -> tmap.containsValue(value));
    }

    /**
     *  Returns the value to which the specified key was mapped at the current time in the
     *  liveMap (i.e. key is mapped to some value that's not removed yet), if no such value exists
     *  then returns the value to which the specified key was mapped at the greatest time less than the
     *  current time, if no such value exists then returns null.
     *
     */
    @Override
    public V get(Object key) {
        return getVal((K)key, LocalDateTime.now(), liveMap)
                .orElse(null);
    }

    /**
     * Associates the specified value with the specified key in this map along with the current time of entry.
     * If the map previously contained a mapping for the key, the old value is preserved
     * along with it's time of entry.
     *
     * It returns the value to which the specified key was mapped at the greatest time less than the
     * current time, if no such value exists then returns null.
     *
     */
    @Override
    public V put(K key, V value) {
        V retVal = null;
        TreeMap<Long, V> tmap = liveMap.get(key);
        if(tmap == null) {
            tmap = new TreeMap<>();
            liveMap.put(key, tmap);
        } else {
            retVal = this.get(key);
        }
        tmap.put(LocalDateTime.now().toInstant(ZoneOffset.UTC).toEpochMilli(), value);

        return retVal;
    }

    /**
     * Removes the mapping of the specified key from the liveMap and archives key and its values
     * along with their temporal info.
     *
     * It returns the value to which the specified key was mapped in the liveMap at the greatest time
     * less than the current time, or null if the liveMap contained no mapping for the key.
     *
     */
    @Override
    public V remove(Object key) {
        TreeMap<Long, V> tmap = liveMap.remove(key);
        if(tmap == null) return null;

        TreeMap<Long, V> htmap = archiveMap.get(key);
        if(htmap != null) {
            htmap.putAll(tmap);
        } else {
            archiveMap.put((K)key, tmap);
        }

        return tmap.lastEntry().getValue();
    }

    @Override
    public void putAll(Map m) {
        throw new NotImplementedException();
    }

    /**
     * Removes the mapping of all the keys from the liveMap and archives keys and its values
     * along with their temporal info.
     *
     */
    @Override
    public void clear() {
        liveMap.keySet().forEach(this::remove);
    }

    @Override
    public Set<K> keySet() {
        return Stream.of(liveMap.keySet().stream(), archiveMap.keySet().stream())
                .flatMap(kset -> kset)
                .collect(Collectors.toSet());
    }

    @Override
    public Collection<V> values() {
         return Stream.of(liveMap.values().stream(), archiveMap.values().stream())
                 .flatMap(tmap -> tmap)
                 .flatMap(tmap -> tmap.values().stream())
                 .collect(Collectors.toList());
    }

    @Override
    public Set<Map.Entry<K, V>> entrySet() {
        throw new NotImplementedException();
    }

    private Optional<V> getVal(K key, LocalDateTime time, Map<K, TreeMap<Long, V>> map) {
        return Optional.ofNullable(map.get(key))
                .map(tmap -> tmap.floorEntry(time.toInstant(ZoneOffset.UTC).toEpochMilli()))
                .map(Map.Entry::getValue);
    }
}

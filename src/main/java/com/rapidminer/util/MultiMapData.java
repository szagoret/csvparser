package com.rapidminer.util;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class MultiMapData<K, V, L> {

    private final Map<K, Map<V, List<L>>> multiMapData = new LinkedHashMap<>();

    /**
     * since this data structure is most used for
     * adding values and random access,
     * the inner storage is ArrayList
     * <p>
     * get(int index) is O(1)
     */
    public void put(K key, V subKey, L value) {
        this.multiMapData
                .computeIfAbsent(key, k -> new LinkedHashMap<>())
                .computeIfAbsent(subKey, sK -> new ArrayList<>())
                .add(value);
    }

    public Map<K, Map<V, List<L>>> getMap() {
        return multiMapData;
    }
}

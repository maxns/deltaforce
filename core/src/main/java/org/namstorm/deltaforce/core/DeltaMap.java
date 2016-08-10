package org.namstorm.deltaforce.core;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Created by maxnamstorm on 6/8/2016.
 *
 * It's a delta containing a map of deltas
 */
public class DeltaMap<K extends String, V extends Delta<?>> extends Delta<Map<K,V>> implements Map<K,V> {

    public DeltaMap(OP op, K fieldName, Map oldValue) {

        super(op, fieldName, oldValue, new HashMap<>());
    }
    protected Map<K, V> map() { return getNewValue(); }

    public void addDelta(K fieldName, V d) {
        map().put(fieldName, d);
    }


    @Override
    public int size() {
        return map().size();
    }

    @Override
    public boolean isEmpty() {
        return map().isEmpty();
    }

    @Override
    public boolean containsKey(Object key) {
        return map().containsKey(key);
    }

    @Override
    public boolean containsValue(Object value) {
        return map().containsValue(value);
    }

    @Override
    public V get(Object key) {
        return map().get(key);
    }

    @Override
    public V put(K key, V value) {
        return map().put(key, value);
    }

    @Override
    public V remove(Object key) {
        return map().remove(key);
    }

    @Override
    public void putAll(Map<? extends K, ? extends V> m) {
        map().putAll(m);
    }

    @Override
    public void clear() {
        map().clear();
    }

    @Override
    public Set<K> keySet() {
        return map().keySet();
    }

    @Override
    public Collection<V> values() {
        return map().values();
    }

    public Collection<V> deltas() {
        return map().values();
    }

    @Override
    public Set<Entry<K, V>> entrySet() {
        return map().entrySet();
    }
}

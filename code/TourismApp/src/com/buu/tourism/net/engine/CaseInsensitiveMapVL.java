package com.buu.tourism.net.engine;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class CaseInsensitiveMapVL implements Map<String, List<String>> {
    Map<String, List<String>> delegate = new HashMap<String, List<String>>();

    public CaseInsensitiveMapVL() {
        // default do
    }

    public CaseInsensitiveMapVL(Map<String, List<String>> map) {
        if (null != map) {
            delegate = map;
        }
    }

    /**
         * 
         */
    private static final long serialVersionUID = 1L;

    @Override
    public List<String> get(Object key) {
        String targetKey = getTargetKey(key);
        return delegate.get(targetKey);
    }

    @Override
    public void clear() {
        delegate.clear();
    }

    @Override
    public boolean containsKey(Object key) {
        String targetKey = getTargetKey(key);
        return delegate.containsKey(targetKey);
    }

    @Override
    public boolean containsValue(Object value) {
        return delegate.containsValue(value);
    }

    @Override
    public Set<java.util.Map.Entry<String, List<String>>> entrySet() {
        return delegate.entrySet();
    }

    @Override
    public boolean isEmpty() {
        return delegate.isEmpty();
    }

    @Override
    public Set<String> keySet() {
        return delegate.keySet();
    }

    @Override
    public List<String> put(String key, List<String> value) {
        return delegate.put(key, value);
    }

    @Override
    public void putAll(Map<? extends String, ? extends List<String>> map) {
        delegate.putAll(map);
    }

    @Override
    public List<String> remove(Object key) {
        return delegate.remove(key);
    }

    @Override
    public int size() {
        return delegate.size();
    }

    @Override
    public Collection<List<String>> values() {
        return delegate.values();
    }

    private String getTargetKey(Object key) {
        if (null == key) {
            return null;
        }
        Set<String> keySet = keySet();
        String targetKey = (String) key;
        if (null != keySet) {
            for (String k : keySet) {
                if (null != k) {
                    if (targetKey.toLowerCase().equals(k.toLowerCase())) {
                        targetKey = k;
                        break;
                    }
                }
            }
        }
        return targetKey;
    }
}
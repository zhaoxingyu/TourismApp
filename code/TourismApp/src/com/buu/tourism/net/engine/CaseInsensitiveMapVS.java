package com.buu.tourism.net.engine;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class CaseInsensitiveMapVS implements Map<String, String> {
    Map<String, String> delegate = new HashMap<String, String>();

    public CaseInsensitiveMapVS() {
        // default do
    }

    public CaseInsensitiveMapVS(Map<String, String> map) {
        if (null != map) {
            delegate = map;
        }
    }

    /**
         * 
         */
    private static final long serialVersionUID = 1L;

    @Override
    public String get(Object key) {
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
    public Set<java.util.Map.Entry<String, String>> entrySet() {
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
    public String put(String key, String value) {
        return delegate.put(key, value);
    }

    @Override
    public void putAll(Map<? extends String, ? extends String> map) {
        delegate.putAll(map);
    }

    @Override
    public String remove(Object key) {
        return delegate.remove(key);
    }

    @Override
    public int size() {
        return delegate.size();
    }

    @Override
    public Collection<String> values() {
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
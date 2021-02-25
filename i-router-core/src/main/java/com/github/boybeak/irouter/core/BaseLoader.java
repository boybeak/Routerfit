package com.github.boybeak.irouter.core;

import java.util.HashMap;
import java.util.Map;

public abstract class BaseLoader implements Loader {

    private Map<String, Class<?>> map = new HashMap<>();

    @Override
    public abstract String getHeader();

    public void loadIntoMap() {
    }

    protected void load(String key, Class<?> clz) {
        map.put(key, clz);
    }

    @Override
    public Class<?> getTargetClass(String key) {
        if (!isLoaded()) {
            loadIntoMap();
        }
        return map.get(key);
    }

    public boolean isLoaded() {
        return !map.isEmpty();
    }

}

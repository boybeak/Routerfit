package com.github.boybeak.irouter.core;

import java.util.HashMap;
import java.util.Map;

public final class LoaderManager {
    private static final LoaderManager sManager = new LoaderManager();

    public static LoaderManager getInstance() {
        return sManager;
    }

    private final Map<String, DelegateLoader> loadersMap = new HashMap<>();
    private boolean isInitialized = false;

    private LoaderManager() {
        init();
    }

    private void init() {
        if (isInitialized) {
            return;
        }
        load();
        isInitialized = true;
    }

    private void load() {
    }

    private void loadInto(BaseLoader loader) {
        String header = loader.getHeader();
        obtainLoader(header).mergeOtherLoaders(loader);
    }

    private DelegateLoader obtainLoader(String header) {
        DelegateLoader delegateLoader = loadersMap.get(header);
        if (delegateLoader == null) {
            delegateLoader = new DelegateLoader(header);
            loadersMap.put(header, delegateLoader);
        }
        return delegateLoader;
    }

    public Class<?> get(String path) {
        String[] segments = path.split("/");
        final String header = segments[0];
        final String tail = segments[1];
        return loadersMap.get(header).getTargetClass(tail);
    }

}
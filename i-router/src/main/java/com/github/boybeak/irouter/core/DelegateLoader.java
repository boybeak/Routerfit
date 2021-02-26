package com.github.boybeak.irouter.core;

import java.util.ArrayList;
import java.util.List;

public class DelegateLoader implements Loader {

    private String header;
    private final List<BaseLoader> loaders = new ArrayList<>();

    public DelegateLoader(String header) {
        this.header = header;
    }

    @Override
    public String getHeader() {
        return header;
    }

    public void mergeOtherLoaders(BaseLoader loader) {
        if (!loader.getHeader().equals(getHeader())) {
            throw new IllegalStateException("Can not merge loader " + loader + " into " + this + ", because not same header: " + loader.getHeader());
        }
        if (loaders.contains(loader)) {
            return;
        }
        loaders.add(loader);
    }

    @Override
    public Class<?> getTargetClass(String key) {
        for (BaseLoader loader : loaders) {
            if (!loader.isLoaded()) {
                loader.loadIntoMap();
            }
            Class<?> targetClz = loader.getTargetClass(key);
            if (targetClz != null) {
                return targetClz;
            }
        }
        return null;
    }
}

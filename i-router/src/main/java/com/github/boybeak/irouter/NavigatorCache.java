package com.github.boybeak.irouter;

import androidx.collection.LruCache;

import com.github.boybeak.irouter.core.LoaderManager;

public class NavigatorCache extends LruCache<String, Navigator> {

    private static final NavigatorCache sCache = new NavigatorCache(16);

    public static NavigatorCache getInstance() {
        return sCache;
    }

    /**
     * @param maxSize for caches that do not override {@link #sizeOf}, this is
     *                the maximum number of entries in the cache. For all other caches,
     *                this is the maximum sum of the sizes of the entries in this cache.
     */
    private NavigatorCache(int maxSize) {
        super(maxSize);
    }

    public Navigator obtain(String path) {
        Navigator navigator = sCache.get(path);
        if (navigator == null) {
            navigator = new Navigator(path);
            Class<?> targetClz = LoaderManager.getInstance().get(path);
            navigator.setTargetClz(targetClz);
        } else {
            navigator.reset();
        }
        return navigator;
    }

    public void put(Navigator navigator) {
        this.put(navigator.getPath(), navigator);
    }

}

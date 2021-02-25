package com.github.boybeak.irouter.compiler;

import java.util.HashMap;

import javax.lang.model.element.Name;

public class PathCache extends HashMap<String, PathCache.Tail> {

    private static final PathCache sCache = new PathCache();

    public static PathCache getInstance() {
        return sCache;
    }

    private PathCache(){}

    public void put(String path, Name clzName) {
        String[] segments = path.split("/");
        String header = segments[0];
        String tail = segments[1];
        obtainTail(header).put(tail, clzName);
    }

    private Tail obtainTail(String header) {
        Tail t = get(header);
        if (t == null) {
            t = new Tail(header);
            put(header, t);
        }
        return t;
    }

    public Name getClassName(String path) {
        String[] segments = path.split("/");
        String header = segments[0];
        String tail = segments[1];

        Tail t = get(header);

        if (t == null) {
            return null;
        }
        return t.get(tail);
    }

    public static class Tail extends HashMap<String, Name> {
        private String header;

        private Tail(String header) {
            this.header = header;
        }

        @Override
        public Name put(String segment, Name name) {
            if (containsKey(segment)) {
                throw new IllegalStateException("Conflict path " + header + "/" + segment + " for " + name);
            }
            return super.put(segment, name);
        }

        public String getHeader() {
            return header;
        }
    }

}

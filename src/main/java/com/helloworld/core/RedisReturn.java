package com.helloworld.core;

public class RedisReturn {
    private final long count;
    private final String key;

    public RedisReturn(long count, String key) {
        this.count = count;
        this.key = key;
    }

    public long getCount() {
        return count;
    }

    public String getKey() {
        return key;
    }
}
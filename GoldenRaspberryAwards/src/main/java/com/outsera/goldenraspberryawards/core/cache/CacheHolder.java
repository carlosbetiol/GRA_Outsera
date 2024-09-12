package com.outsera.goldenraspberryawards.core.cache;

import java.util.concurrent.Callable;

public interface CacheHolder {

    <T> T rememberOf(Callable<T> callable, long lockTimeout, Object... keys);

    void invalidateAll();

    default <T> T rememberOf(Callable<T> callable, Object... keys) {
        return this.rememberOf(callable, 0L, keys);
    }

    CacheBuilder fromKeys(Object... keys);

    class CacheBuilder {

        private final CacheHolder cache;

        private long lockTimeout;
        private final Object[] keys;

        public CacheBuilder(CacheHolder cache, Object... keys) {
            this.cache = cache;
            this.keys = keys;
        }

        public CacheBuilder lockTimeout(long lockTimeout) {
            this.lockTimeout = lockTimeout;
            return this;
        }

        public <T> T rememberOf(Callable<T> callable) {
            return cache.rememberOf(callable, lockTimeout, keys);
        }

    }



}

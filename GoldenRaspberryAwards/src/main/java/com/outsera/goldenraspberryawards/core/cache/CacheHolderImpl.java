package com.outsera.goldenraspberryawards.core.cache;

import org.springframework.cache.Cache;
import org.springframework.cache.support.AbstractValueAdaptingCache;

import java.util.concurrent.Callable;

public class CacheHolderImpl implements CacheHolder {

    private final AbstractValueAdaptingCache cache;

    public CacheHolderImpl(AbstractValueAdaptingCache cache) {
        this.cache = cache;
    }

    @Override
    public <T> T rememberOf(Callable<T> callable, long lockTimeout, Object... keys) {

//        final Object key = getCacheKey(keys);
//
//        if( key == null ) {
//            try {
//                T value = callable.call();
//                cache.put(Arrays.stream(keys).toArray(), value);
//                return value;
//            }
//            catch (Exception ex) {
//                throw new Cache.ValueRetrievalException(key, callable, ex);
//            }
//        }

        Cache.ValueWrapper valueWrapper;

        lockTimeout = 3000;

        synchronized (this) {

            valueWrapper =  cache.get(keys);

            if (valueWrapper == null) {
                long startTime = System.currentTimeMillis();
                while (valueWrapper == null && (System.currentTimeMillis() - startTime) < lockTimeout) {
                    try {
                        Thread.sleep(50);
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    }
                    valueWrapper = cache.get(keys);
                }
            }
        }

        if( valueWrapper == null ) {
            try {
                T value = callable.call();
                cache.put(keys, value);
                return value;
            }
            catch (Exception ex) {
                throw new Cache.ValueRetrievalException(keys, callable, ex);
            }
        }

        return (T) valueWrapper.get();

//
//        T cacheValue = (T) cache.get(keys, callable);
//
//        return (T) cache.putIfAbsent(keys, cacheValue);
//
//
//
//        T cacheValue = (T) cache.get(keys, callable);
//
//        if( lockTimeout <= 0 )
//            return cacheValue;
//
//        synchronized (this) {
//            cacheValue = (T) cache.get(keys, callable);
//
//            if (cacheValue == null) {
//                long startTime = System.currentTimeMillis();
//                while (cacheValue == null && (System.currentTimeMillis() - startTime) < lockTimeout) {
//                    try {
//                        Thread.sleep(50);
//                    } catch (InterruptedException e) {
//                        Thread.currentThread().interrupt();
//                    }
//                    cacheValue = (T) cache.get(keys, callable);
//                }
//            }
//        }
//
//        return cacheValue;

    }

    @Override
    public void invalidateAll() {

    }

    @Override
    public <T> T rememberOf(Callable<T> callable, Object... keys) {
        return CacheHolder.super.rememberOf(callable, keys);
    }

    @Override
    public CacheBuilder fromKeys(Object... keys) {
        return new CacheBuilder(this, keys);
    }

    protected Object getCacheKey(Object[] object_keys) {

        if (object_keys == null || object_keys.length == 0) {
            throw new IllegalArgumentException("No cache key specified");
        }

        if (object_keys.length == 1)
            return object_keys[0];

        return cache.get(object_keys);

    }
}

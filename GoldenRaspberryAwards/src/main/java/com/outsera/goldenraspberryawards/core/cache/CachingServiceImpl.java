package com.outsera.goldenraspberryawards.core.cache;

import lombok.extern.log4j.Log4j2;
import org.springframework.cache.CacheManager;
import org.springframework.cache.support.AbstractValueAdaptingCache;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Component
@Log4j2
public class CachingServiceImpl implements CachingService {

    private final CacheManager cacheManager;

    private final Map<String, CacheHolder> cache_holders;

    public CachingServiceImpl(CacheManager cacheManager) {
        this.cacheManager = cacheManager;
        cache_holders = new ConcurrentHashMap<>();
    }

    @Override
    public Collection<String> getCacheNames() {
        return cacheManager.getCacheNames();
    }

    @Override
    public CacheHolder getCacheHolder(String name) {

        return cache_holders.computeIfAbsent(name, key ->

                Optional.ofNullable(cacheManager.getCache(name))
                        .map(cache -> new CacheHolderImpl((AbstractValueAdaptingCache) cache))
                        .orElseGet(() -> {

                            log.warn("No CacheHolder found with name {}. Proceeding with NoOp CacheHolder.", name);

                            return NoOpCacheHolderImpl.getInstance();

                        })

        );

    }

    @Override
    public CacheHolder.CacheBuilder from(String cacheHolderName, Object... keys) {
        return getCacheHolder(cacheHolderName).fromKeys(keys);
    }

}

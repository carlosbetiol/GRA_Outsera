package com.outsera.goldenraspberryawards.core.cache;

import java.util.Collection;

public interface CachingService {
    Collection<String> getCacheNames();

    CacheHolder getCacheHolder(String name);

    CacheHolder.CacheBuilder from(String cacheHolderName, Object... keys);
}

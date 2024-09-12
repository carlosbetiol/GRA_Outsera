package com.outsera.goldenraspberryawards.core.cache;

import org.springframework.cache.support.AbstractValueAdaptingCache;

public class NoOpCacheHolderImpl extends CacheHolderImpl {

    private static final NoOpCacheHolderImpl instance = new NoOpCacheHolderImpl(new NoOpCache(true));

    @SuppressWarnings("QsPrivateBeanMembersInspection")
    private NoOpCacheHolderImpl(AbstractValueAdaptingCache cache) {
        super(cache);
    }

    public static NoOpCacheHolderImpl getInstance() {
        return instance;
    }
}

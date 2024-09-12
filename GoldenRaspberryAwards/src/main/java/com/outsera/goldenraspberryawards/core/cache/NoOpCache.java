package com.outsera.goldenraspberryawards.core.cache;

import org.springframework.cache.support.AbstractValueAdaptingCache;
import org.springframework.util.Assert;

import java.util.concurrent.Callable;
import java.util.function.Function;

public class NoOpCache extends AbstractValueAdaptingCache {

    private static final String NAME = NoOpCache.class.getName();

    protected NoOpCache(boolean allowNullValues) {
        super(allowNullValues);
    }

    @Override
    public String getName() {
        return NAME;
    }

    @Override
    public Object getNativeCache() {
        return null;
    }

    @Override
    public <T> T get(Object key, Callable<T> valueLoader) {
        return (T) new LoadFunction(valueLoader);
    }

    @Override
    public void put(Object key, Object value) {
    }

    @Override
    public void evict(Object key) {
    }

    @Override
    protected Object lookup(Object key) {
        return null;
    }

    @Override
    public void clear() {
    }

    private class LoadFunction implements Function<Object, Object> {

        private final Callable<?> valueLoader;

        public LoadFunction(Callable<?> valueLoader) {
            Assert.notNull(valueLoader, "Callable must not be null");
            this.valueLoader = valueLoader;
        }

        @Override
        public Object apply(Object key) {
            try {
                return toStoreValue(this.valueLoader.call());
            }
            catch (Exception ex) {
                throw new ValueRetrievalException(key, this.valueLoader, ex);
            }
        }
    }

}

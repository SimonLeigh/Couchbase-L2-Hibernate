package net.thumbtack.hibernate.couchbase;

import com.googlecode.hibernate.memcached.*;
import com.googlecode.hibernate.memcached.Config;
import net.spy.memcached.MemcachedClient;

/**
 * Created by ipergenitsa on 05.10.14.
 */
public class MemcachedRegionFactoryDelegat extends MemcachedRegionFactory {
    public MemcacheClientFactory getFactory(Config config) {
        return getMemcachedClientFactory(config);
    }
}

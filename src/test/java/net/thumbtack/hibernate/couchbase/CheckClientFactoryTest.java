package net.thumbtack.hibernate.couchbase;

import com.couchbase.client.CouchbaseClient;
import com.googlecode.hibernate.memcached.*;
import com.googlecode.hibernate.memcached.Config;
import net.thumbtack.hibernate.couchbase.CouchbaseClientFactory;
import org.junit.Before;
import org.junit.Test;

import java.io.*;
import java.util.Properties;

import static org.junit.Assert.assertTrue;

/**
 * Created by ipergenitsa on 05.10.14.
 */
public class CheckClientFactoryTest {

    MemcachedRegionFactoryDelegat memcachedRegionFactoryDelegat;
    Properties properties;

    @Test
    public void testCreateClient() throws Exception {
        ClassLoader classloader = Thread.currentThread().getContextClassLoader();
        InputStreamReader inputStreamReader = new InputStreamReader(classloader.getResourceAsStream(
                "factory-properties.properties"));
        properties = new Properties();
        properties.load(inputStreamReader);
        com.googlecode.hibernate.memcached.Config config = new Config(new PropertiesHelper(properties));
        memcachedRegionFactoryDelegat = new MemcachedRegionFactoryDelegat();

        MemcacheClientFactory memcacheClientFactory = memcachedRegionFactoryDelegat.getFactory(config);
        Memcache client = memcacheClientFactory.createMemcacheClient();

        assertTrue(CouchbaseClientFactory.class.equals(memcacheClientFactory.getClass()));
        assertTrue(CouchbaseCacheClient.class.equals(client.getClass()));

    }
}

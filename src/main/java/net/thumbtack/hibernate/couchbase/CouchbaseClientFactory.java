/*
 *  Copyright 2014 Couchbase, Inc.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package net.thumbtack.hibernate.couchbase;

import com.couchbase.client.CouchbaseClient;
import com.couchbase.client.CouchbaseConnectionFactory;
import com.couchbase.client.CouchbaseConnectionFactoryBuilder;
import com.googlecode.hibernate.memcached.Memcache;
import com.googlecode.hibernate.memcached.PropertiesHelper;
import com.googlecode.hibernate.memcached.spymemcached.SpyMemcacheClientFactory;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

/**
 * Factory of Couchbase clients. This factory shall be used instead of default SpyMemcacheClientFactory. 
 * To replace the factory, specify the following line in <code>persistence.xml</code>:
 * <code>
 * &lt;property name="hibernate.memcached.memcacheClientFactory" value="net.thumbtack.hibernate.couchbase.CouchbaseClientFactory" /&gt;
 * </code>
 */
public class CouchbaseClientFactory extends SpyMemcacheClientFactory {

    public CouchbaseClientFactory(final PropertiesHelper properties) {
        super(properties);
    }

    public Memcache createMemcacheClient() throws Exception {
        CouchbaseClient client = new CouchbaseClient(buildCouchbaseConnectionFactory());
        return new CouchbaseCacheClient(client);
    }

    private CouchbaseConnectionFactory buildCouchbaseConnectionFactory() throws Exception {
        CouchbaseConnectionFactoryBuilder cfb = new CouchbaseConnectionFactoryBuilder();
        cfb
                .setReadBufferSize(getReadBufferSize())
                .setHashAlg(getHashAlgorithm())
                .setOpTimeout(getOperationTimeoutMillis())
                .setDaemon(isDaemonMode());

        CouchbaseConnectionFactory cf = cfb.buildCouchbaseConnection(getCouchbaseServerList(), getBucketName(), getBucketPassword());
        return cf;
    }

    String getBucketName() {
        return getProperties().get(Configuration.COUCHBASE_SETTINGS_BUCKET_NAME, "default");
    }

    String getBucketPassword() {
        return getProperties().get(Configuration.COUCHBASE_SETTINGS_BUCKET_PASSWORD, "");
    }

    List<URI> getCouchbaseServerList() throws Exception {
        final String urlsLine = getProperties().get(Configuration.COUCHBASE_SETTINGS_SERVERS, "http://localhost:8091/pools");
        StringTokenizer tokenizer = new StringTokenizer(urlsLine, ",");
        ArrayList<URI> urls = new ArrayList<URI>();
        while (tokenizer.hasMoreTokens()) {
            String uri = tokenizer.nextToken().trim();
            try {
                urls.add(new URI(uri));
            } catch (URISyntaxException e) {
                String msg = String.format("Wrong URI: %s", uri);
                throw new Exception(msg, e);
            }
        }
        return urls;
    }
}

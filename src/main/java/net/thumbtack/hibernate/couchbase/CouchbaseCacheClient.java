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
import com.googlecode.hibernate.memcached.spymemcached.SpyMemcache;

/**
 * Implementation of Memcache client by Couchbase. Since CouchbaseClient is inherited from {@link com.googlecode.hibernate.memcached.spymemcached.SpyMemcache}
 * and Couchbase proto is internally based on memcache proto, it is possible to keep this minimalistic implementation.
 * All the cache access logic is implemented in {@link com.googlecode.hibernate.memcached.spymemcached.SpyMemcache} class.
 *
 * Please use this class as an extension point if you need to override or extend any of {@link com.googlecode.hibernate.memcached.Memcache} methods.
 */
public class CouchbaseCacheClient extends SpyMemcache {
    public CouchbaseCacheClient(final CouchbaseClient client) {
        super(client);
    }
}

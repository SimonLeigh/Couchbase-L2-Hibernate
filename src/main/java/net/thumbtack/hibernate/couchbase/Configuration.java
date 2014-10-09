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

/**
 * Configuration properties names. Used in persistence.xml to configure the cache.
 */
public class Configuration {
    /**
     * Configuration name prefix.
     */
    public static final String COUCHBASE_SETTINGS_PREFIX = "couchbase.cache.";
    /**
     * List of Couchbase servers, comma separated. For example:
     * <code>&lt;property name="couchbase.cache.servers"
     value="http://cluster-node00:8091/pools,
     http://cluster-node01:8091/pools,
     http://cluster-node02:8091/pools"/&gt;</code>
     */
    public static final String COUCHBASE_SETTINGS_SERVERS = COUCHBASE_SETTINGS_PREFIX + "servers";
    /**
     * Name of the bucket which will be used to store cached data.
     * <code>&lt;property name="couchbase.cache.bucket.name" value="l2-cache"/&gt;</code>
     */
    public static final String COUCHBASE_SETTINGS_BUCKET_NAME = COUCHBASE_SETTINGS_PREFIX + "bucket.name";
    /**
     * Bucket password.
     * @see #COUCHBASE_SETTINGS_BUCKET_NAME
     * <code>&lt;property name="couchbase.cache.bucket.password" value="12345"/&gt;</code>
     */
    public static final String COUCHBASE_SETTINGS_BUCKET_PASSWORD = COUCHBASE_SETTINGS_PREFIX + "bucket.password";
    /**
     * Lifetime of the cache entry, in seconds. If not specified or -1 - the entries will live until not removed.
     * <b>Not Implemented Yet.</b>
     * <code>&lt;property name="couchbase.cache.ttl" value="600"/&gt;</code>
     */
    public static final String COUCHBASE_SETTINGS_TTL = COUCHBASE_SETTINGS_PREFIX + "ttl";
}

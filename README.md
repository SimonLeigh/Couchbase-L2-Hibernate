# Hibernate Couchbase L2 cache

Implementation of the Hibernate Level 2 Cache with [Couchbase](http://www.couchbase.com/) cluster as a backend store.

## Disclaimer

This is a beta version. The current version of the library does not support a lot of settings of the Couchbase client.

## Licensing

Copyright 2014 [Couchbase, Inc.](http://www.couchbase.com/)

The library is licensed under Apache 2.0 license. See `LICENSE.txt` for the full license text.

## Prerequesites

* Compiled and installed [hibernate-memcached](https://github.com/thumbtack-technology/hibernate-memcached) 
* Installed and configured Couchbase cluster.
* Configured bucket to store the cached objects.
* Virtual Box and Vagrant (to run tests).

## Build

To build the project you'll need the JDK 1.7+ and Maven 3+.

### Quick build

For a quick build, please run `mvn install -Dmaven.test.skip=true` to compile without running any tests.

### Full build (with tests)

To make the build with running all the tests:

1. Run `vagrant up` to start and initialize Vagrant virtual box. It will take some time to install the dependencies.
2. Run the Maven build: `mvn install`.

### Build results

After the build you can either copy the result jar from `target/couchbase-l2-hibernate-1.0-SNAPSHOT.jar` or add it to your project as a Maven dependency:

```
<dependency>
    <groupId>net.thumbtack.couchbase-l2</groupId>
    <artifactId>couchbase-l2-hibernate</artifactId>
    <version>1.0-SNAPSHOT</version>
</dependency>
```

## Configuration

To start using the Couchbase cluster as a cache in Hibernate you need to configure Couchbase and make some changes in your `persistence.xml` file.

1. Create a new bucket in Couchbase admin panel. Write down the bucket name and the password.
2. Add `couchbase-l2-hibernate-1.0-SNAPSHOT.jar` and all dependencies to your project classpath.
3. In `persistence.xml` add the following lines (common configuration).
Please update the Couchbase URLs, bucket name and password with actual values.

```
<property name="couchbase.cache.servers"
          value="http://couchbase-cluster-node01:8091/pools,
                 http://couchbase-cluster-node02:8091/pools"/>                 
<property name="couchbase.cache.bucket.name" value="l2-cache"/>
<property name="couchbase.cache.bucket.password" value="cachepwd"/>
<property name="couchbase.cache.ttl" value="600"/>

<property name="hibernate.cache.region.factory_class" value="com.googlecode.hibernate.memcached.MemcachedRegionFactory" />
<property name="hibernate.memcached.memcacheClientFactory" value="net.thumbtack.hibernate.couchbase.CouchbaseClientFactory" />
```

## Contributors

Please drop us an email if you find this project useful or if you have any questions.

### [Thumbtack technology](http://thumbtack.net/)

Alexander Samolov <asamolov@thumbtack.net> or <alexsamolov@gmail.com> - cache implementation, basic tests.

Igor Pergenitsa <ipergenitsa@thumbtack.net> - unit tests.

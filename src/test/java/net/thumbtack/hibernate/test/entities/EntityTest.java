package net.thumbtack.hibernate.test.entities;

import net.spy.memcached.AddrUtil;
import net.spy.memcached.BinaryConnectionFactory;
import net.spy.memcached.MemcachedClient;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.io.IOException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.fail;

/**
 * Created by asamolov on 08/09/14.
 */
public abstract class EntityTest {

    protected final static Logger log = LoggerFactory.getLogger(EntityTest.class);

    static EntityManagerFactory emf;



    protected static <T> boolean checkInCache(Class<T> aClass, EntityWithId entity) {
        //TODO add check in couchbase

        // Wait till all writes to cache are finished
        try {
            TimeUnit.MILLISECONDS.sleep(50);
        } catch (InterruptedException e) {
        }
        boolean res = emf.getCache().contains(aClass, entity.getId());
        res = res;// && getObject(aClass, entity.getId()).equals(entity);
        if (res) {
            System.out.println("CACHED: " + entity);
        } else {
            System.out.println("NOT CACHED: " + entity);
        }
        return res;
    }

    public static void insertEntity(Object o) {
        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();
        em.persist(o);
        em.getTransaction().commit();
        em.close();
    }

    private static <T> T getObject(Class<T> aClass, Object id) {
        EntityManager em = emf.createEntityManager();
        T result = em.find(aClass, id);
        em.close();
        return result;
    }


    public static void clearCache() throws Exception {
        emf.getCache().evictAll();
    }


    //@Before
    public void clearDB() {
        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();
        em.createQuery("DELETE FROM OrderItem").executeUpdate();
        em.createQuery("DELETE FROM Order").executeUpdate();
        em.createQuery("DELETE FROM Person").executeUpdate();
        em.createQuery("DELETE FROM Item").executeUpdate();
        em.getTransaction().commit();
        em.close();
    }

    @BeforeClass
    public static void setUpEMFCouchbase() throws Exception {
        emf = Persistence.createEntityManagerFactory("net.thumbtack.hibernate.l2-couchbase-test");
    }

    @AfterClass
    public static void tearDown() throws Exception {
        emf.close();
    }

    public void dl() {
        Thread t = Thread.currentThread();
        StackTraceElement line =t.getStackTrace()[2];
        log.trace("-----> {}.{}({}:{})", line.getClassName(), line.getMethodName(), line.getFileName(), line.getLineNumber());
    }
}

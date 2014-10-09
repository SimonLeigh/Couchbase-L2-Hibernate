package net.thumbtack.hibernate.test.entities;

import org.junit.Test;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import java.util.concurrent.TimeUnit;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * CacheByQueryTest
 */
public class CacheByQueryTest extends EntityTest {

    public static final String name = "Michael";

    @Test
    public void testAppearanceObjectInL2CacheByPrimaryKey() throws Exception {
        Person person = new Person();
        person.setName(name);
        insertEntity(person);

        checkInCacheAndClearCache(person);

        EntityManager em = emf.createEntityManager();
        em.find(Person.class, person.getId());
        em.close();
        assertTrue(checkInCache(Person.class, person));

        TimeUnit.MICROSECONDS.sleep(50);
        em = emf.createEntityManager();
        em.find(Person.class, person.getId());
        em.close();
        assertTrue(checkInCache(Person.class, person));

    }

    @Test
    public void testAppearanceObjectInL2CacheByNamedQuery() throws Exception {
        Person person = new Person();
        person.setName(name);
        insertEntity(person);

        checkInCacheAndClearCache(person);

        EntityManager em = emf.createEntityManager();
        em.createNamedQuery("findPersonById").setParameter("id", person.getId()).getResultList();
        em.close();
        assertTrue(checkInCache(Person.class, person));

        TimeUnit.MICROSECONDS.sleep(50);

        em = emf.createEntityManager();
        em.createNamedQuery("findPersonById").setParameter("id", person.getId()).getResultList();
        em.close();
    }

    @Test
    public void testAppearanceObjectInL2CacheByJPQA() throws Exception {
        Person person = new Person();
        person.setName(name);
        insertEntity(person);

        checkInCacheAndClearCache(person);

        String query = "SELECT p FROM Person p WHERE p.id = " + person.getId();
        EntityManager em = emf.createEntityManager();
        em.createQuery(query, Person.class).getResultList();
        em.close();
        assertTrue(checkInCache(Person.class, person));
    }

    @Test
    public void testAppearanceObjectInL2CacheByJPQAWithParams() throws Exception {
        Person person = new Person();
        person.setName(name);
        insertEntity(person);

        checkInCacheAndClearCache(person);

        String query = "SELECT p FROM Person p WHERE p.id = :id";
        EntityManager em = emf.createEntityManager();
        em.createQuery(query, Person.class).setParameter("id", person.getId()).getResultList();
        em.close();
        assertTrue(checkInCache(Person.class, person));
    }

    @Test
    public void testAppearanceObjectInL2CacheByCriteriaJPA() throws Exception {
        Person person = new Person();
        person.setName(name);
        insertEntity(person);

        checkInCacheAndClearCache(person);

        EntityManager em = emf.createEntityManager();
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Person> cq = cb.createQuery(Person.class);
        Root<Person> personRoot = cq.from(Person.class);
        cq.select(personRoot);
        TypedQuery<Person> q = em.createQuery(cq);
        q.getResultList();
        em.close();
        assertTrue(checkInCache(Person.class, person));
    }

    @Test
    public void testAppearanceObjectInL2CacheByNativeSQL() throws Exception {
        Person person = new Person();
        person.setName(name);
        insertEntity(person);

        checkInCacheAndClearCache(person);

        String query = "SELECT * FROM PERSON WHERE id = " + person.getId();
        EntityManager em = emf.createEntityManager();
        em.createNativeQuery(query, Person.class).getResultList();
        em.close();
        assertTrue(checkInCache(Person.class, person));
    }

    @Test
    public void testAppearanceObjectInL2CacheByNativeSQLWithParams() throws Exception {
        Person person = new Person();
        person.setName(name);
        insertEntity(person);

        checkInCacheAndClearCache(person);

        String query = "SELECT * FROM PERSON WHERE id = :id";
        EntityManager em = emf.createEntityManager();
        em.createNativeQuery(query, Person.class).setParameter("id", person.getId()).getResultList();
        em.close();
        assertTrue(checkInCache(Person.class, person));
    }

    static void checkInCacheAndClearCache(Person person) throws Exception {
        assertTrue(checkInCache(Person.class, person));
        emf.getCache().evict(Person.class, person.getId());
        assertFalse(checkInCache(Person.class, person));
    }
}

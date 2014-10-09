package net.thumbtack.hibernate.test.entities;

import org.junit.Test;

import javax.persistence.EntityManager;

import static org.junit.Assert.*;

public class PersonTest extends EntityTest {

    @Test
    public void testSetName() throws Exception {
        EntityManager em = emf.createEntityManager();

        /*
        em.getTransaction().begin();
        em.createQuery(em.getCriteriaBuilder().createCriteriaDelete(Person.class)).executeUpdate();
        em.getTransaction().commit();
        */
        //List<Person> afterDelete = em.createQuery(em.getCriteriaBuilder().createQuery(Person.class)).getResultList();
        //assertEquals("Should be no records after remove all", 0, afterDelete.size());


        Person pTom = new Person();
        pTom.setName("Tom");
        Person pAlex = new Person();
        pAlex.setName("Alex");
        Person pPeter = new Person();
        pPeter.setName("Peter");

        System.out.println("Before: " + pAlex);
        em.getTransaction().begin();
        em.persist(pAlex);
        em.persist(pPeter);
        em.persist(pTom);
        em.getTransaction().commit();
        System.out.println("After: " + pAlex);
        em.close();


        em = emf.createEntityManager();
        checkInCache(Person.class, pAlex);
        checkInCache(Person.class, pPeter);
        checkInCache(Person.class, pTom);
        //emf.getCache().evictAll();
        clearCache();

        long nanos = System.nanoTime();
        Person pWarmup = em.find(Person.class, pTom.getId());
        long delta = System.nanoTime() - nanos;
        System.out.printf("Initial access in %.2f ms\n", delta / 1000000.0);
        assertEquals(pTom, pWarmup);

        nanos = System.nanoTime();
        Person p2 = em.find(Person.class, pAlex.getId());
        delta = System.nanoTime() - nanos;
        System.out.printf("Retrieved in %.2f ms\n", delta/1000000.0);
        checkInCache(Person.class, pAlex);
        checkInCache(Person.class, pPeter);
        checkInCache(Person.class, pTom);

        // known cache
        nanos = System.nanoTime();
        Person pCache = em.find(Person.class, pAlex.getId());
        delta = System.nanoTime() - nanos;
        System.out.printf("Retrieved (known cache) in %.2f ms\n", delta/1000000.0);
        if (p2 == pCache) {
            System.out.println("Reference equal vals!");
        } else {
            System.out.println("References not equals, please check cache settings");
        }
        assertEquals(p2, pCache);
        checkInCache(Person.class, pAlex);
        checkInCache(Person.class, pPeter);

        nanos = System.nanoTime();
        pCache = em.find(Person.class, pAlex.getId());
        delta = System.nanoTime() - nanos;
        System.out.printf("Retrieved (known cache) in %.2f ms\n", delta/1000000.0);
        if (p2 == pCache) {
            System.out.println("Reference equal vals!");
        } else {
            System.out.println("References not equals, please check cache settings");
        }
        assertEquals(p2, pCache);
        checkInCache(Person.class, pAlex);
        checkInCache(Person.class, pPeter);

        // known no-cache
        nanos = System.nanoTime();
        Person px = em.find(Person.class, pPeter.getId());
        delta = System.nanoTime() - nanos;
        System.out.printf("Retrieved (known no cache) in %.2f ms\n", delta/1000000.0);
        if (px == null) {
            System.out.println("Null returned no-cache");
        } else {
            assertNotEquals(pAlex, px);
        }
        checkInCache(Person.class, pAlex);
        checkInCache(Person.class, pPeter);


        em.close();

        em = emf.createEntityManager();
        System.out.println("Checking in new EM");
        checkInCache(Person.class, pAlex);
        checkInCache(Person.class, pPeter);
        checkInCache(Person.class, pTom);


        nanos = System.nanoTime();
        pWarmup = em.find(Person.class, pTom.getId());
        delta = System.nanoTime() - nanos;
        System.out.printf("Initial access in %.2f ms\n", delta / 1000000.0);
        assertEquals(pTom, pWarmup);

        nanos = System.nanoTime();
        pCache = em.find(Person.class, pPeter.getId());
        delta = System.nanoTime() - nanos;
        System.out.printf("Retrieved (L2 cache) in %.2f ms\n", delta/1000000.0);
        if (pPeter == pCache) {
            System.out.println("Reference equal vals!");
        } else {
            System.out.println("References not equals, please check cache settings");
        }
        assertEquals(pPeter, pCache);
        checkInCache(Person.class, pAlex);
        checkInCache(Person.class, pPeter);
        nanos = System.nanoTime();

        pCache = em.find(Person.class, pPeter.getId());
        delta = System.nanoTime() - nanos;
        System.out.printf("Retrieved (L1 cache) in %.2f ms\n", delta/1000000.0);
        if (pPeter == pCache) {
            System.out.println("Reference equal vals!");
        } else {
            System.out.println("References not equals, please check cache settings");
        }
        assertEquals(pPeter, pCache);
        checkInCache(Person.class, pAlex);
        checkInCache(Person.class, pPeter);
        em.close();


        System.out.println("From DB: " + p2);
        assertTrue("Shall be different objects", pAlex != p2);
        assertEquals("Shall be equal!", pAlex, p2);
    }
}
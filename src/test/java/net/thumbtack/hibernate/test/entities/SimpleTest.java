package net.thumbtack.hibernate.test.entities;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Created by asamolov on 11/09/14.
 */
public class SimpleTest extends EntityTest {
    protected final static Logger log = LoggerFactory.getLogger(SimpleTest.class);

    @Test
    public void testGet() throws Exception {
        Person p = new Person("GetTest");

        dl();        EntityManager em = emf.createEntityManager();
        dl();        em.getTransaction().begin();
        dl();        em.persist(p);
        dl();        em.getTransaction().commit();
        dl();        em.close();

        dl();        em = emf.createEntityManager();
        dl();        Person p2 = em.find(Person.class, p.getId());
        dl();        log.debug("Person saved DB: {}", p);
        dl();        log.debug("Person from DB: {}", p2);
        dl();        em.close();

        dl();        em = emf.createEntityManager();
        dl();        p2 = em.find(Person.class, p.getId());
        dl();        log.debug("Person saved DB: {}", p);
        dl();        log.debug("Person from DB: {}", p2);
        dl();        em.close();

    }

    @Test
    public void testQuery() throws Exception {
                     Person p = new Person("Alex");
        dl();        EntityManager em = emf.createEntityManager();
        dl();        em.getTransaction().begin();
        dl();        em.persist(p);
        dl();        em.getTransaction().commit();
        dl();        em.close();
        dl();        em = emf.createEntityManager();
                     CriteriaQuery<Person> cb = em.getCriteriaBuilder().createQuery(Person.class);
                     Root<Person> root = cb.from(Person.class);
                     cb.select(root);

        dl();        List<Person> list = em.createQuery(cb).getResultList();
        dl();        p = list.get(0);
        dl();        em.close();

        dl();        em = emf.createEntityManager();
        dl();        Person p2 = em.find(Person.class, p.getId());
        dl();        log.debug("Person saved DB: {}", p);
        dl();        log.debug("Person from DB: {}", p2);
        dl();        em.close();

    }

    @Test
    public void testWriteChild() throws Exception {
        Person p = new Person("ChildTest");
        p.newOrder("xxx");
        p.newOrder("yyy");

        dl();        EntityManager em = emf.createEntityManager();
        dl();        em.getTransaction().begin();
        dl();        em.persist(p);
        dl();        em.getTransaction().commit();
        dl();        em.close();

        TimeUnit.MILLISECONDS.sleep(100);
        System.out.println("Turn #0");
        dl();        em = emf.createEntityManager();
        dl();        Person p2 = em.find(Person.class, p.getId());
        dl();        log.debug("Person saved DB: {}", p);
        dl();        log.debug("Person from DB: {}", p2);
        dl();        em.close();

        TimeUnit.MILLISECONDS.sleep(50);
        System.out.println("Turn #1");
        dl();        em = emf.createEntityManager();
        dl();        p2 = em.find(Person.class, p.getId());
        dl();        log.debug("Person saved DB: {}", p);
        dl();        log.debug("Person from DB: {}", p2);
        dl();        em.close();

    }

}

package net.thumbtack.hibernate.test.entities;

import org.junit.Test;

import javax.persistence.EntityManager;
import java.util.Date;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class OrderTest extends EntityTest {

    @Test
    public void testGetPerson() throws Exception {
        EntityManager em = emf.createEntityManager();

        Person p = new Person();
        p.setName("Buyer");

        Order o = new Order();
        o.setPerson(p);
        o.setDetails("lalala");
        o.setPlaced(new Date());
        o.setShipped(new Date());
        o.setStatus(StatusEnum.NEW);

        em.getTransaction().begin();
        em.persist(p);
        p.addOrder(o);
        //em.persist(o);
        em.getTransaction().commit();
        em.close();

        //emf.getCache().evictAll();

        em = emf.createEntityManager();

        Order o3 = em.find(Order.class, o.getId());
        assertEquals(o.getDetails(), o3.getDetails());

        Person p2 = em.find(Person.class, p.getId());

        List<Order> orders = p2.getOrders();
        assertEquals(1, orders.size());

        Order o2 = orders.get(0);
        assertTrue(o != o2);
        assertEquals(o.getDetails(), o2.getDetails());

        em.close();


        int beforeRemove, afterRemove;
        em = emf.createEntityManager();
        em.getTransaction().begin();
        p = em.find(Person.class, p.getId());
        beforeRemove = p.getOrders().size();
        o = p.getOrders().get(0);
        p.removeOrder(o);
        em.getTransaction().commit();
        em.close();

        //assertTrue(checkInCache(Person.class, p));

        em = emf.createEntityManager();
        p = em.find(Person.class, p.getId());
        afterRemove = p.getOrders().size();
        em.close();
        assertEquals(1, beforeRemove - afterRemove);

    }
}
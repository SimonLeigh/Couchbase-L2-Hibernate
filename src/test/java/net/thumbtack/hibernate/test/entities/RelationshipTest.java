package net.thumbtack.hibernate.test.entities;

import org.junit.Ignore;
import org.junit.Test;

import javax.persistence.EntityManager;
import java.util.Date;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;

/**
 * RelationshipTest
 */
public class RelationshipTest extends EntityTest {

    @Test
    @Ignore
    public void testRemoveChild() throws Exception {
        Person person = new Person();
        person.setName("personName");
        person.setSurname("personSurname");

        Order order = new Order();
        order.setDetails("Details");
        order.setPerson(person);
        order.setPlaced(new Date());
        insertEntity(order);

        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();
        Order orderToBeRemoved = em.merge(order);
        em.remove(orderToBeRemoved);
        em.getTransaction().commit();
        em.close();
        em = emf.createEntityManager();
        Person findPerson = em.find(Person.class, person.getId());
        em.close();
        // will fail, since no cascade delete
        assertNull(findPerson);
    }

    @Test
    public void testRemoveChildFromParent() throws Exception {
        Person person = new Person();
        person.setName("personName");
        person.setSurname("personSurname");

        Order order = new Order();
        order.setDetails("Details");
        order.setPerson(person);
        order.setPlaced(new Date());
        insertEntity(order);

        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();
        person.removeOrder(order);
        em.merge(person);
        em.getTransaction().commit();
        em.close();
        em = emf.createEntityManager();
        order = em.find(Order.class, order.getId());
        em.close();
        assertNull(order);
    }

    @Test
    public void testDeleteParent() throws Exception {
        final int COUNT_ORDERS = 10;
        EntityManager em;
        Person person = new Person();
        person.setName("personName");
        person.setSurname("personSurname");
        /*insertEntity(person);
        EntityManager em = emf.createEntityManager();
        person = em.find(Person.class, person.getId());
        em.close();
        System.out.println("person = " + person);*/

        Order[] orders = new Order[COUNT_ORDERS];
        for (int i = 0; i < COUNT_ORDERS; i++) {
            System.out.println("i = " + i);
            orders[i] = new Order();
            orders[i].setDetails("Details" + i);
            orders[i].setPerson(person);
            orders[i].setPlaced(new Date());
        }

        insertEntity(person);

        em = emf.createEntityManager();
        em.getTransaction().begin();
        person.removeAllOrders();
        em.merge(person);
        em.getTransaction().commit();
        em.close();

        for (int i = 0; i < COUNT_ORDERS; i++) {
            // will fail, since deleted record is still in cache
            //assertFalse(checkInCache(Order.class, orders[i]));
            em = emf.createEntityManager();
            Order order = em.find(Order.class, orders[i].getId());
            em.close();
            assertNull(order);
        }
    }
}

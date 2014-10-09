package net.thumbtack.hibernate.test.entities;

import org.junit.Test;

import javax.persistence.EntityManager;

/**
 * CrateOrderWithItems
 */

public class CrateOrderWithItemsTest extends EntityTest {

    public static final String personName = "Jim";

    @Test
    public void testCreateOrderWithItem() throws Exception{
        Person person = CreatePersonTest.createPerson(personName);
        Order order = CreateOrderTest.createOrder(person);
        Item item = CreateItemTest.createSomeItem();
        order.addItem(item, 140);
        order.addItem(item, 1);
        person.addOrder(order);

            EntityManager em = emf.createEntityManager();
            em.getTransaction().begin();
        em.persist(item);
        em.persist(order);
            em.getTransaction().commit();
        em.close();
    }
}

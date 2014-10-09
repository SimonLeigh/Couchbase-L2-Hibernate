package net.thumbtack.hibernate.test.entities;

import org.junit.Test;

import javax.persistence.EntityManager;
import java.util.Date;

import static org.junit.Assert.*;

/**
 * CreateOrderTest
 */
public class CreateOrderTest extends EntityTest {
    public static final String SOME_NAME = "someName";
    public static final String SOME_DETAILS = "someDetails";
    public static final Date SOME_DATE = new Date();
    public static final StatusEnum SOME_STATUS = StatusEnum.PLACED;

    public static Order createOrder(Person person, Date placed, String details, StatusEnum status) {
        Order order = new Order();
        order.setPerson(person);
        order.setPlaced(placed);
        order.setDetails(details);
        order.setStatus(status);

        return order;
    }

    public static Order createOrder(Person person) {
        Order order = new Order();
        order.setPerson(person);
        order.setPlaced(SOME_DATE);
        order.setDetails(SOME_DETAILS);
        order.setStatus(SOME_STATUS);

        return order;
    }

    public static Order findOrder(int id) {
        EntityManager em = emf.createEntityManager();
        Order foundOrder = em.find(Order.class, id);
        em.close();
        return foundOrder;
    }

    public static Order createSomeOrder() {
        return createOrder(CreatePersonTest.createPerson(SOME_NAME), SOME_DATE, SOME_DETAILS, SOME_STATUS);
    }

    @Test
    public void testCreateOrderAndIdentityFind() {
        Order order = createSomeOrder();
        order.getPerson().addOrder(order);
        insertEntity(order.getPerson());
        Order orderRead_1 = findOrder(order.getId());
        Order orderRead_2 = findOrder(order.getId());
        assertEquals(orderRead_1, orderRead_2);
    }

    @Test
    public void testCreateOrderAndFindInCache() throws Exception {
        Order order = createSomeOrder();
        order.getPerson().addOrder(order);
        insertEntity(order.getPerson());
        assertTrue(checkInCache(Order.class, order));
        assertEquals(order, findOrder(order.getId()));
    }

    @Test
    public void testIdentityAndNoIdentityTest() throws Exception {
        Order order = createSomeOrder();
        order.getPerson().addOrder(order);
        insertEntity(order.getPerson());

        EntityManager em = emf.createEntityManager();
        Order readOrder_1 =  em.find(Order.class, order.getId());
        Order readOrder_2 =  em.find(Order.class, order.getId());
        em.close();
        assertEquals(order, readOrder_1);
        assertTrue(readOrder_1 == readOrder_2);
        assertTrue(readOrder_1.equals(readOrder_2));

        em = emf.createEntityManager();
        readOrder_1 =  em.find(Order.class, order.getId());
        em.close();
        em = emf.createEntityManager();
        readOrder_2 =  em.find(Order.class, order.getId());
        em.close();
        assertFalse(readOrder_1 == readOrder_2);
        assertTrue(readOrder_1.equals(readOrder_2));
    }
}

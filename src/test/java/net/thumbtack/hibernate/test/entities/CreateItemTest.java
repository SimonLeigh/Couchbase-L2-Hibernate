package net.thumbtack.hibernate.test.entities;

import org.junit.Ignore;
import org.junit.Test;

import javax.persistence.EntityManager;

import static org.junit.Assert.*;

/**
 * CreateItemTest
 */
public class CreateItemTest extends EntityTest {
    public static final String SOME_NAME = "someName";
    public static final String SOME_MANUFACTURER = "someManufacturer";
    public static final String SOME_DESCRIPTION = "someDescription";
    public static final double SOME_PRICE = 199.23;

            public static Item createItem(String name, String manufacturer, String description, double price) {
        Item item = new Item();
        item.setName(name);
        item.setManufacturer(manufacturer);
        item.setDescription(description);
        item.setPrice(price);
        return item;
    }

    public static Item findItem(int id) {
        EntityManager em = emf.createEntityManager();
        Item foundItem = em.find(Item.class, id);
        em.close();
        return foundItem;
    }

    public static Item createSomeItem() {
        return createItem(SOME_NAME, SOME_MANUFACTURER, SOME_DESCRIPTION, SOME_PRICE);
    }

    @Test
    public void testCreateItemAndIdentityFind() {
        Item item = createSomeItem();
        insertEntity(item);
        Item itemRead_1 = findItem(item.getId());
        Item itemRead_2 = findItem(item.getId());
        assertEquals(itemRead_1, itemRead_2);
    }

    @Test
    public void testCreateItemAndFindInCache() throws Exception {
        Item item = createSomeItem();
        insertEntity(item);
        assertTrue(checkInCache(Item.class, item));
        assertEquals(findItem(item.getId()), item);
    }

    @Test
    public void testCreateItemAndFindInDB() throws Exception {
        Item item = createSomeItem();
        insertEntity(item);
        emf.getCache().evict(Item.class, item.getId());
        //clearCache();
        assertFalse(checkInCache(Item.class, item));
        assertEquals(findItem(item.getId()), item);
    }

    @Test
    public void testIdentityAndNoIdentityTest() throws Exception {
        Item item = createSomeItem();
        insertEntity(item);

        EntityManager em = emf.createEntityManager();
        Item readItem_1 =  em.find(Item.class, item.getId());
        Item readItem_2 =  em.find(Item.class, item.getId());
        em.close();
        assertTrue(item.equals(readItem_1));
        assertTrue(readItem_1 == readItem_2);
        assertTrue(readItem_1.equals(readItem_2));

        em = emf.createEntityManager();
        readItem_1 =  em.find(Item.class, item.getId());
        em.close();
        em = emf.createEntityManager();
        readItem_2 =  em.find(Item.class, item.getId());
        em.close();
        assertFalse(readItem_1 == readItem_2);
        assertTrue(readItem_1.equals(readItem_2));
    }

    @Test
    public void generateItems() throws Exception {
        Item item;
        for (int i = 0; i < 10; i++) {
            item = createItem("name" + i, "manufacturer" + i, "descr" + i, Math.random() * 100.0);
            insertEntity(item);
        }

    }
}

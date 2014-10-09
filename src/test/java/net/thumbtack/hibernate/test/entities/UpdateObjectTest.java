package net.thumbtack.hibernate.test.entities;

import org.junit.Test;

import javax.persistence.EntityManager;

import static org.junit.Assert.*;

/**
 * UpdatingObjectTest
 */
public class UpdateObjectTest extends EntityTest {

    private final static int COUNT_UPDATE = 10;

    @Test
    public void testUpdatePersonAndCheckCache() throws Exception {
        Person person = CreatePersonTest.createSomePerson();
        assertFalse(checkInCache(Person.class, person));
        insertEntity(person);
        assertTrue(checkInCache(Person.class, person));

        for (int i = 0; i < COUNT_UPDATE; i++) {

            EntityManager entityManager = emf.createEntityManager();
            entityManager.getTransaction().begin();
            person.setSurname(String.valueOf(i));
            entityManager.merge(person);
            entityManager.getTransaction().commit();
            entityManager.close();

            assertTrue(checkInCache(Person.class, person));
            EntityManager em = emf.createEntityManager();
            Person getPerson = em.find(Person.class, person.getId());
            em.close();
            assertEquals(getPerson, person);
            assertTrue(getPerson != person);
        }
    }
}

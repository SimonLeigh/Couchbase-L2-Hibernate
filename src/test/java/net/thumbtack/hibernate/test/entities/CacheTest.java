package net.thumbtack.hibernate.test.entities;

import org.junit.Ignore;
import org.junit.Test;

import javax.persistence.EntityManager;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;

/**
 * CacheTest
 */
public class CacheTest extends EntityTest {

    public static final String NAME = "Nick";
    public static final String SURNAME = "Apie";

    @Test
    @Ignore
    public void testClearCache() {
        Person person = CreatePersonTest.createPerson(NAME, SURNAME);
        insertEntity(person);
        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();
        Person personToBeRemoved = em.merge(person);
        em.remove(personToBeRemoved);
        em.getTransaction().commit();
        em.close();
        // Can be true, since the cache is not explicitely cleared.
        assertFalse(checkInCache(Person.class, person));
        em = emf.createEntityManager();
        Person nullPerson = em.find(Person.class, person.getId());
        em.close();
        assertNull(nullPerson);
    }
}

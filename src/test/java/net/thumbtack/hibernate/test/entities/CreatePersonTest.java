package net.thumbtack.hibernate.test.entities;

/**
 * CreateRecordTest
 */

import org.junit.Test;

import javax.persistence.EntityManager;

import static org.junit.Assert.*;

public class CreatePersonTest extends EntityTest {

    public static final String SOME_NAME = "someName";
    public static final String SOME_SURNAME = "someSurname";

    public static Person createSomePerson() {
        return createPerson(SOME_NAME, SOME_SURNAME);
    }

    public static Person createPerson(String name) {
        Person person = new Person();
        person.setName(name);
        return person;
    }

    public static Person createPerson(String name, String surname) {
        Person person = createPerson(name);
        person.setSurname(surname);
        return person;
    }

    public static Person findPerson(int id) {
        EntityManager em = emf.createEntityManager();
        Person foundPerson = em.find(Person.class, id);
        em.close();
        return foundPerson;
    }

    @Test
    public void testCreatePersonAndIdentityFind() throws Exception {
        Person person = createSomePerson();
        insertEntity(person);
        //emf.getCache().evictAll();
        clearCache();
        EntityManager entityManager = emf.createEntityManager();
        Person personRead_1 = entityManager.find(Person.class, person.getId());
        Person personRead_2 = entityManager.find(Person.class, person.getId());
        entityManager.close();
        assertEquals(personRead_1, personRead_2);
    }

    @Test
    public void testCreatePersonAndFindInCache() throws Exception {
        Person person = createSomePerson();
        insertEntity(person);
        assertTrue(checkInCache(Person.class, person));
        assertEquals(findPerson(person.getId()), person);
    }

    @Test
    public void testCreatePersonAndFindInDB() throws Exception {
        Person person = createSomePerson();
        insertEntity(person);
        emf.getCache().evict(Person.class, person.getId());
        //clearCache();
        assertFalse(checkInCache(Person.class, person));
        assertEquals(findPerson(person.getId()), person);
    }

    @Test
    public void testIdentityAndNoIdentityTest() throws Exception {
        Person person = createSomePerson();
        insertEntity(person);

        EntityManager em = emf.createEntityManager();
        Person readPerson_1 =  em.find(Person.class, person.getId());
        Person readPerson_2 =  em.find(Person.class, person.getId());
        em.close();
        assertTrue(person.equals(readPerson_1));
        assertTrue(readPerson_1 == readPerson_2);
        assertTrue(readPerson_1.equals(readPerson_2));

        em = emf.createEntityManager();
        readPerson_1 =  em.find(Person.class, person.getId());
        em.close();
        em = emf.createEntityManager();
        readPerson_2 =  em.find(Person.class, person.getId());
        em.close();
        assertTrue(readPerson_1 != readPerson_2);
        assertTrue(readPerson_1.equals(readPerson_2));
    }
}
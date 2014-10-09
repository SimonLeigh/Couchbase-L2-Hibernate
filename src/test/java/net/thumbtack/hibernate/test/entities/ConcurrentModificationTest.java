package net.thumbtack.hibernate.test.entities;

import org.junit.Test;

import javax.persistence.EntityManager;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.CyclicBarrier;

import static org.junit.Assert.assertTrue;

/**
 * ConcurrentModificationTest
 */
public class ConcurrentModificationTest extends EntityTest {

    public static final String CHANGED_SURNAME_1 = "changed surname 1";
    public static final String CHANGED_SURNAME_2 = "changed surname 2";
    public static final String CHANGED_NAME = "Changed name";
    public static final int COUNT_THREAD = 4;
    public static final String NAME = "Oleg";
    public static final String SURNAME = "Olegov";

    @Test
    public void testChangeSeveralFieldsInDifferentTransactions() throws Exception {
        Person person = new Person();
        person.setName(NAME);
        person.setSurname(SURNAME);
        insertEntity(person);

        EntityManager em_1 = emf.createEntityManager();
        Person person_1 = em_1.find(Person.class, person.getId());
        EntityManager em_2 = emf.createEntityManager();
        Person person_2 = em_2.find(Person.class, person.getId());

        em_1.getTransaction().begin();
        em_2.getTransaction().begin();

        person_1.setSurname(CHANGED_SURNAME_1);
        person_2.setName(CHANGED_NAME);
        em_1.merge(person_1);
        em_2.merge(person_2);
        em_2.getTransaction().commit();
        em_1.getTransaction().commit();

        em_1.close();
        em_2.close();

        person.setName(CHANGED_NAME);
        person.setSurname(CHANGED_SURNAME_1);
        assertTrue(checkInCache(Person.class, person));
    }

    @Test
    public void testChangeOneFieldInDifferentTransactions() throws Exception {
        Person person = new Person();
        person.setName(NAME);
        person.setSurname(SURNAME);
        insertEntity(person);

        EntityManager em_1 = emf.createEntityManager();
        Person person_1 = em_1.find(Person.class, person.getId());
        EntityManager em_2 = emf.createEntityManager();
        Person person_2 = em_2.find(Person.class, person.getId());

        em_1.getTransaction().begin();
        em_2.getTransaction().begin();

        person_1.setSurname(CHANGED_SURNAME_1);
        person_2.setSurname(CHANGED_SURNAME_2);
        em_1.merge(person_1);
        em_2.merge(person_2);
        em_2.getTransaction().commit();
        em_1.getTransaction().commit();

        em_1.close();
        em_2.close();

        person.setSurname(CHANGED_SURNAME_1);
        assertTrue(checkInCache(Person.class, person));
    }

    @Test
    public void testConcurrentChangeSeveralFieldInDifferentThreads() throws Exception {
        final Person person = new Person();
        person.setName(NAME);
        person.setSurname(SURNAME);
        insertEntity(person);

        final ChangerFieldsOfPersonThread[] changerFieldsOfPersonThreads = new ChangerFieldsOfPersonThread[COUNT_THREAD];

        CountDownLatch startSignal = new CountDownLatch(1);
        CountDownLatch doneSignal = new CountDownLatch(COUNT_THREAD);
        CyclicBarrier continueSignalBarrier = new CyclicBarrier(COUNT_THREAD);
        for (int i = 0; i < COUNT_THREAD; i++) {
            if (i % 2 == 0) {
                changerFieldsOfPersonThreads[i] = new ChangerFieldsOfPersonThread(startSignal, doneSignal, continueSignalBarrier, person.getId(), "name");
            } else {
                changerFieldsOfPersonThreads[i] = new ChangerFieldsOfPersonThread(startSignal, doneSignal, continueSignalBarrier, person.getId(), "surname");
            }
            changerFieldsOfPersonThreads[i].start();
        }
        startSignal.countDown();
        doneSignal.await();

        String[] winner = getWinner(person.getId());
        String winnerName = winner[0];
        String winnerSurname = winner[1];

        person.setName(winnerName);
        person.setSurname(winnerSurname);
        assertTrue(checkInCache(Person.class, person));
    }

    private String[] getWinner(int id) throws Exception {
        String[] winner = new String[2];
        Class.forName("org.postgresql.Driver");
        String username = String.valueOf(emf.getProperties().get("javax.persistence.jdbc.user"));
        //String password = String.valueOf(emf.getProperties().get("javax.persistence.jdbc.password"));
        String password = "12345";
        String url = String.valueOf(emf.getProperties().get("javax.persistence.jdbc.url"));
        Connection connection = DriverManager.getConnection(url, username, password);
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery("SELECT * FROM Person where id = " + id);
        resultSet.next();
        String winnerName = resultSet.getString("name");
        String winnerSurname = resultSet.getString("surname");
        connection.close();

        winner[0] = winnerName;
        winner[1] = winnerSurname;

        return winner;
    }

    @Test
    public void testConcurrentChangeOneFieldInDifferentThreads() throws Exception {
        final Person person = new Person();
        person.setName(NAME);
        person.setSurname(SURNAME);
        insertEntity(person);

        final ChangerFieldsOfPersonThread[] changerFieldsOfPersonThreads = new ChangerFieldsOfPersonThread[COUNT_THREAD];

        CountDownLatch startSignal = new CountDownLatch(1);
        CountDownLatch doneSignal = new CountDownLatch(COUNT_THREAD);
        CyclicBarrier continueSignalBarrier = new CyclicBarrier(COUNT_THREAD);
        for (int i = 0; i < COUNT_THREAD; i++) {
            changerFieldsOfPersonThreads[i] = new ChangerFieldsOfPersonThread(startSignal, doneSignal, continueSignalBarrier, person.getId(), "name");
            changerFieldsOfPersonThreads[i].start();
        }
        startSignal.countDown();
        doneSignal.await();

        String[] winner = getWinner(person.getId());
        String winnerName = winner[0];
        String winnerSurname = winner[1];

        person.setName(winnerName);
        person.setSurname(winnerSurname);
        assertTrue(checkInCache(Person.class, person));
    }

    private class ChangerFieldsOfPersonThread extends Thread {
        private final CyclicBarrier continueSignalBarrier;
        private final CountDownLatch startSignal;
        private final CountDownLatch doneSignal;
        private EntityManager em;
        private Person person;
        private final String field;

        public ChangerFieldsOfPersonThread(CountDownLatch startSignal, CountDownLatch doneSignal, CyclicBarrier continueSignalBarrier, int personId, String field) {
            this.startSignal = startSignal;
            this.doneSignal = doneSignal;
            this.continueSignalBarrier = continueSignalBarrier;
            this.field = field;
            readPerson(personId);
        }

        @Override
        public void run() {
            try {
                startSignal.await();
                em = emf.createEntityManager();
                em.getTransaction().begin();
                if (field.equals("name")) {
                    person.setName(this.getName());
                }
                if (field.equals("surname")) {
                    person.setSurname(this.getName());
                }
                em.merge(person);
                continueSignalBarrier.await();
                em.getTransaction().commit();
                em.close();
                doneSignal.countDown();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (BrokenBarrierException e) {
                e.printStackTrace();
            }
        }

        private void readPerson(int personId) {
            em = emf.createEntityManager();
            person = em.find(Person.class, personId);
            em.close();
        }
    }
}

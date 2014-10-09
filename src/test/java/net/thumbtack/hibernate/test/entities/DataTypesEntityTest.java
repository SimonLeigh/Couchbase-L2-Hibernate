package net.thumbtack.hibernate.test.entities;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.postgresql.net.PGinet;
import org.postgresql.util.PGmoney;

import javax.persistence.EntityManager;
import java.util.Date;
import java.util.UUID;

import static org.junit.Assert.assertTrue;

/**
 * DataTypesEntityTest.
 */
public class DataTypesEntityTest extends EntityTest {

    Date _date;
    String _json;
    PGmoney _money;
    char _char;
    int _int;
    PGinet _inet;
    boolean _boolean;
    long _long;
    float _float;
    double _double;
    String _text;
    UUID _uuid;

    @Before
    public void init() throws Exception{
        _date = new Date();
        //_json = "{\"name\":\"MyNode\", \"width\":200, \"height\":100}";
        //_money = new PGmoney("10034654645654365634563463456546456");
        _boolean = true;
        _long = 1237489271348L;
        _float = 199.071239f;
        _char = 'j';
        _int = 10;
        _inet = new PGinet("192.1.1.2");
        _double = 0.1234123324;
        _text = "some text";
        //_uuid = UUID.randomUUID();
    }

    @Test
    @Ignore
    public void testCreateDataTypesEntity() throws Exception {
        DataTypes dataTypes = new DataTypes();
        dataTypes.setaDouble(_double);
        dataTypes.setaChar(_char);
        dataTypes.setaBoolean(_boolean);
        dataTypes.setaFloat(_float);
        dataTypes.setaLong(_long);
        dataTypes.setDate(_date);
        dataTypes.setInteger(_int);
        dataTypes.setInetAddress(_inet);
        dataTypes.setString(_text);

        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();
        em.persist(dataTypes);
        em.getTransaction().commit();
        em.close();

        em = emf.createEntityManager();
        DataTypes dt = em.find(DataTypes.class, dataTypes.getId());
        em.close();

        System.out.println("dt = " + dt);

        assertTrue(checkInCache(DataTypes.class, dataTypes));
    }
}

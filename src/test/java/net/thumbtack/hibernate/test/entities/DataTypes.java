package net.thumbtack.hibernate.test.entities;

//import org.eclipse.persistence.internal.helper.DatabaseField;
//import org.eclipse.persistence.mappings.DatabaseMapping;
//import org.eclipse.persistence.mappings.DirectCollectionMapping;
//import org.eclipse.persistence.mappings.converters.Converter;
//import org.eclipse.persistence.sessions.Session;
//import org.postgresql.util.PGobject;

import org.hibernate.HibernateException;
import org.hibernate.annotations.*;
import org.hibernate.engine.spi.SessionImplementor;
import org.postgresql.net.PGinet;

import javax.persistence.*;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.io.Serializable;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.Date;

//import javax.persistence.Converter;

/**
 * DataTypes.
 */
@Entity
@Table(name = "Data_Types")
@TypeDef(name = "inet", typeClass = DataTypes.InetType.class)
@Cacheable
@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class DataTypes implements EntityWithId, Serializable {
    @Column(name = "_boolean")
    private Boolean aBoolean;
    @Column(name = "_char")
    private char aChar;
    @Column(name = "_double")
    private Double aDouble;
    @Column(name = "_float")
    private Float aFloat;
    @Column(name = "_long")
    private Long aLong;
    @Column(name = "_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date date;
    @Id
    @SequenceGenerator(name = "data_types_generator", sequenceName = "data_types_id_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "data_types_generator")
    private int id;
    @Column(name = "_inet")
    //@org.eclipse.persistence.annotations.Convert("inetConverter")
    @Type(type = "inet")
    private PGinet inetAddress;
    @Column(name = "_int")
    private Integer integer;
    @Column(name = "_text")
    private String string;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        DataTypes dataTypes = (DataTypes) o;

        if (aChar != dataTypes.aChar) return false;
        if (id != dataTypes.id) return false;
        if (aBoolean != null ? !aBoolean.equals(dataTypes.aBoolean) : dataTypes.aBoolean != null) return false;
        if (aDouble != null ? !aDouble.equals(dataTypes.aDouble) : dataTypes.aDouble != null) return false;
        if (aFloat != null ? !aFloat.equals(dataTypes.aFloat) : dataTypes.aFloat != null) return false;
        if (aLong != null ? !aLong.equals(dataTypes.aLong) : dataTypes.aLong != null) return false;
        if (date != null ? !date.equals(dataTypes.date) : dataTypes.date != null) return false;
        if (inetAddress != null ? !inetAddress.equals(dataTypes.inetAddress) : dataTypes.inetAddress != null)
            return false;
        if (integer != null ? !integer.equals(dataTypes.integer) : dataTypes.integer != null) return false;
        if (string != null ? !string.equals(dataTypes.string) : dataTypes.string != null) return false;

        return true;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    @Override
    public Object getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public PGinet getInetAddress() {
        return inetAddress;
    }

    public void setInetAddress(PGinet inetAddress) {
        this.inetAddress = inetAddress;
    }

    public Integer getInteger() {
        return integer;
    }

    public void setInteger(Integer integer) {
        this.integer = integer;
    }

    public String getString() {
        return string;
    }

    public void setString(String string) {
        this.string = string;
    }

    public Boolean getaBoolean() {
        return aBoolean;
    }

    public void setaBoolean(Boolean aBoolean) {
        this.aBoolean = aBoolean;
    }

    public char getaChar() {
        return aChar;
    }

    public void setaChar(char aChar) {
        this.aChar = aChar;
    }

    public Double getaDouble() {
        return aDouble;
    }

    public void setaDouble(Double aDouble) {
        this.aDouble = aDouble;
    }

    public Float getaFloat() {
        return aFloat;
    }

    public void setaFloat(Float aFloat) {
        this.aFloat = aFloat;
    }

    public Long getaLong() {
        return aLong;
    }

    public void setaLong(Long aLong) {
        this.aLong = aLong;
    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + (date != null ? date.hashCode() : 0);
        result = 31 * result + (int) aChar;
        result = 31 * result + (integer != null ? integer.hashCode() : 0);
        result = 31 * result + (inetAddress != null ? inetAddress.hashCode() : 0);
        result = 31 * result + (aBoolean != null ? aBoolean.hashCode() : 0);
        result = 31 * result + (aLong != null ? aLong.hashCode() : 0);
        result = 31 * result + (aFloat != null ? aFloat.hashCode() : 0);
        result = 31 * result + (aDouble != null ? aDouble.hashCode() : 0);
        result = 31 * result + (string != null ? string.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "DataTypes{" +
                "id=" + id +
                ", date=" + date +
                ", aChar=" + aChar +
                ", integer=" + integer +
                ", inetAddress=" + inetAddress +
                ", aBoolean=" + aBoolean +
                ", aLong=" + aLong +
                ", aFloat=" + aFloat +
                ", aDouble=" + aDouble +
                ", string='" + string + '\'' +
                '}';
    }

    public static class InetType implements org.hibernate.usertype.UserType{

        private static final int[] SQL_TYPES = {Types.OTHER};

        @Override
        public int[] sqlTypes() {
            return SQL_TYPES;
        }

        @Override
        public Class returnedClass() {
            return PGinet.class;
        }

        @Override
        public boolean equals(Object arg0, Object arg1) throws HibernateException {
            return arg0.equals(arg1);
        }

        @Override
        public int hashCode(Object arg0) throws HibernateException {
            return arg0.hashCode();
        }

        @Override
        public Object nullSafeGet(ResultSet resultSet, String[] strings, SessionImplementor sessionImplementor, Object o) throws HibernateException, SQLException {
            PGinet grade = new PGinet(resultSet.getString(strings[0]));
            return resultSet.wasNull() ? null : grade;
        }

        @Override
        public void nullSafeSet(PreparedStatement preparedStatement, Object o, int i, SessionImplementor sessionImplementor) throws HibernateException, SQLException {
            System.out.println("o = " + o);
            System.out.println("o.getClass() = " + o.getClass());
            if (o == null){
                preparedStatement.setNull(i, Types.OTHER );
            }else{
                preparedStatement.setObject(i, o, Types.OTHER );
            }
            System.out.println("preparedStatement = " + preparedStatement);
        }

        @Override
        public Object deepCopy(Object arg0) throws HibernateException {
            return arg0;
        }

        @Override
        public boolean isMutable() {
            return false;
        }

        @Override
        public Serializable disassemble(Object value) throws HibernateException {
            return (Serializable) value;
        }

        @Override
        public Object assemble(Serializable arg0, Object arg1) throws HibernateException {
            return arg0;
        }

        @Override
        public Object replace(Object arg0, Object arg1, Object arg2) throws HibernateException {
            return arg0;
        }
    }

}

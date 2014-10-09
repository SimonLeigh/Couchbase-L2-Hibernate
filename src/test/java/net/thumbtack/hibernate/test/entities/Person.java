package net.thumbtack.hibernate.test.entities;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.Cacheable;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

/**
 * Created by asamolov on 08/09/14.
 */
@Entity
@NamedQuery(name = "findPersonById", query = "from Person p where p.id = :id")
@Cacheable
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Person implements EntityWithId {
    @Id
    @SequenceGenerator(name = "person_generator", sequenceName = "person_id_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "person_generator")
    private int id;

    private String name;

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    @Column(nullable = true) /* changed for hibernate */
    private String surname;

    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @OneToMany(mappedBy = "person", fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Order> orders = new ArrayList<Order>();

    public Person() {

    }

    public Person(Person person) {
        this.id = person.id;
        this.name = person.name;
        this.surname = person.surname;
        this.orders = person.orders;
    }

    public Person(final String name) {
        this.name = name;
    }

    public void addOrder(Order o) {
        if (o.getPerson() != this) {
            throw new IllegalArgumentException("Wrong owner of the order, please explicitely set to this");
        }
        if (!orders.contains(o)) {
            orders.add(o);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Person person = (Person) o;

        if (id != person.id) return false;
        if (name != null ? !name.equals(person.name) : person.name != null) return false;
        //if (orders != null ? !orders.equals(person.orders) : person.orders != null) return false;
        if (surname != null ? !surname.equals(person.surname) : person.surname != null) return false;

        return true;
    }

    public Integer getId() {
        return id;
    }

    public void setId(final int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public List<Order> getOrders() {
        return Collections.unmodifiableList(orders);
    }

    public void setOrders(final List<Order> orders) {
        this.orders = orders;
    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + (name != null ? name.hashCode() : 0);
        return result;
    }

    public Order newOrder(String details) {
        Order o = new Order();
        o.setDetails(details);
        o.setStatus(StatusEnum.PLACED);
        o.setPerson(this);
        o.setPlaced(new Date());
        orders.add(o);
        return o;
    }

    public void removeOrder(Order o) {
        orders.remove(o);
    }

    public void removeAllOrders() {
        orders.clear();
    }

    @Override
    public String toString() {
        return "Person{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", surname='" + surname + '\'' +
                ", orders=" + orders +
                '}';
    }
}

package net.thumbtack.hibernate.test.entities;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.Cacheable;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by asamolov on 08/09/14.
 */
@Entity
@Table(name = "ORDR")
@Cacheable
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Order implements EntityWithId {
    private String details;
    @Id
    @SequenceGenerator(name = "ordr_generator", sequenceName = "ordr_id_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "ordr_generator")
    private int id;

    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @OneToMany(mappedBy = "order", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private List<OrderItem> items = new ArrayList<OrderItem>();

    public void addItem(Item item, int amount) {
        for (OrderItem orderItem : items) {
            if (orderItem.getItem().equals(item)) {
                orderItem.addAmount(amount);
                return;
            }
        }
        OrderItem orderItem = new OrderItem();
        orderItem.setItem(item);
        orderItem.setOrder(this);
        orderItem.setAmount(amount);
        items.add(orderItem);
    }

    @ManyToOne(optional = false, cascade = CascadeType.PERSIST)
    @JoinColumn(name = "person_id", nullable = false)
    private Person person;
    @Temporal(TemporalType.TIMESTAMP)
    @Column(insertable = false, nullable = false)
    private Date placed;
    @Temporal(TemporalType.TIMESTAMP)
    private Date shipped;
    private String status;

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        final Order order = (Order) o;

        if (id != order.id) return false;
        if (details != null ? !details.equals(order.details) : order.details != null) return false;
        if (person != null ? !person.equals(order.person) : order.person != null) return false;
        //if (placed != null ? !placed.equals(order.placed) : order.placed != null) return false;
        if (shipped != null ? !shipped.equals(order.shipped) : order.shipped != null) return false;
        if (status != null ? !status.equals(order.status) : order.status != null) return false;

        return true;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(final String details) {
        this.details = details;
    }

    public Integer getId() {
        return id;
    }

    public void setId(final int id) {
        this.id = id;
    }

    public List<OrderItem> getItems() {
        return items;
    }

    public void setItems(List<OrderItem> items) {
        this.items = items;
    }

    public Person getPerson() {
        return person;
    }

    public void setPerson(final Person person) {
        this.person = person;
        person.addOrder(this);
    }

    public Date getPlaced() {
        return placed;
    }

    public void setPlaced(final Date placed) {
        this.placed = placed;
    }

    public Date getShipped() {
        return shipped;
    }

    public void setShipped(final Date shipped) {
        this.shipped = shipped;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(final StatusEnum status) {
        this.status = status.getLowCase();
    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + (details != null ? details.hashCode() : 0);
        result = 31 * result + (status != null ? status.hashCode() : 0);
        //result = 31 * result + (placed != null ? placed.hashCode() : 0);
        result = 31 * result + (shipped != null ? shipped.hashCode() : 0);
        result = 31 * result + (person != null ? person.hashCode() : 0);
        return result;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "Order{" +
                "id=" + id +
                ", details='" + details + '\'' +
                ", status='" + status + '\'' +
                ", placed=" + placed +
                ", shipped=" + shipped +
                '}';
    }
}

package net.thumbtack.hibernate.test.entities;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.Cacheable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.SequenceGenerator;

/**
 * Created by asamolov on 08/09/14.
 */
@Entity
@NamedQuery(name = "getAllItems", query = "select i from Item i")
@Cacheable
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Item implements EntityWithId {
    private String description;
    @Id
    @SequenceGenerator(name = "item_generator", sequenceName = "item_id_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "item_generator")
    private int id;
    @Column(nullable = false)
    private String manufacturer;
    @Column(nullable = false)
    private String name;
    @Column(nullable = false)
    private double price;

    public void addOrder(Order order, int amount) {
        OrderItem orderItem = new OrderItem();
        orderItem.setItem(this);
        orderItem.setOrder(order);
        orderItem.setAmount(amount);
        order.getItems().add(orderItem);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Item item = (Item) o;

        if (id != item.id) return false;
        if (Double.compare(item.price, price) != 0) return false;
        if (!description.equals(item.description)) return false;
        if (!manufacturer.equals(item.manufacturer)) return false;
        if (!name.equals(item.name)) return false;

        return true;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(final String description) {
        this.description = description;
    }

    public Integer getId() {
        return id;
    }

    public void setId(final int id) {
        this.id = id;
    }

    public String getManufacturer() {
        return manufacturer;
    }

    public void setManufacturer(final String manufacturer) {
        this.manufacturer = manufacturer;
    }

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(final double price) {
        this.price = price;
    }

    @Override
    public int hashCode() {
        int result;
        long temp;
        result = id;
        result = 31 * result + name.hashCode();
        result = 31 * result + description.hashCode();
        result = 31 * result + manufacturer.hashCode();
        temp = Double.doubleToLongBits(price);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        return result;
    }

    @Override
    public String toString() {
        return "Item{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", manufacturer='" + manufacturer + '\'' +
                ", price=" + price +
                '}';
    }
}

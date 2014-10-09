package net.thumbtack.hibernate.test.entities;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.Cacheable;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.io.Serializable;

/**
 * OrderItem
 */
@Entity
@Table(name = "ORDR_ITEM")
@Cacheable
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class OrderItem implements Serializable {
    private int amount = 1;
    @Id
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "item_id", nullable = false)
    private Item item;

    @Id
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "ordr_id", nullable = false)
    private Order order;

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public Item getItem() {
        return item;
    }

    public void addAmount(int amount) {
        this.amount += amount;
    }

    public void setItem(Item item) {
        this.item = item;
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    @Override
    public String toString() {
        return "OrderItem{" +
                "amount=" + amount +
                ", item=" + item +
                ", order=" + order +
                '}';
    }

    /* Done for Hibernate */
    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        final OrderItem orderItem = (OrderItem) o;

        if (amount != orderItem.amount) return false;
        if (item != null ? !item.equals(orderItem.item) : orderItem.item != null) return false;
        if (order != null ? !order.equals(orderItem.order) : orderItem.order != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = amount;
        result = 31 * result + (item != null ? item.hashCode() : 0);
        result = 31 * result + (order != null ? order.hashCode() : 0);
        return result;
    }
}

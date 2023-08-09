package com.example.test.Entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OrderLine {
    private int quantity;

    //    @EmbeddedId
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private OrderLineKey id;
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Integer id;
    @ManyToOne
    @JoinColumn(name = "order_id")
    private Order order;

    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;

    @OneToMany(mappedBy = "orderLine")
    private List<MaintenanceOrder> maintenanceOrderList;

//    @Embeddable
//    @EqualsAndHashCode
//    @Setter
//    @Getter
//    public class OrderLineKey implements Serializable {
//        @Column(name = "order_id")
//        Integer orderId;
//
//        @Column(name = "product_id")
//        Integer productId;
//
//        // standard constructors, getters, and setters
//        // hashcode and equals implementation
//    }


    public OrderLine(String categoryName, int quantity, double price, int status) {
        this.product = new Product(price);
        this.product.setType(new Type(categoryName));
        this.quantity = quantity;
        this.order = new Order(status);
    }

    public OrderLine( int quantity, String productName, double price, int status, String name) {
        this.product = new Product(productName, price);
        this.quantity = quantity;
        this.order = new Order(status);
        this.product.setType(new Type(name));
    }

    @Override
    public String toString() {
        return "OrderLine{" +
                "quantity=" + quantity +
                ", id=" + id +
                ", order=" + order +
                ", product=" + product +
                '}';
    }
}

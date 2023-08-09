package com.example.test.Entity;

import lombok.*;
import org.hibernate.annotations.Nationalized;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Date;
import java.util.HashSet;
import java.util.List;

@Entity
@Getter
@Setter
@Table(name = "orders")
public class Order implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
//    @Nationalized
    private Date orderDate;
    private  double amount;
    private  int status;

    @Enumerated(EnumType.STRING)
    private PaymentMethod paymentMethod;

    @Enumerated(EnumType.STRING)
    public OrderStatus orderStatus;

    private Date orderConfirmDate;


    public void setOrderStatus(OrderStatus orderStatus) {
        this.orderStatus = orderStatus;
    }

    public Order() {
    }

    public Order(int id, Date orderDate, double amount, int status, String paymentStatus, Customer customer, Users user, List<OrderLine> orderLineList) {
        this.id = id;
        this.orderDate = orderDate;
        this.amount = amount;
        this.status = status;
        this.paymentStatus = paymentStatus;
        this.customer = customer;
        this.user = user;
        this.orderLineList = orderLineList;
    }

    public Order(int id, Date orderDate, double amount, int status) {
        this.id = id;
        this.orderDate = orderDate;
        this.amount = amount;
        this.status = status;
    }

    public Order( @NonNull int status) {
            this.status = status;
        }

    @Column(name = "payment_status")
    private String paymentStatus;

    @ManyToOne
    @JoinColumn(name = "customer_id")
    private Customer customer;
    @ManyToOne
    @JoinColumn(name = "user_id")
    private Users user;

    @OneToMany(mappedBy = "order")
    List<OrderLine> orderLineList;
//    private Set<OrderLine> orderLine = new HashSet<>();

//    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
//    List<OrderTrack> orderTracks;

    @Override
    public String toString() {
        return "Order{" +
                "id=" + id +
                ", orderDate=" + orderDate +
                ", amount=" + amount +
                '}';
    }


}

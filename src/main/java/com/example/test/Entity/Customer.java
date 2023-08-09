package com.example.test.Entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Nationalized;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Customer implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Nationalized
    private  String name;
    @Nationalized
    private  String address;

    private  String phone;
    @Column(unique = true)
    private  String email;

    @OneToMany(mappedBy ="customer" )
    private List<Order> orderList;
}

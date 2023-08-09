//package com.example.test.Entity;
//
////import jakarta.persistence.Entity;
////import jakarta.persistence.GeneratedValue;
////import jakarta.persistence.GenerationType;
////import jakarta.persistence.Id;
//
//import lombok.AllArgsConstructor;
//import lombok.Getter;
//import lombok.NoArgsConstructor;
//import lombok.Setter;
//
//import javax.persistence.*;
//import java.io.Serializable;
//
//@Entity
//@Getter
//@Setter
//@NoArgsConstructor
//@AllArgsConstructor
//public class Maintenance implements Serializable {
//    private static final long serialVersionUID = 1L;
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private Integer id;
//    private Integer order_id;
//
//    private String startDate;
//
//    private String endDate;
//
//    private String maintenanceDate;
//
//    private int frequency;
//    private String description;
//
//    private String customerName;
//
//    private int status;
//
//    @ManyToOne
//    @JoinColumn(name = "product_id")
//    private Product product;
//
//}

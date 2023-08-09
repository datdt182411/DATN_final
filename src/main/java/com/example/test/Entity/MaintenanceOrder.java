package com.example.test.Entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "maintenance")
public class MaintenanceOrder implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String startDate;

    private String endDate;

    private String maintenanceDate;

    private String description;

    private int status;

    private String serialNumber;

    @ManyToOne
    @JoinColumn(name = "order_line_id")
    private OrderLine orderLine;


}

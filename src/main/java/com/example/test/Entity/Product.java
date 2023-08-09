package com.example.test.Entity;

import lombok.*;
import org.hibernate.annotations.Nationalized;

import javax.persistence.*;
import javax.validation.constraints.Min;
import java.io.Serializable;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Product implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Nationalized
    @Column(unique = true)
    @NonNull
    private String name;
    @NonNull
    private int status;
    @Min(0)
    @NonNull
    private double price;
    @Min(0)
    @NonNull
    private int quantity;
    @NonNull
    private String photo;

    int reviewCount;

    float averageRating;

    long vnp_TxnRef;

    public Product(Integer id) {
        this.id = id;
    }

    @ManyToOne
    @JoinColumn(name = "type_id")
    private Type type;
    @ManyToOne
    @JoinColumn(name = "vendor_id")
    private Vendor vendor;

    @OneToMany(mappedBy = "product" )
    private List<Repair> repairList;

//    @OneToMany(mappedBy = "product")
//    private List<MaintenanceOrder> maintenanceList;

    @Override
    public String toString() {
        return "Product{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", status=" + status +
                '}';
    }
    @OneToMany(mappedBy = "product")
    List<OrderLine> orderLineList;

    @OneToMany(mappedBy = "product")
    List<Review> reviewList;

    public void setType(Type type) {
        this.type = type;
    }

    public Product(@NonNull String name) {
        this.name = name;
    }

    public Product(@NonNull double price) {
        this.price = price;
    }

    public Product(@NonNull String name, @NonNull double price) {
        this.name = name;
        this.price = price;
    }

//    @Transient
//    public String getURI() {
//        return this.id + "/";
//    }

    @Transient
    private boolean userCanReview;

    @Transient
    private boolean reviewedByUser;

    public boolean isUserCanReview() {
        return userCanReview;
    }

    public void setUserCanReview(boolean userCanReview) {
        this.userCanReview = userCanReview;
    }

    public boolean isReviewedByUser() {
        return reviewedByUser;
    }

    public void setReviewedByUser(boolean reviewedByUser) {
        this.reviewedByUser = reviewedByUser;
    }

    @Transient
    public String getShortName() {
        if (name.length() > 70) {
            return name.substring(0, 70).concat("...");
        }
        return name;
    }
}

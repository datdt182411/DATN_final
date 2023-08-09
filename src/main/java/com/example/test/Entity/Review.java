package com.example.test.Entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "reviews")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Review {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column (length = 128, nullable = false)
    private String headline;

    @Column (length = 300, nullable = false)
    private String comment;

    private int rating;

    private int votes;

    @Column(nullable = false)
    private Date reviewTime;

    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private Users user;

    @OneToMany(mappedBy = "review")
    private List<ReviewVote> reviewVoteList;
    @Transient
    private boolean upvotedByCurrentCustomer;

    @Transient
    private boolean downvotedByCurrentCustomer;
}

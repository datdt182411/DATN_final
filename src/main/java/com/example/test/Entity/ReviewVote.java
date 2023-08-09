package com.example.test.Entity;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table(name = "reviews_votes")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReviewVote {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private static final int VOTE_UP_POINT = 1;
    private static final int VOTE_DOWN_POINT = -1;
    private int votes;
    @ManyToOne
    @JoinColumn(name = "user_id")
    private Users user;

    @ManyToOne
    @JoinColumn(name = "review_id")
    private Review review;

}

package com.example.test.Repository;

import com.example.test.Entity.ReviewVote;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ReviewVoteRepository extends JpaRepository<ReviewVote, Integer> {

    @Query("SELECT v FROM ReviewVote v WHERE v.review.id = ?1 AND v.user = ?2")
    public ReviewVote findByReviewAnAndUsers(Integer reviewId, Integer userId);

    @Query("SELECT v FROM ReviewVote v WHERE v.review.product.id = ?1 AND v.user.id = ?2")
    public List<ReviewVote> findByProductAndUsers(Integer productId, Integer userId);
}

package com.example.test.Repository;

import com.example.test.Entity.Product;
import com.example.test.Entity.Review;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface ReviewRepository extends JpaRepository<Review, Integer> {
//    List<Review> findAllByNameContaining(String keyword);

    public Page<Review> findByProduct(Product product, Pageable pageable);

    @Query("SELECT COUNT(r.id) FROM Review r WHERE r.user.id = ?1 AND "
            + "r.product.id = ?2")
    Long countByUsersAndProduct(int id, Integer productId);
}

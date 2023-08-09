package com.example.test.Repository;

import com.example.test.Entity.Product;
import com.example.test.Entity.Type;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product, Integer> {


    List<Product> findAllByNameContaining(String keyword);
    List<Product> findAllByName(String keyword);

    List<Product> findAllByType(Type type);
//    List<Product> findAll( Pageable pageable);

    public boolean existsByName(String name);

    @Query("update Product p SET p.averageRating = COALESCE (( SELECT AVG(r.rating) FROM Review r WHERE r.product.id = ?1), 0), "
            + " p.reviewCount = (SELECT COUNT (r.id) FROM Review r WHERE r.product.id =?1) "
            + "WHERE p.id = ?1")
    @Modifying
    public void updateReviewCountAndAverageRating(Integer productId);
}

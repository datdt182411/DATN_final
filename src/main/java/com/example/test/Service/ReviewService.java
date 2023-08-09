package com.example.test.Service;

import com.example.test.Entity.Product;
import com.example.test.Entity.Review;
import com.example.test.Entity.Customer;
import com.example.test.Entity.Users;
import com.example.test.Exception.ProductNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public interface ReviewService {
    public static final int REVIEWS_PER_PAGE = 5;

    public List<Review> listAll();

//    public boolean checkReview(String name);

    public Review getReview(Integer id) ;

    public Review saveReview(Review review);
    public void save(Review reviewInForm);

    public void deleteReview(Integer id);

    public List<Review> findAllByReviewName(String keyword);

    public Page<Review> list3MostRecentReviewsByProduct(Product product);

    public boolean didUserReviewProduct(Users user, Integer productId);
    public boolean canUserReviewProduct(Users user, Integer productId);

    public Page<Review> listByProduct(Product product, int pageNum, String sortField, String sortDir);

}

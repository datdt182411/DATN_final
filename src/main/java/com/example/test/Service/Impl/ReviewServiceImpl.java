package com.example.test.Service.Impl;

import com.example.test.Entity.*;
import com.example.test.Exception.ProductNotFoundException;
import com.example.test.Repository.OrderLineRepository;
import com.example.test.Repository.ProductRepository;
import com.example.test.Repository.ReviewRepository;
import com.example.test.Service.ReviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Optional;
@Service
@Transactional
public class ReviewServiceImpl implements ReviewService {
    public static final int REVIEWS_PER_PAGE = 5;
    @Autowired
    ReviewRepository reviewRepository;
    @Autowired
    ProductRepository productRepository;

    @Autowired
    OrderLineRepository orderLineRepository;

    @Override
    public List<Review> listAll() {
        return (List<Review>) reviewRepository.findAll();
    }

    @Override
    public Review getReview(Integer id) {
        Optional<Review> result = reviewRepository.findById(id);
        return result.get();
    }

    @Override
    public Review saveReview(Review review) {
        review.setReviewTime(new Date());
        Review savedReview = reviewRepository.save(review);

        Integer productId = savedReview.getProduct().getId();
        productRepository.updateReviewCountAndAverageRating(productId);
        return savedReview;
    }

    @Override
    public void save(Review reviewInForm) {
        Review reviewInDB = reviewRepository.findById(reviewInForm.getId()).get();
        reviewInDB.setHeadline(reviewInForm.getHeadline());
        reviewInDB.setComment(reviewInForm.getComment());

        reviewRepository.save(reviewInDB);
        productRepository.updateReviewCountAndAverageRating(reviewInDB.getProduct().getId());
    }
    @Override
    public void deleteReview(Integer id) {
        reviewRepository.delete(reviewRepository.getOne(id));
    }

    @Override
    public List<Review> findAllByReviewName(String keyword) {
        return null;
    }


    @Override
    public Page<Review> list3MostRecentReviewsByProduct(Product product) {
        Sort sort = Sort.by("reviewTime").descending();
        Pageable pageable = PageRequest.of(0, 3, sort);

        return reviewRepository.findByProduct(product, pageable);
    }

    @Override
    public boolean didUserReviewProduct(Users user, Integer productId) {
        Long count = reviewRepository.countByUsersAndProduct(user.getId(), productId);
        return count > 0;
    }

    @Override
    public boolean canUserReviewProduct(Users user, Integer productId) {
        Long count = orderLineRepository.countByProductAndUserAndOrderStatus(productId, user.getId(), OrderStatus.DELIVERED);
        return count > 0;
    }

    public Page<Review> listByProduct(Product product, int pageNum, String sortField, String sortDir) {
        Sort sort = Sort.by(sortField);
        sort = sortDir.equals("asc") ? sort.ascending() : sort.descending();
        Pageable pageable = PageRequest.of(pageNum - 1, REVIEWS_PER_PAGE, sort);

        return reviewRepository.findByProduct(product, pageable);
    }
}

package com.example.test.Controller;

import com.example.test.Entity.Customer;
import com.example.test.Entity.Product;
import com.example.test.Entity.Review;
import com.example.test.Exception.ProductNotFoundException;
import com.example.test.Security.CustomerOAuth2User;
import com.example.test.Service.ProductService;
import com.example.test.Service.ReviewService;
import org.springframework.data.domain.Page;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
public class ReviewController {
    private final ProductService productService;
    private final ReviewService reviewService;

    public ReviewController(ProductService productService, ReviewService reviewService) {
        this.productService = productService;
        this.reviewService = reviewService;
    }
    @GetMapping("/ratings/{productId}/page/{pageNum}")
    public String listByProductByPage(Model model,
                                      @PathVariable(name = "productId") Integer productId,
                                      @PathVariable(name = "pageNum") int pageNum,
                                      String sortField, String sortDir,
                                      HttpServletRequest request) {
        Product product = null;

        try {
            product = productService.getProduct(productId);
        } catch ( ProductNotFoundException ex) {
            return "error/404";
        }

        Page<Review> page = reviewService.listByProduct(product, pageNum, sortField, sortDir);
        List<Review> listReviews = page.getContent();

        String userName = authenticationName();
        try {
            model.addAttribute("userAuthen", userName);
        } catch (Exception ex) {
            model.addAttribute("userAuthen", null);
        }
//        if (userName != null) {
//            voteService.markReviewsVotedForProductByCustomer(listReviews, product.getId(), customer.getId());
//        }

        model.addAttribute("totalPages", page.getTotalPages());
        model.addAttribute("totalItems", page.getTotalElements());
        model.addAttribute("currentPage", pageNum);
        model.addAttribute("sortField", sortField);
        model.addAttribute("sortDir", sortDir);
        model.addAttribute("reverseSortDir", sortDir.equals("asc") ? "desc" : "asc");

        model.addAttribute("listReviews", listReviews);
        model.addAttribute("product", product);

        long startCount = (pageNum - 1) * ReviewService.REVIEWS_PER_PAGE + 1;
        model.addAttribute("startCount", startCount);

        long endCount = startCount + ReviewService.REVIEWS_PER_PAGE - 1;
        if (endCount > page.getTotalElements()) {
            endCount = page.getTotalElements();
        }

        model.addAttribute("endCount", endCount);
        model.addAttribute("pageTitle", "Reviews for " + product.getShortName());
        return "Item_Review/reviews_product";
    }

    @GetMapping("/ratings/{productId}")
    public String listByProductFirstPage(@PathVariable(name = "productId") Integer productId, Model model,
                                         HttpServletRequest request) {
        return listByProductByPage(model, productId, 1, "reviewTime", "desc", request);
    }

    public String authenticationName() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Object auth = authentication.getPrincipal();
        String userLoginName = null;
        if (auth instanceof UserDetails) {
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            userLoginName = userDetails.getUsername();
        } else if (auth instanceof CustomerOAuth2User) {
            CustomerOAuth2User userDetails = (CustomerOAuth2User) authentication.getPrincipal();
            userLoginName = userDetails.getName();
        }
        return userLoginName;
    }
}

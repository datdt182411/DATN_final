package com.example.test.Service.Impl;

import com.example.test.Entity.Product;
import com.example.test.Exception.ProductNotFoundException;
import com.example.test.Repository.ProductRepository;
import com.example.test.Service.NotificationService;
import com.example.test.Service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.transaction.Transactional;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class ProductServiceImpl implements ProductService {

    @Autowired
    ProductRepository productRepository;

    @Autowired
    NotificationService notificationService;

    @Override
    public List<Product> listAll() {
        return (List<Product>) productRepository.findAll();
    }

    @Override
    public boolean checkProduct(String name) {
        return productRepository.existsByName(name);
    }

    @Override
    public Product getProduct(Integer id) throws ProductNotFoundException{
        Optional<Product> result = productRepository.findById(id);
        if(result.isPresent()){
            return result.get();
        }
        throw new ProductNotFoundException("Could not find any product with ID " + id);
    }

    @Override
    public Product saveProduct(Product product) {
        Product updatedProduct = productRepository.save(product);
        productRepository.updateReviewCountAndAverageRating(updatedProduct.getId());
        return updatedProduct;
    }

    @Override
    public void deleteProduct(Integer id) {
        productRepository.delete(productRepository.getOne(id));
    }

    @Override
    public List<Product> findAllByProductName(String keyword) {
        return productRepository.findAllByNameContaining(keyword);
    }

    //The method perform send email when have quantity product = 0.
    @Scheduled(cron = "0 0 */2 ? * *")
    public void checkProductStock() throws MessagingException, UnsupportedEncodingException {
        List<Product> products = productRepository.findAll();
        List<Product> productList = new ArrayList<>();
        for (Product product : products) {
            if (product.getQuantity() == 0 && product.getStatus() == 1) {
                productList.add(product);
            }
        }
        if(!productList.isEmpty()) {
            notificationService.sendOutOfStockNotification(productList);
        }
    }
}

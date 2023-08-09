package com.example.test.Service.Impl;

import com.example.test.Repository.CartRepository;
import com.example.test.Service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CartServiceImpl implements CartService {
    @Autowired
    CartRepository cartRepository;
//    @Override
//    public List<Cart> findByCartId(Integer id) {
//        Optional<Cart> optionalCart = cartRepository.findById(id);
//        return ;
    }


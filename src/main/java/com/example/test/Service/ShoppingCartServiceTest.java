package com.example.test.Service;

import com.example.test.Entity.Cart;
import com.example.test.Entity.Customer;
import org.springframework.stereotype.Service;

import java.util.Collection;

@Service
public interface ShoppingCartServiceTest {

    public Integer addProduct(Integer productId, Integer quantity, Customer customer);


    int getCount();

    double getAmount();

    void clear();

    Collection<Cart> getCart();

    void remove(Cart item);

    void add(Cart item);

    void remove(Integer id);

}

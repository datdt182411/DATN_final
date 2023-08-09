package com.example.test.Service.Impl;


import com.example.test.Entity.Cart;
import com.example.test.Entity.Customer;
import com.example.test.Entity.Product;
import com.example.test.Repository.CartItemsRepository;
import com.example.test.Service.ShoppingCartServiceTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Service
public class ShoppingCartServiceTestImpl implements ShoppingCartServiceTest {

    private Map<Integer, Cart> map = new HashMap<Integer, Cart>();

   @Autowired
   private CartItemsRepository cartItemsRepository;
    @Override
    public Integer addProduct(Integer productId, Integer quantity, Customer customer) {
        Integer updatedQuantity = quantity;
        Product product = new Product(productId);

       Cart cart = cartItemsRepository.findByCustomerAndProduct(customer, product);

       if (cart != null) {
           updatedQuantity = cart.getQuantity() + quantity;
       } else {
//           cart = new Cart(product.getId(), product.getName(), product.getPhoto(), 1, product.getPrice());
           cart = new Cart();
           cart.setCustomer(customer);
           cart.setProduct(product);
       }

       cart.setQuantity(updatedQuantity);

       cartItemsRepository.save(cart);

       return updatedQuantity;
    }

//    @Override
//    public Integer addProduct(Integer productId, Integer quantity, Users users) {
//        return null;
//    }


    @Override
    public int getCount() {

        if (map.isEmpty()) {
            return 0;
        }

        return map.values().size();
    }

    @Override
    public double getAmount() {

        return map.values().stream().mapToDouble(item -> item.getQuantity() * item.getProduct().getPrice()).sum();
    }

    @Override
    public void clear() {
        map.clear();
    }

    @Override
    public Collection<Cart> getCart() {

        return map.values();
    }

    @Override
    public void remove(Cart item) {
        map.remove(item.getId());

    }

    @Override
    public void add(Cart item) {
        Cart existedItem = map.get(item.getId());

        if (existedItem != null) {
            existedItem.setQuantity(item.getQuantity() + existedItem.getQuantity());
        } else {
            map.put(item.getId(), item);
        }

    }

    @Override
    public void remove(Integer id) {
        map.remove(id);
    }
}

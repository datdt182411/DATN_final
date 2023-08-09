package com.example.test.Repository;

import com.example.test.Entity.Cart;
import com.example.test.Entity.Customer;
import com.example.test.Entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CartItemsRepository extends JpaRepository<Cart, Integer> {
    public List<Cart> findByCustomer(Customer customer);

    public Cart findByCustomerAndProduct(Customer customer, Product product);

    @Modifying
    @Query ("UPDATE Cart c SET c.quantity = ?1 WHERE c.customer.id = ?2 AND c.product.id = ?3 ")
    public void updateQuantity (Integer quantity, Integer customerId, Integer productId);

    @Modifying
    @Query("DELETE FROM Cart c WHERE c.customer.id = ?1 AND c.product.id = ?2 ")
    public void deleteByCustomerAndProduct(Integer customerId, Integer productId);

}

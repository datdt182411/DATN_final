package com.example.test.Repository;

import com.example.test.Entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CustomerRepository extends JpaRepository<Customer,Integer> {
    List<Customer> findAllByNameContaining(String keyword);

    Customer findByEmail(String email);



//    @Query("UPDATE Customer  c SET c.authenticationType = ?2 WHERE c.id = ?1 ")
//    @Modifying
//    public void updateAuthenticationType(Integer customerId , AuthenticationType type);
//
//    @Query("SELECT c FROM Customer c WHERE c.email = ?1")
//    public Customer findByEmail(String email);
}

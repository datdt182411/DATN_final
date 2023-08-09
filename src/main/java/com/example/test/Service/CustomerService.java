package com.example.test.Service;

import com.example.test.Entity.Customer;
import com.example.test.Exception.CustomerNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface CustomerService {
    public List<Customer> findAllByProductName(String keyword);

    public Customer getCustomer(Integer id) throws CustomerNotFoundException;

//    public void updateAuthenticationType(Customer customer, AuthenticationType type);
//
    public Customer getCustomerByEmail(String email);
//
//    public void addNewCustomerUponOAuthLogin(String name, String email);
//
//    public void save(Customer customerInForm) ;

}

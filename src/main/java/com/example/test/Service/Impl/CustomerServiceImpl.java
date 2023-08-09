package com.example.test.Service.Impl;

import com.example.test.Entity.Customer;
import com.example.test.Exception.CustomerNotFoundException;
import com.example.test.Repository.CustomerRepository;
import com.example.test.Service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CustomerServiceImpl implements CustomerService {
    @Autowired
    CustomerRepository customerRepository;
    @Override
    public List<Customer> findAllByProductName(String keyword) {
        return customerRepository.findAllByNameContaining(keyword);
    }

    @Override
    public Customer getCustomer(Integer id) throws CustomerNotFoundException {
        Optional<Customer> result = customerRepository.findById(id);
        if(result.isPresent()){
            return result.get();
        }
        throw new CustomerNotFoundException("Could not find any users with ID " + id);
    }

//    @Override
//    public void updateAuthenticationType(Customer customer, AuthenticationType type) {
//        if (!customer.getAuthenticationType().equals(type)){
//            customerRepository.updateAuthenticationType(customer.getId(), type);
//        }
//    }

    @Override
    public Customer getCustomerByEmail(String email) {
        return customerRepository.findByEmail(email);
    }
//
//    @Override
//    public void addNewCustomerUponOAuthLogin(String name, String email) {
//            Customer customer = new Customer();
//            customer.setEmail(email);
//            customer.setName(name);
//            customer.setAuthenticationType(AuthenticationType.GOOGLE);
//            customer.setPhone("");
//            customer.setAddress("");
//
//            customerRepository.save(customer);
//        }
//
//    @Override
//    public void save(Customer customerInForm) {
//
//    }

}


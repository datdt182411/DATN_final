package com.example.test.Service.Impl;

import com.example.test.Entity.Customer;
import com.example.test.Entity.Order;
import com.example.test.Entity.Users;
import com.example.test.Exception.OrderNotFoundException;
import com.example.test.Repository.OrderRepository;
import com.example.test.Service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    OrderRepository orderRepository;
    @Override
    public List<Order> listAll() {
        return  (List<Order>) orderRepository.findAll();
    }


    @Override
    public Order getOrder(Integer id) throws OrderNotFoundException {
        Optional<Order> result = orderRepository.findById(id);
        if(result.isPresent()){
            return result.get();
        }
        throw new OrderNotFoundException("Could not find any order with ID " + id);
    }

    @Override
    public Order saveOrder(Order order) {
        return orderRepository.save(order);
    }

    @Override
    public void deleteOrder(Integer id) {
    orderRepository.delete(orderRepository.getOne(id));
    }

    @Override
    public Order getOrder(Integer id, Users user) {
        return orderRepository.findByIdAndUser(id, user);
    }

}

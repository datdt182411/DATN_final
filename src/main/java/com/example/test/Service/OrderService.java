package com.example.test.Service;

import com.example.test.Entity.Customer;
import com.example.test.Entity.Order;
import com.example.test.Entity.Users;
import com.example.test.Exception.OrderNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface OrderService {
    public List<Order> listAll();


    public Order getOrder(Integer id) throws OrderNotFoundException;

    public Order saveOrder(Order order);

    public void deleteOrder(Integer id);

    public Order getOrder(Integer id, Users user);
}

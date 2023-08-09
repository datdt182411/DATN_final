package com.example.test.Service.Impl;

import com.example.test.Entity.OrderLine;
import com.example.test.Entity.Product;
import com.example.test.Exception.OrderLineNotFoundException;
import com.example.test.Exception.ProductNotFoundException;
import com.example.test.Repository.OrderLineRepository;
import com.example.test.Service.OrderLineService;
import org.aspectj.weaver.ast.Or;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class OrderLineServiceImpl implements OrderLineService {
    @Autowired
    OrderLineRepository orderLineRepository;

    @Override
    public OrderLine getOrderLine(Integer id) throws OrderLineNotFoundException {
        Optional<OrderLine> result = orderLineRepository.findById(id);
        if(result.isPresent()){
            return result.get();
        }
        throw new OrderLineNotFoundException("Could not find any product with ID " + id);
    }

    @Override
    public List<OrderLine> listAll() {
        return orderLineRepository.findAll();
    }
}

package com.example.test.Service;

import com.example.test.Entity.OrderLine;
import com.example.test.Exception.OrderLineNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface OrderLineService {

    public OrderLine getOrderLine(Integer id) throws OrderLineNotFoundException;

    List<OrderLine> listAll();
}

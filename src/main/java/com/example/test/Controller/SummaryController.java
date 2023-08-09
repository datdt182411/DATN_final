package com.example.test.Controller;

import com.example.test.Converter.ConvertObject;
import com.example.test.Repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.text.DecimalFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Controller
public class SummaryController {
    private final OrderRepository orderRepository;
    @Autowired
    ConvertObject covConvertObject;

    public SummaryController(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    @GetMapping("/summary")
   public String getSummary(Model model){
        DecimalFormat formatter = new DecimalFormat("###,###,###");
//        model.addAttribute("order", orderRepository.orders_by_month());
        List<Object> list = orderRepository.orders_by_month(LocalDate.now().getYear());
        List<String> months = new ArrayList<>();
        List<String> orders = new ArrayList<>();
        List<String> amounts = new ArrayList<>();
        for (Object o : list) {
            String[] data = covConvertObject.convert(o);
            String order = (data[0]);
            Double amount = Double.valueOf((data[1]));
            String month = (data[2]);

            orders.add(order);
            amounts.add(formatter.format(amount));
            months.add(month);

        }
        model.addAttribute("orders", orders);
        model.addAttribute("amounts", amounts);
        model.addAttribute("months", months);

        return "Summary/View";
    }
}

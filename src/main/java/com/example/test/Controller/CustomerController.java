package com.example.test.Controller;

import com.example.test.Converter.ConvertObject;
import com.example.test.Entity.Customer;
import com.example.test.Entity.Order;
import com.example.test.Exception.CustomerNotFoundException;
import com.example.test.Repository.CustomerRepository;
import com.example.test.Repository.OrderRepository;
import com.example.test.Service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.List;

@Controller
public class CustomerController {
    @Autowired
    ConvertObject convert;

    private final CustomerRepository customerRepository;
    private final CustomerService customerService;
    private final OrderRepository orderRepository;
//    private final OrderService orderService;

    public CustomerController(CustomerRepository customerRepository, CustomerService customerService,OrderRepository orderRepository) {
        this.customerRepository = customerRepository;

        this.orderRepository = orderRepository;
        this.customerService = customerService;
    }

    @GetMapping("/CustomerView")
    public String CustomerView(Model model) {
        Pageable pageable= PageRequest.of(0,5);
        Page<Customer> customerList = customerRepository.findAll(pageable);
        int totalPages = customerList.getTotalPages();
        long totalItems = customerList.getTotalElements();
        model.addAttribute("currentPage", 1);
        model.addAttribute("totalPages", totalPages);
        model.addAttribute("totalItems", totalItems);
        model.addAttribute("customerList", customerList);
        return "Customer/View";
    }

    @GetMapping("/CustomerView/{pageNumber}")
    public String CustomerView(Model model, @PathVariable(value = "pageNumber") int currentPage) {
        Pageable pageable= PageRequest.of(currentPage-1,5);
        Page<Customer> customerList = customerRepository.findAll(pageable);
        int totalPages = customerList.getTotalPages();
        long totalItems = customerList.getTotalElements();
        model.addAttribute("currentPage", currentPage);
        model.addAttribute("totalPages", totalPages);
        model.addAttribute("totalItems", totalItems);
        model.addAttribute("customerList", customerList);
        return "Customer/View";
    }

    @GetMapping("/CustomerOrder")
    public String orderDetail(Model model, @RequestParam Integer id) throws CustomerNotFoundException {

        List<String> customerName = new ArrayList<>();
        List<String> quantities = new ArrayList<>();
        List<String> prices = new ArrayList<>();
        List<String> photos = new ArrayList<>();

        Customer customer = customerService.getCustomer(id);
        List<Order> orders= customer.getOrderList();
        for (Order order:orders
             ) {
            List<Object> list = orderRepository.managerOrder(order.getId());
//            List<Object> list = orderService.managerOrder(order.getId());
//        List<HistoryForUser> historyForUsers = new ArrayList<>();
//        HistoryForUser h;
            for (Object o : list) {
                String[] data = convert.convert(o);
                String cusName = (data[0]);
                String quantity = (data[2]);
                String price = (data[1]);
                String photo = (data[3].substring(1, data[3].length() - 1));

                customerName.add(cusName);
                quantities.add(quantity);
                prices.add(price);
                photos.add(photo);
            }
        }
        model.addAttribute("customerName", customerName);
        model.addAttribute("quantities", quantities);
        model.addAttribute("prices", prices);
        model.addAttribute("photos", photos);
        return "Order/orderDetail";
    }
    @GetMapping("/searchCustomer")
    public String searchCustomer(Model model, @RequestParam(name = "keyword") String keyword) {
        List<Customer> customerList =  customerService.findAllByProductName(keyword);
        model.addAttribute("customerList", customerList);
        return "Customer/View";
    }



//
}

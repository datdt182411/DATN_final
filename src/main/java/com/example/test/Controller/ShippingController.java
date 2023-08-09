package com.example.test.Controller;

import com.example.test.Config.MyUserDetails;
import com.example.test.Entity.Order;
import com.example.test.Entity.OrderStatus;
import com.example.test.Repository.OrderRepository;
import com.example.test.Service.OrderService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.List;

@Controller
public class ShippingController {

    private final OrderRepository orderRepository;
    private final OrderService orderService;

    public ShippingController(OrderRepository orderRepository, OrderService orderService) {
        this.orderRepository = orderRepository;
        this.orderService = orderService;
    }


    @GetMapping("/Shipping")
    public String ShippingView(Model model , @AuthenticationPrincipal MyUserDetails loggedUser) {
        Pageable pageable = PageRequest.of(0, 5, Sort.by("orderDate").descending());

        Page<Order> orderPage = orderRepository.findAllByStatus(1, pageable);
        int totalPages = orderPage.getTotalPages();
        long totalItems = orderPage.getTotalElements();
        model.addAttribute("orderList", orderPage);
        model.addAttribute("currentPage", 1);
        model.addAttribute("totalPages", totalPages);
        model.addAttribute("totalItems", totalItems);
//        if(loggedUser.hasRole("SHIPPER") && !loggedUser.hasRole("ADMIN") && !loggedUser.hasRole("SALE")){
//            return "Order/OrderShipper";
//        }
        return "Shipping/View";
    }
    @GetMapping("/Shipping/{pageNumber}")
    public String ShippingView(Model model, @PathVariable(value = "pageNumber") int currentPage) {

        Pageable pageable = PageRequest.of(currentPage-1, 5,Sort.by("orderDate").descending());

        Page<Order> orderPage = orderRepository.findAllByStatus(1, pageable);
        int totalPages = orderPage.getTotalPages();
        long totalItems = orderPage.getTotalElements();
        model.addAttribute("orderList", orderPage);
        model.addAttribute("currentPage", currentPage);
        model.addAttribute("totalPages", totalPages);
        model.addAttribute("totalItems", totalItems);

        return "Shipping/View";
    }

    @GetMapping("/ListOrderNew")
    public String listOrderNew(Model model) {
        Pageable pageable = PageRequest.of(0, 5, Sort.by("orderDate").descending());

        displayIndexPage(model, OrderStatus.NEW, pageable);

        return "Shipping/OrderNewView";
    }

    @GetMapping("/ListOrderNew/{pageNumber}")
    public String listOrderNew(Model model, @PathVariable(value = "pageNumber") int currentPage) {

        Pageable pageable = PageRequest.of(currentPage-1, 5,Sort.by("orderDate").descending());

        displayIndexPage(model, OrderStatus.NEW, pageable);
        return "Shipping/OrderNewView";
    }

    @GetMapping("/ListOrderShipping")
    public String listOrderShipping(Model model) {
        Pageable pageable = PageRequest.of(0, 5, Sort.by("orderDate").descending());

        displayIndexPage(model, OrderStatus.SHIPPING, pageable);

        return "Shipping/OrderShippingView";
    }

    @GetMapping("/ListOrderShipping/{pageNumber}")
    public String listOrderShipping(Model model, @PathVariable(value = "pageNumber") int currentPage) {

        Pageable pageable = PageRequest.of(currentPage-1, 5,Sort.by("orderDate").descending());

        displayIndexPage(model, OrderStatus.SHIPPING, pageable);
        return "Shipping/OrderShippingView";
    }

    @GetMapping("/OrderShipping")
    public String orderShipping(Model model, @RequestParam(name = "id") Integer id) {
        Pageable pageable = PageRequest.of(0, 5, Sort.by("orderDate").descending());

        displayIdIndexPage(model, id, OrderStatus.SHIPPING, pageable);

        return "Shipping/View";
    }

    @GetMapping("/ListOrderDelivered")
    public String listOrderDelivered(Model model) {

        Pageable pageable = PageRequest.of(0, 5, Sort.by("orderDate").descending());

        displayIndexPage(model, OrderStatus.DELIVERED, pageable);

        return "Shipping/OrderDeliveredView";
    }

    @GetMapping("/ListOrderDelivered/{pageNumber}")
    public String listOrderDelivered(Model model, @PathVariable(value = "pageNumber") int currentPage) {

        Pageable pageable = PageRequest.of(currentPage-1, 5,Sort.by("orderDate").descending());

        displayIndexPage(model, OrderStatus.DELIVERED, pageable);

        return "Shipping/OrderDeliveredView";
    }

    @GetMapping("/OrderDelivered")
    public String orderDelivered(Model model, @RequestParam(name = "id") Integer id) {
        Pageable pageable = PageRequest.of(0, 5, Sort.by("orderDate").descending());

        displayIdIndexPage(model, id, OrderStatus.DELIVERED, pageable);
        return "Shipping/OrderDeliveredView";
    }

    public void displayIdIndexPage(Model model,Integer id, OrderStatus orderStatus, Pageable pageable) {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            UserDetails userDetail = (UserDetails) authentication.getPrincipal();
            System.out.println(userDetail.getAuthorities());
            model.addAttribute("userAuthen", userDetail.getUsername());
        } catch (Exception ex) {
            model.addAttribute("userAuthen", null);
        }

        Page<Order> orderPage = orderRepository.findAllByOrderStatus(orderStatus, pageable);
        int totalPages = orderPage.getTotalPages();
        long totalItems = orderPage.getTotalElements();
        model.addAttribute("orderList", orderPage);
        model.addAttribute("currentPage", 1);
        model.addAttribute("totalPages", totalPages);
        model.addAttribute("totalItems", totalItems);

        List<Order> orderList = orderService.listAll();
        List<Order> orders = new ArrayList<>();

        Order order = orderRepository.getOrderById(id);
        order.setOrderStatus(orderStatus);
        orderRepository.save(order);
        for (Order o : orderList
        ) {
            if (o.getOrderStatus() == orderStatus) {
                orders.add(o);
            }
        }
        model.addAttribute("orderList", orders);
    }

    public void displayIndexPage(Model model, OrderStatus orderStatus, Pageable pageable) {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            UserDetails userDetail = (UserDetails) authentication.getPrincipal();
            System.out.println(userDetail.getAuthorities());
            model.addAttribute("userAuthen", userDetail.getUsername());
        } catch (Exception ex) {
            model.addAttribute("userAuthen", null);
        }

        Page<Order> orderPage = orderRepository.findAllByOrderStatus(orderStatus, pageable);
        int totalPages = orderPage.getTotalPages();
        long totalItems = orderPage.getTotalElements();
        model.addAttribute("orderList", orderPage);
        model.addAttribute("currentPage", 1);
        model.addAttribute("totalPages", totalPages);
        model.addAttribute("totalItems", totalItems);

        List<Order> orderList = orderService.listAll();
        List<Order> orders = new ArrayList<>();
        for (Order o : orderList
        ) {
            if (o.getOrderStatus() == orderStatus) {
                orders.add(o);
            }
        }
        model.addAttribute("orderList", orders);
    }

}

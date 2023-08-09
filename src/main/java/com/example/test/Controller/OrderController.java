package com.example.test.Controller;

import com.example.test.Config.MyUserDetails;
import com.example.test.Converter.ConvertObject;
import com.example.test.Entity.*;
import com.example.test.Exception.MaintenanceNotFoundException;
import com.example.test.Exception.OrderNotFoundException;
import com.example.test.Repository.OrderLineRepository;
import com.example.test.Repository.OrderRepository;
import com.example.test.Repository.ProductRepository;
import com.example.test.Service.OrderService;
import com.example.test.Service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
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
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Controller
public class OrderController {
    private final OrderRepository orderRepository;
    @Autowired
    private ConvertObject convert;
    private final ProductRepository productRepository;
    private final ProductService productService;
    private final OrderLineRepository orderLineRepository;

    private final OrderService orderService;


    public OrderController(OrderRepository orderRepository,
                           ProductRepository productRepository,
                           ProductService productService, OrderLineRepository orderLineRepository, OrderService orderService) {
        this.orderRepository = orderRepository;
        this.productRepository = productRepository;
        this.productService = productService;
        this.orderLineRepository = orderLineRepository;
        this.orderService = orderService;
    }

    @GetMapping("/OrderView")
    public String OrderView(Model model , @AuthenticationPrincipal MyUserDetails loggedUser) {
        Pageable pageable = PageRequest.of(0, 5, Sort.by("orderDate").descending());
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            UserDetails userDetail = (UserDetails) authentication.getPrincipal();
            System.out.println(userDetail.getAuthorities());
            model.addAttribute("userAuthen", userDetail.getUsername());
        } catch (Exception ex) {
            model.addAttribute("userAuthen", null);
        }

        Page<Order> orderPage = orderRepository.findAll(pageable);
        int totalPages = orderPage.getTotalPages();
        long totalItems = orderPage.getTotalElements();
        model.addAttribute("orderList", orderPage);
        model.addAttribute("currentPage", 1);
        model.addAttribute("totalPages", totalPages);
        model.addAttribute("totalItems", totalItems);
//        if(loggedUser.hasRole("SHIPPER") && !loggedUser.hasRole("ADMIN") && !loggedUser.hasRole("SALE")){
//            return "Order/OrderShipper";
//        }
            return "Order/View";
    }
    @GetMapping("/OrderView/{pageNumber}")
    public String OrderView(Model model, @PathVariable(value = "pageNumber") int currentPage) {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            UserDetails userDetail = (UserDetails) authentication.getPrincipal();
            model.addAttribute("userAuthen", userDetail.getUsername());
        } catch (Exception ex) {
            model.addAttribute("userAuthen", null);
        }

        Pageable pageable = PageRequest.of(currentPage-1, 5,Sort.by("orderDate").descending());
        Page<Order> orderPage = orderRepository.findAll(pageable);
        int totalPages = orderPage.getTotalPages();
        long totalItems = orderPage.getTotalElements();
            model.addAttribute("orderList", orderPage);
            model.addAttribute("currentPage", currentPage);
            model.addAttribute("totalPages", totalPages);
            model.addAttribute("totalItems", totalItems);
            return "Order/View";

    }

    @GetMapping("/listNotConfirm")
    public String notConfirm(Model model) {
        List<Order> orderList = orderService.listAll();
        List<Order> orders = new ArrayList<>();

        Pageable pageable = PageRequest.of(0, 5, Sort.by("orderDate").descending());
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            UserDetails userDetail = (UserDetails) authentication.getPrincipal();
            model.addAttribute("userAuthen", userDetail.getUsername());
        } catch (Exception ex) {
            model.addAttribute("userAuthen", null);
        }

        Page<Order> orderPage =  orderRepository.findAllByStatus(0, pageable);
        int totalPages = orderPage.getTotalPages();
        long totalItems = orderPage.getTotalElements();
        model.addAttribute("orderList", orderPage);
        model.addAttribute("currentPage", 1);
        model.addAttribute("totalPages", totalPages);
        model.addAttribute("totalItems", totalItems);

        for (Order o : orderList
        ) {
            if (o.getStatus() == 0) {
                orders.add(o);
            }
        }
        model.addAttribute("orderList", orders);
        return "Order/viewNotConfirmOrder";
    }

    @GetMapping("/listNotConfirm/{pageNumber}")
    public String notConfirm(Model model, @PathVariable(value = "pageNumber") int currentPage) {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            UserDetails userDetail = (UserDetails) authentication.getPrincipal();
            model.addAttribute("userAuthen", userDetail.getUsername());
        } catch (Exception ex) {
            model.addAttribute("userAuthen", null);
        }

        Pageable pageable = PageRequest.of(currentPage-1, 5,Sort.by("orderDate").descending());
        Page<Order> orderPage =  orderRepository.findAllByStatus(0, pageable);
        int totalPages = orderPage.getTotalPages();
        long totalItems = orderPage.getTotalElements();
        model.addAttribute("orderList", orderPage);
        model.addAttribute("currentPage", currentPage);
        model.addAttribute("totalPages", totalPages);
        model.addAttribute("totalItems", totalItems);
        return "Order/viewNotConfirmOrder";
    }


    @GetMapping("/listConfirmed")
    public String confirmed(Model model) {
        List<Order> orderList = orderService.listAll();
        List<Order> orders = new ArrayList<>();

        Pageable pageable = PageRequest.of(0, 5, Sort.by("orderDate").descending());
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            UserDetails userDetail = (UserDetails) authentication.getPrincipal();
            model.addAttribute("userAuthen", userDetail.getUsername());
        } catch (Exception ex) {
            model.addAttribute("userAuthen", null);
        }

        Page<Order> orderPage =  orderRepository.findAllByStatus(1, pageable);
        int totalPages = orderPage.getTotalPages();
        long totalItems = orderPage.getTotalElements();
        model.addAttribute("orderList", orderPage);
        model.addAttribute("currentPage", 1);
        model.addAttribute("totalPages", totalPages);
        model.addAttribute("totalItems", totalItems);
        for (Order o : orderList
        ) {
            if (o.getStatus() == 1) {
                orders.add(o);
            }
        }
        model.addAttribute("orderList", orders);
        return "Order/viewConfirmOrder";
    }


    @GetMapping("/listConfirmed/{pageNumber}")
    public String confirmed(Model model, @PathVariable(value = "pageNumber") int currentPage) {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            UserDetails userDetail = (UserDetails) authentication.getPrincipal();
            model.addAttribute("userAuthen", userDetail.getUsername());
        } catch (Exception ex) {
            model.addAttribute("userAuthen", null);
        }

        Pageable pageable = PageRequest.of(currentPage-1, 5,Sort.by("orderDate").descending());
        Page<Order> orderPage =  orderRepository.findAllByStatus(1, pageable);
        int totalPages = orderPage.getTotalPages();
        long totalItems = orderPage.getTotalElements();
        model.addAttribute("orderList", orderPage);
        model.addAttribute("currentPage", currentPage);
        model.addAttribute("totalPages", totalPages);
        model.addAttribute("totalItems", totalItems);
        return "Order/viewConfirmOrder";
    }

    @GetMapping("/listProcessing")
    public String viewListProcessing(Model model, OrderStatus orderStatus) {
        List<Order> orderList = orderService.listAll();
        List<Order> orders = new ArrayList<>();

        Pageable pageable = PageRequest.of(0, 5, Sort.by("orderDate").descending());
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            UserDetails userDetail = (UserDetails) authentication.getPrincipal();
            model.addAttribute("userAuthen", userDetail.getUsername());
        } catch (Exception ex) {
            model.addAttribute("userAuthen", null);
        }

        Page<Order> orderPage =   orderRepository.findAllByOrderStatus(OrderStatus.PROCESSING, pageable);
        int totalPages = orderPage.getTotalPages();
        long totalItems = orderPage.getTotalElements();
        model.addAttribute("orderList", orderPage);
        model.addAttribute("currentPage", 1);
        model.addAttribute("totalPages", totalPages);
        model.addAttribute("totalItems", totalItems);
        for (Order o : orderList
        ) {
            if (o.getOrderStatus() == OrderStatus.PROCESSING) {
                orders.add(o);
            }
        }
        model.addAttribute("orderList", orders);
        return "Order/viewProcessingOrder";
    }

    @GetMapping("/listProcessing/{pageNumber}")
    public String viewListProcessing(Model model,@PathVariable(value = "pageNumber") int currentPage, OrderStatus orderStatus) {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            UserDetails userDetail = (UserDetails) authentication.getPrincipal();
            model.addAttribute("userAuthen", userDetail.getUsername());
        } catch (Exception ex) {
            model.addAttribute("userAuthen", null);
        }

        Pageable pageable = PageRequest.of(currentPage-1, 5,Sort.by("orderDate").descending());
        Page<Order> orderPage =  orderRepository.findAllByOrderStatus(OrderStatus.PROCESSING, pageable);
        int totalPages = orderPage.getTotalPages();
        long totalItems = orderPage.getTotalElements();
        model.addAttribute("orderList", orderPage);
        model.addAttribute("currentPage", currentPage);
        model.addAttribute("totalPages", totalPages);
        model.addAttribute("totalItems", totalItems);
        return "Order/viewProcessingOrder";
    }

    @GetMapping("/listCanceled")
    public String viewListCanceled(Model model, OrderStatus orderStatus) {
        List<Order> orderList = orderService.listAll();
        List<Order> orders = new ArrayList<>();

        Pageable pageable = PageRequest.of(0, 5, Sort.by("orderDate").descending());
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            UserDetails userDetail = (UserDetails) authentication.getPrincipal();
            model.addAttribute("userAuthen", userDetail.getUsername());
        } catch (Exception ex) {
            model.addAttribute("userAuthen", null);
        }

        Page<Order> orderPage =   orderRepository.findAllByOrderStatus(OrderStatus.CANCELLED, pageable);
        int totalPages = orderPage.getTotalPages();
        long totalItems = orderPage.getTotalElements();
        model.addAttribute("orderList", orderPage);
        model.addAttribute("currentPage", 1);
        model.addAttribute("totalPages", totalPages);
        model.addAttribute("totalItems", totalItems);
        for (Order o : orderList
        ) {
            if (o.getOrderStatus() == OrderStatus.CANCELLED) {
                orders.add(o);
            }
        }
        model.addAttribute("orderList", orders);
        return "Order/viewCanceledOrder";
    }

    @GetMapping("/listCanceled/{pageNumber}")
    public String viewListCanceled(Model model,@PathVariable(value = "pageNumber") int currentPage, OrderStatus orderStatus) {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            UserDetails userDetail = (UserDetails) authentication.getPrincipal();
            model.addAttribute("userAuthen", userDetail.getUsername());
        } catch (Exception ex) {
            model.addAttribute("userAuthen", null);
        }

        Pageable pageable = PageRequest.of(currentPage-1, 5,Sort.by("orderDate").descending());
        Page<Order> orderPage =  orderRepository.findAllByOrderStatus(OrderStatus.CANCELLED, pageable);
        int totalPages = orderPage.getTotalPages();
        long totalItems = orderPage.getTotalElements();
        model.addAttribute("orderList", orderPage);
        model.addAttribute("currentPage", currentPage);
        model.addAttribute("totalPages", totalPages);
        model.addAttribute("totalItems", totalItems);
        return "Order/viewCanceledOrder";
    }

    //Display after choose button Xác Nhận trên OrderView
    @GetMapping("/orderConfirm")
    public String OrderView(Model model, @RequestParam Integer id) throws OrderNotFoundException {
        model.addAttribute("order", orderService.getOrder(id));
        List<Object> list = orderRepository.managerOrder(id);
//        List<HistoryForUser> historyForUsers = new ArrayList<>();
//        HistoryForUser h;
        List<String> customerName = new ArrayList<>();
        List<String> quantities = new ArrayList<>();
        List<String> prices = new ArrayList<>();
        List<String> photos = new ArrayList<>();
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
        model.addAttribute("customerName", customerName);
        model.addAttribute("quantities", quantities);
        model.addAttribute("prices", prices);
        model.addAttribute("photos", photos);
        return "Order/orderConfirm";
    }

    @GetMapping("/orderDetail")
    public String orderDetail(Model model, @RequestParam Integer id) throws OrderNotFoundException {
//        model.addAttribute("order", orderRepository.getOne(id));
        model.addAttribute("order", orderService.getOrder(id));
        List<Object> list = orderRepository.managerOrder(id);
//        List<HistoryForUser> historyForUsers = new ArrayList<>();
//        HistoryForUser h;
        List<String> customerName = new ArrayList<>();
        List<String> quantities = new ArrayList<>();
        List<String> prices = new ArrayList<>();
        List<String> photos = new ArrayList<>();
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
        model.addAttribute("customerName", customerName);
        model.addAttribute("quantities", quantities);
        model.addAttribute("prices", prices);
        model.addAttribute("photos", photos);
        return "Order/orderDetail";
    }

    //Choose button in Form orderConfirm is button Xác nhận
    @GetMapping("/toConfirm")
    public String toConfirm(Model model, @RequestParam Integer id, RedirectAttributes redirectAttributes) throws OrderNotFoundException {
//        Order order = orderRepository.getOne(id);
        Order order = orderService.getOrder(id);
        List<OrderLine> orderLineList = new ArrayList<>();
        orderLineList = order.getOrderLineList();

        System.out.println("linnnnnnnnnnnnnnnnnn"+orderLineList);
        for (OrderLine orderLine : orderLineList) {
            Product product = orderLine.getProduct();
            if (product.getQuantity() >= orderLine.getQuantity()) {
                product.setQuantity(product.getQuantity() - orderLine.getQuantity());
                productRepository.save(product);
//                productService.saveProduct(product);
            } else {
                redirectAttributes.addFlashAttribute("mess", "Sản phẩm đã hết hàng, vui lòng liên hệ kho!");
                return "redirect:/orderConfirm?id=" + id;
            }
        }
        order.setStatus(1);
        order.setOrderStatus(OrderStatus.NEW);
        order.setOrderConfirmDate(new Date());
        orderRepository.save(order);
//        orderService.saveOrder(order);
        return "redirect:/OrderView";
    }

    @GetMapping("/searchByTime")
    public String searchByTime(Model model, @RequestParam String fromDate , @RequestParam String toDate) {
        List<Order> orderList = orderRepository.orders_by_time(fromDate,toDate);

        model.addAttribute("orderList", orderList);
        return "Order/View";
    }




//    @GetMapping("/ShippingEdit")
//    public String shippingEdit(Model model, @RequestParam Integer id) throws OrderNotFoundException {
//
//        model.addAttribute("order", orderService.getOrder(id));
//
//        return "Shipping/Edit";
//    }
//
//    @PostMapping("ShippingEdit")
//    public String shippingEdit (@RequestParam("id") Integer id, @ModelAttribute Order order){
//        try {
//            orderService.saveOrder(order);
//            return "redirect:/Shipping";
//        }catch (Exception ex){
//            return "redirect:/Shipping";
//        }
//    }



//    @GetMapping("/shippingNew")
//    public String orderNew(Model model) {
//        List<Order> orderList = orderService.listAll();
//        List<Order> orders = new ArrayList<>();
//        for (Order o : orderList
//        ) {
//            if (o.getOrderStatus() == OrderStatus.NEW) {
//                orders.add(o);
//            }
//        }
//        model.addAttribute("orderList", orders);
//        return "Order/View";
//    }


}

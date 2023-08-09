package com.example.test.Controller;

import com.example.test.Converter.ConvertObject;
import com.example.test.Entity.*;
import com.example.test.Exception.OrderNotFoundException;
import com.example.test.Exception.ProductNotFoundException;
import com.example.test.Repository.*;
import com.example.test.Security.CustomerOAuth2User;
import com.example.test.Service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

@Controller
public class TestController {
    @Autowired
    private ConvertObject convert;
    @Autowired
    ShoppingCartService shoppingCartService;

    private final OrderService orderService;
    private final ProductRepository productRepository;
    private final TypeRepository typeRepository;
    private final CustomerRepository customerRepository;
    private final OrderRepository orderRepository;
    private final OrderLineRepository orderLineRepository;
    private final UserRepository userRepository;
    private final ReviewService reviewService;
    private final ProductService productService;

    public TestController(OrderService orderService, ProductRepository productRepository,
                          TypeRepository typeRepository,
                          CustomerRepository customerRepository,
                          OrderRepository orderRepository,
                          OrderLineRepository orderLineRepository,
                          UserRepository userRepository, ReviewService reviewService, ProductService productService) {
        this.orderService = orderService;
        this.productRepository = productRepository;
        this.typeRepository = typeRepository;
        this.customerRepository = customerRepository;
        this.orderRepository = orderRepository;
        this.orderLineRepository = orderLineRepository;
        this.userRepository = userRepository;
        this.reviewService = reviewService;
        this.productService = productService;
    }

    @GetMapping("/home")
    public String Index(Model model) {

        String userName = authenticationName();

        try {
            model.addAttribute("userAuthen", userName);
        } catch (Exception ex) {
            model.addAttribute("userAuthen", null);
        }

//        model.addAttribute("itemsWithFineList", itemIssuanceService.getItemsWithFine());
        return "home";
    }

    @GetMapping("/intro")
    public String intro(Model model, HttpServletRequest request) {
        String userName = authenticationName();

        try {
            model.addAttribute("userAuthen", userName);
        } catch (Exception ex) {
            model.addAttribute("userAuthen", null);
        }


        return "IntroPage/intro";
    }

    @GetMapping("/product")
    public String product(Model model, @RequestParam(name = "id") Integer id) {

        String userName = authenticationName();

        try {
            model.addAttribute("userAuthen", userName);
        } catch (Exception ex) {
            model.addAttribute("userAuthen", null);
        }

        List<Product> productList= new ArrayList<>();
        for (Product product : productRepository.findAllByType(typeRepository.getOne(id))) {
            if(product.getStatus()==1){
//                System.out.println(product);
                productList.add(product);
            }
        }
        model.addAttribute("ProductList",productList );
        model.addAttribute("TypeList", typeRepository.findAll());
//        model.addAttribute("itemsWithFineList", itemIssuanceService.getItemsWithFine());
        return "IntroPage/product";
    }

    @GetMapping("/contact")
    public String contact(Model model) {
        String userName = authenticationName();

        try {
            model.addAttribute("userAuthen", userName);
        } catch (Exception ex) {
            model.addAttribute("userAuthen", null);
        }

//        model.addAttribute("itemsWithFineList", itemIssuanceService.getItemsWithFine());
        return "IntroPage/contact";
    }


    @GetMapping("/order")
    public String order(Model model) {
        OrderLine orderLine = new OrderLine(1, null, new Order(), new Product(), new ArrayList<>());
        model.addAttribute("customer", new Customer());

        String userName = authenticationName();

        try {
            model.addAttribute("userAuthen", userName);
        } catch (Exception ex) {
            model.addAttribute("userAuthen", null);
        }

        Collection<CartItem> cartItems = shoppingCartService.getCartItems(userName);
        model.addAttribute("cartItems", cartItems);
//        model.addAttribute("cartItems", cart);
        model.addAttribute("total", shoppingCartService.getAmount(userName));
        double totalPrice = 0;
        for (CartItem cartItem : cartItems) {
//            System.out.println(cartItem);
            double price = cartItem.getQuantity() * cartItem.getPrice();
            totalPrice += price;
        }
        model.addAttribute("totalPrice", totalPrice);
        model.addAttribute("totalCartItems", shoppingCartService.getCount(userName));

//        model.addAttribute("itemsWithFineList", itemIssuanceService.getItemsWithFine());
        return "IntroPage/order";
    }

    @PostMapping("/order")
    public String addOrder(Model model,
                           @ModelAttribute Customer customer,
                           RedirectAttributes redirectAttributes) {

        //Add new code to get username Facebook and Google
        String userName = authenticationName();
        try {
            model.addAttribute("userAuthen", userName);
        } catch (Exception ex) {
            model.addAttribute("userAuthen", null);
        }


        try {
            customerRepository.save(customer);
//            Collection<Cart> cart = shoppingCartServiceTest.getCart();
//            model.addAttribute("cart", cart);
//            model.addAttribute("total", shoppingCartService.getAmount());
            Collection<CartItem> cartItems = shoppingCartService.getCartItems(userName);
            model.addAttribute("cartItems", cartItems);
            model.addAttribute("total", shoppingCartService.getAmount(userName));
            double totalPrice = 0;
            for (CartItem cartItem : cartItems) {
                System.out.println(cartItem);
                double price = cartItem.getQuantity() * cartItem.getPrice();
                totalPrice += price;
            }
            Order order = new Order();
//            Calendar calendar = Calendar.getInstance();
//            Date currentDate = calendar.getTime();
            order.setOrderDate(new Date());
            order.setAmount(totalPrice);
            order.setCustomer(customerRepository.getOne(customer.getId()));
            order.setUser(userRepository.getUsersByUsername(userName));
            //New
            order.setPaymentMethod(PaymentMethod.CREDIT_CARD);
//            System.out.println(userRepository.getUsersByUsername(userDetail.getUsername()));
            orderRepository.save(order);
            List<OrderLine> orderLineList = new ArrayList<>();

            for (CartItem cartItem : cartItems) {
                OrderLine orderLine = new OrderLine();
//                System.out.println("oiddddddddđ" + order.getId());
                orderLine.setOrder(orderRepository.getOne(order.getId()));
                orderLine.setProduct(productRepository.getOne(cartItem.getId()));
                orderLine.setQuantity(cartItem.getQuantity());

                System.out.println(orderLine);
                orderLineRepository.save(orderLine);
                orderLineList.add(orderLine);
            }
            redirectAttributes.addFlashAttribute("success", "Đơn hàng đã được gửi thành công!");
            shoppingCartService.clear(userName);
        } catch (Exception ex) {
            redirectAttributes.addFlashAttribute("error", "Thông tin đơn hàng không hợp lệ, vui lòng nhập lại!");
        }

//        model.addAttribute("itemsWithFineList", itemIssuanceService.getItemsWithFine());
        return "redirect:/history";
    }


    @GetMapping("/addToCart")
    public String addToCart(Model model, @RequestParam Integer id) {
        String userName = authenticationName();
        Product product = productRepository.getOne(id);
        shoppingCartService.add(new CartItem(product.getId(), product.getName(), product.getPhoto(), 1, product.getPrice()), userName);
//        model.addAttribute("itemsWithFineList", itemIssuanceService.getItemsWithFine());
        System.out.println(shoppingCartService.getCartItems(userName).size());
        System.out.println(shoppingCartService.getCartItems(userName));
        return "redirect:/order";
    }


    @GetMapping("/history")
    public String history(Model model) {
        String userName = authenticationName();

        try {
            model.addAttribute("userAuthen", userName);
        } catch (Exception ex) {
            model.addAttribute("userAuthen", null);
        }
        Users users=  userRepository.getUsersByUsername(userName);
        List<Order> orders=users.getOrderList();
        model.addAttribute("orderList",orders);
        return "IntroPage/history";
    }

    @GetMapping("/historyDetail")
    public String orderDetail(Model model, @RequestParam Integer id) {
        String userName = authenticationName();
        try {
            model.addAttribute("userAuthen", userName);
        } catch (Exception ex) {
            model.addAttribute("userAuthen", null);
        }

        model.addAttribute("order", orderRepository.getOne(id));
        String total = String.valueOf(orderRepository.getOne(id).getAmount());
        String newTotal= total.substring(0,total.length()-2);
        model.addAttribute("total",newTotal);
        model.addAttribute("id" , id);
        List<Object> list = orderRepository.managerOrder(id);
//        List<HistoryForUser> historyForUsers = new ArrayList<>();
//        HistoryForUser h;

        List<String> productName = new ArrayList<>();
        List<String> quantities = new ArrayList<>();
        List<String> prices = new ArrayList<>();
        List<String> photos = new ArrayList<>();
        for (Object o : list) {
            String[] data = convert.convert(o);
            String cusName = (data[0]);
            String quantity = (data[2]);
            String price = (data[1]);
            String photo = (data[3].substring(1, data[3].length() - 1));

            productName.add(cusName);
            quantities.add(quantity);
            prices.add(price);
            photos.add(photo);
        }
        Users user = userRepository.findByUsername(userName);
        Order order = orderService.getOrder(id, user);
        setProductReviewableStatus(user, order);

        model.addAttribute("productName", productName);
        model.addAttribute("quantities", quantities);
        model.addAttribute("prices", prices);
        model.addAttribute("photos", photos);
        return "IntroPage/historyDetail";
    }

    private void setProductReviewableStatus(Users user, Order order){
        Iterator<OrderLine> iterator = order.getOrderLineList().iterator();

        while (iterator.hasNext()) {
            OrderLine orderLine = iterator.next();
            Product product = orderLine.getProduct();
            Integer productId = product.getId();

            boolean didUserReviewProduct = reviewService.didUserReviewProduct(user, productId);
            product.setReviewedByUser(didUserReviewProduct);

            if (!didUserReviewProduct){
                boolean canUserReviewProduct = reviewService.canUserReviewProduct(user, productId);
                product.setUserCanReview(canUserReviewProduct);
            }
        }

    }

    @GetMapping("/removeCart")
    public String removeToCart(Model model, @RequestParam Integer id) {
        String userName = authenticationName();
        shoppingCartService.remove(id, userName);
        return "redirect:/order";
    }


//    Create new Display
    @GetMapping("/productDetail")
    public String productDetailDemo(Model model, @RequestParam Integer id) throws OrderNotFoundException {
        String userName = authenticationName();
        try {
            model.addAttribute("userAuthen", userName);
        } catch (Exception ex) {
            model.addAttribute("userAuthen", null);
        }

        Product product = productRepository.getOne(id);
        Page<Review> listReviews = reviewService.list3MostRecentReviewsByProduct(product);

        model.addAttribute("listReviews", listReviews);
        model.addAttribute("product", productRepository.getOne(id));
        return "IntroPage/ProductDetail";
    }


    @GetMapping("/")
    public String homeIntro(Model model) {
        String userName = authenticationName();
        try {
            model.addAttribute("userAuthen", userName);
        } catch (Exception ex) {
            model.addAttribute("userAuthen", null);
        }

        List<Product> productList= new ArrayList<>();
        for (Product product : productRepository.findAll()) {
            if(product.getStatus()==1){
                productList.add(product);
            }
        }
        model.addAttribute("ProductList",productList );
        return "IntroPage/home";
    }

    @GetMapping ("/login")
    public String loginTest(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(authentication == null || authentication instanceof AnonymousAuthenticationToken){
            return "User/login";
        }
        return "redirect:/";
    }

    //Display detail order with button Xác Nhận Hủy Đơn Hàng on Frontend
    @GetMapping("processingOrderDetail")
    public String processingOrderDetail(Model model, @RequestParam Integer id) {
        String userName = authenticationName();
        try {
            model.addAttribute("userAuthen", userName);
        } catch (Exception ex) {
            model.addAttribute("userAuthen", null);
        }

        model.addAttribute("order", orderRepository.getOne(id));
        String total = String.valueOf(orderRepository.getOne(id).getAmount());
        String newTotal= total.substring(0,total.length()-2);
        model.addAttribute("total",newTotal);
        model.addAttribute("id" , id);
        List<Object> list = orderRepository.managerOrder(id);

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
        return "IntroPage/cancelOrderDetail";
    }

//Processing after choose button Xác Nhận Hủy Đơn Hàng
    @GetMapping("/toConfirmProcessing")
    public String toProcessingConfirm(Model model, @RequestParam Integer id, RedirectAttributes redirectAttributes) throws OrderNotFoundException {
        Order order = orderService.getOrder(id);
        order.setStatus(-1);
        order.setOrderConfirmDate(new Date());
        order.setOrderStatus(OrderStatus.PROCESSING);
        orderRepository.save(order);

        return "redirect:/history";
    }


    @GetMapping("/cancelOrderDetail")
    public String cancelOrderDetail(Model model, @RequestParam Integer id) throws OrderNotFoundException {
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
        return "Order/confirmCancelOrder";
    }


    @GetMapping("/toConfirmCancel")
    public String toCancelConfirm(Model model, @RequestParam Integer id, RedirectAttributes redirectAttributes) throws OrderNotFoundException {
        Order order = orderService.getOrder(id);

        order.setOrderStatus(OrderStatus.CANCELLED);
        order.setStatus(-2);
        order.setOrderConfirmDate(new Date());
        orderRepository.save(order);
//        orderService.saveOrder(order);
        return "redirect:/listCanceled";
    }

    @GetMapping("/write_review/product")
    public String writeReviewProduct(Model model, @RequestParam(name = "id") Integer id) throws ProductNotFoundException {
        Review review = new Review();
        String userName = authenticationName();
        Users users = userRepository.findByUsername(userName);
        Product product = productService.getProduct(id);
        try {
            model.addAttribute("userAuthen", userName);
        } catch (Exception ex) {
            model.addAttribute("userAuthen", null);
        }

        boolean userReviewed = reviewService.didUserReviewProduct(users, product.getId());

        if (userReviewed) {
            model.addAttribute("userReviewed", userReviewed);
        } else {
            boolean userCanReview = reviewService.canUserReviewProduct(users , product.getId());

            if (userCanReview) {
                model.addAttribute("userCanReview", userCanReview);
            } else {
                model.addAttribute("NoReviewPermission", true);
            }
        }

        model.addAttribute("review", review);
        model.addAttribute("product", productService.getProduct(id));
        return "Item_Review/Test";
    }

    @PostMapping("/post_review")
    public String saveReview(Model model, Integer productId, Review review) throws ProductNotFoundException {
        String userName = authenticationName();
        Users users = userRepository.findByUsername(userName);
        try {
            model.addAttribute("userAuthen", userName);
        } catch (Exception ex) {
            model.addAttribute("userAuthen", null);
        }
        Product product = productService.getProduct(productId);
        review.setProduct(product);
        review.setUser(users);


        Review savedReview = reviewService.saveReview(review);

        model.addAttribute("review", savedReview);

        return "redirect:/history";
    }

    public String authenticationName() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Object auth = authentication.getPrincipal();
        String userLoginName = null;
        if (auth instanceof UserDetails) {
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            userLoginName = userDetails.getUsername();
        } else if (auth instanceof CustomerOAuth2User) {
            CustomerOAuth2User userDetails = (CustomerOAuth2User) authentication.getPrincipal();
            userLoginName = userDetails.getName();
        }
        return userLoginName;
    }
}



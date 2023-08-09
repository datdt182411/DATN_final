package com.example.test.Controller;//package com.example.test.Controller;
//
//import com.example.test.Entity.Cart;
//import com.example.test.Entity.Product;
//import com.example.test.Repository.CartRepository;
//import com.example.test.Repository.ProductRepository;
//import com.example.test.Service.CartService;
//import org.springframework.stereotype.Controller;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestParam;
//
//import javax.servlet.http.HttpSession;
//import java.math.BigDecimal;
//
//@Controller
//public class CartController {
//
//    private final ProductRepository productRepository;
//    private final CartRepository cartRepository;
//    private final CartService cartService;
//
//    public CartController(ProductRepository productRepository, CartRepository cartRepository, CartService cartService){
//        this.productRepository = productRepository;
//        this.cartRepository = cartRepository;
//        this.cartService = cartService;
//    }
//
//    @PostMapping("/add-to-cart")
//    public String addToCart(@RequestParam("productid") Integer productId,
//                            @RequestParam("cartid") Integer cartId,
//                            @RequestParam("userid") Integer userId,
//                            @RequestParam("quantity") int quantity, HttpSession session) {
//        // Kiểm tra xem người dùng đã đăng nhập hay chưa
//        if (session.getAttribute("customer") == null) {
//            // Nếu chưa đăng nhập, chuyển hướng về trang đăng nhập
//            return "redirect:/login";
//        }
//        // Kiểm tra tính hợp lệ của thông tin
//        if (productId <= 0 || cartId <= 0 || userId <= 0 || quantity <= 0) {
//            return "redirect:/error"; // Chuyển hướng đến trang lỗi nếu thông tin không hợp lệ
//        }
//
//        // Lấy đối tượng Product từ productRepository bằng productId
//        Product product = productRepository.findById(productId).orElse(null);
//        Cart cart = cartRepository.findById(cartId).orElse(null);
//
//        if (product == null) {
//            return "redirect:/error"; // Chuyển hướng đến trang lỗi nếu không tìm thấy sản phẩm
//        }
//
//        // Lấy giá của sản phẩm từ Product và gán vào một biến price
//        BigDecimal price = product.getPrice();
//
//        // Tính giá tiền bằng cách nhân quantity từ request với price
//        BigDecimal totalPrice = price.multiply(BigDecimal.valueOf(quantity));
//
//        // Thực hiện insert vào bảng cart_detail
//        CartDetail existingCartDetail = cartDetailRepository.findByProductAndCart(product, cart);
//        if (existingCartDetail != null) {
//            // Nếu sản phẩm đã tồn tại trong giỏ hàng, cập nhật số lượng
//            int newQuantity = existingCartDetail.getQuantity() + quantity;
//            BigDecimal newTotalPrice = price.multiply(BigDecimal.valueOf(newQuantity));
//            existingCartDetail.setQuantity(newQuantity);
//            existingCartDetail.setPrice(newTotalPrice);
//            cartDetailRepository.save(existingCartDetail);
//        } else {
//            // Nếu sản phẩm chưa tồn tại trong giỏ hàng, thêm mới một CartDetail
//            CartDetail cartDetail = new CartDetail();
//            cartDetail.setProduct(product);
//            cartDetail.setCart(cart);
//            cartDetail.setQuantity(quantity);
//            cartDetail.setPrice(totalPrice);
//            cartDetailRepository.save(cartDetail);
//        }
//
//        return "redirect:/product/"+ product.getId(); // Chuyển hướng về trang sản phẩm đang chọn
//    }
//}

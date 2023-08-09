package com.example.test.Controller;

import com.example.test.Entity.Customer;
import com.example.test.Entity.Utility;
import com.example.test.Exception.CustomerNotFoundException;
import com.example.test.Service.CustomerService;
import com.example.test.Service.ShoppingCartServiceTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
public class ShoppingCartRestController {
    @Autowired
    private ShoppingCartServiceTest shoppingCartServiceTest;

    @Autowired
    private CustomerService customerService;

    @PostMapping("/cart/add/{productId}/{quantity}")
    public String addProductToCart(@PathVariable ("productId") Integer productId,
                                   @PathVariable ("quantity") Integer quantity, HttpServletRequest request) {

        try {
            Customer customer = getAuthenticatedCustomer(request);
            Integer updatedQuantity = shoppingCartServiceTest.addProduct(productId, quantity, new Customer());
            return updatedQuantity + " item(s) of this product were added to your shopping cart";
        }catch (CustomerNotFoundException e){
            return "You must login to add this product to cart";
        }
    }


    private Customer getAuthenticatedCustomer(HttpServletRequest request) throws CustomerNotFoundException {

        String email = Utility.getEmailOfAuthenticatedCustomer(request);
        if (email == null){
            new CustomerNotFoundException("No authenticated customer");
        }

        return customerService.getCustomerByEmail(email);
    }

}

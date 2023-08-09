package com.example.test.Service;

import com.example.test.Entity.CartItem;
import com.example.test.Entity.Users;
import com.example.test.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;


@Service
public interface ShoppingCartService {

	int getCount(String username);

	double getAmount(String username);

	void clear(String username);

	Collection<CartItem> getCartItems(String username);

	void remove(CartItem item, String username);

	void add(CartItem item, String username);

	void remove(Integer id, String username);

}

package com.example.test.Service.Impl;

import com.example.test.Entity.CartItem;
import com.example.test.Entity.Users;
import com.example.test.Service.ShoppingCartService;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class ShoppingCartServiceImpl implements ShoppingCartService {

//	private Map<Integer, CartItem> map = new HashMap<Integer, CartItem>();
	Map<String, List<CartItem>> cartManager = new ConcurrentHashMap<>();
	Object lock = new Object();


	@Override
	public void add(CartItem item, String username) {

		synchronized (lock) {
			List<CartItem> listItem = cartManager.get(username);
			if (listItem == null) {
				listItem = new ArrayList<>();
				cartManager.put(username, listItem);
			}

			boolean contains = false;
			int existingItemIndex = -1;

			for (int i = 0; i < listItem.size(); i++) {
				CartItem cartItem = listItem.get(i);
				if (cartItem.getId() == (item.getId())) {
					contains = true;
					existingItemIndex = i;
					break;
				}
			}

			if (contains) {
				CartItem existingItem = listItem.get(existingItemIndex);
				int newQuantity = existingItem.getQuantity() + item.getQuantity();
				existingItem.setQuantity(newQuantity);
			} else {
				listItem.add(item);

			}

		}
	}



	@Override
	public void remove(CartItem item, String username) {

//		map.remove(item.getId());
		List<CartItem> listItem = cartManager.get(username);
		listItem.remove(item);
	}

	@Override
	public Collection<CartItem> getCartItems(String username) {
		List<CartItem> listItem = cartManager.get(username);
		if(listItem == null) return  Collections.emptyList();
		return listItem;
	}

	@Override
	public void clear(String username) {
		cartManager.remove(username);
	}

	@Override
	public double getAmount(String username) {
		List<CartItem> listItem = cartManager.get(username);
		if(listItem == null) return 0;
		return listItem.stream().mapToDouble(item -> item.getQuantity() * item.getPrice()).sum();
	}

	@Override
	public int getCount(String username) {
		List<CartItem> listItem = cartManager.get(username);
		if(listItem == null) return 0;
		return listItem.size();
	}

	@Override
	public void remove(Integer id, String username) {
		List<CartItem> listItem = cartManager.get(username);
		if(listItem != null) listItem.remove(new CartItem(id));
	}
}

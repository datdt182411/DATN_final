package com.example.test.Entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CartItem {
    private  int id;
    private  String name;
    private  String photo;
    private  int quantity;
    private  Double price;
    private  Double total;

    public CartItem(int id, String name, String photo, int quantity, Double price) {
        this.id = id;
        this.name = name;
        this.photo = photo;
        this.quantity = quantity;
        this.price = price;
    }

    public CartItem(int id) {
        this.id = id;
    }

    @Override
    public boolean equals(Object obj) {
        return this.id  == ((CartItem)obj).id;
    }
}

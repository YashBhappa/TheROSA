package com.finalyear.microproject.interfaces;

import com.finalyear.microproject.model.Cart;

import java.util.ArrayList;

public interface UpdateSelectedItems {
    void addItems(String name, String price, String category, int quantity, String res_name);

    ArrayList<Cart> getItems();
}

package com.finalyear.microproject.misc;

import android.app.Application;
import android.content.Context;

import com.finalyear.microproject.interfaces.UpdateSelectedItems;
import com.finalyear.microproject.model.Cart;

import java.util.ArrayList;

public class ApplicationMain extends Application implements UpdateSelectedItems {

    private static Context context;
    ArrayList<Cart> carts;

    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();
        carts = new ArrayList<>();
    }

    public static Context getMyContext(){
        return context;
    }

    @Override
    public void addItems(String name, String price, String category, int quantity, String res_name) {
        //carts.add(new Cart(name, price, category, quantity, res_name));

    }

    @Override
    public ArrayList<Cart> getItems() {
        return carts;
    }


}

package com.finalyear.microproject.extras;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;

import com.finalyear.microproject.R;
import com.finalyear.microproject.interfaces.UpdateSelectedItems;
import com.finalyear.microproject.adapter.CartAdapter;
import com.google.android.material.appbar.MaterialToolbar;

public class CartActivity extends AppCompatActivity {

    RecyclerView orderRv;
    CartAdapter cartAdapter;
    Activity context;
    UpdateSelectedItems updateSelectedItems;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_cart);

        MaterialToolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Confirm Order");

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        orderRv =findViewById(R.id.recycler_food_items);
        //cartAdapter = new CartAdapter(context);
        orderRv.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL,false));
        orderRv.setAdapter(cartAdapter);

    }
}
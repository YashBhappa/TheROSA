package com.finalyear.microproject.adapter;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.finalyear.microproject.R;
import com.finalyear.microproject.fragment.RestaurantFragment;
import com.finalyear.microproject.model.Restaurant;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class ShopAdapter extends RecyclerView.Adapter<ShopAdapter.MyViewHolder> {

    Context context;
    private final List<Restaurant> restaurantList;
    private final OnShopsListener mOnShopsListener;

    public ShopAdapter(List<Restaurant> restaurantList, Context context, OnShopsListener mOnShopsListener) {
        this.context = context;
        this.restaurantList = restaurantList;
        this.mOnShopsListener = mOnShopsListener;
    }

    @NonNull
    @NotNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_shop, parent, false);

        return new MyViewHolder(itemView, mOnShopsListener);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull MyViewHolder holder, int position) {
        Restaurant shop = restaurantList.get(position);
        holder.title.setText(shop.getName());
        holder.time.setText(shop.getDescription());
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                FragmentManager manager = ((AppCompatActivity)context).getSupportFragmentManager();
                RestaurantFragment restaurantFragment = new RestaurantFragment();
                restaurantFragment.setArguments(bundle);
                bundle.putString("name",shop.getName());
                manager.beginTransaction().replace(R.id.container,restaurantFragment)
                        .commit();
            }
        });
    }

    @Override
    public int getItemCount() {
        return restaurantList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView title;
        public TextView time;
        public CardView cardView;
        OnShopsListener onShopsListener;

        public MyViewHolder(View view, OnShopsListener onShopsListener){
            super(view);
            title = view.findViewById(R.id.text_shop_name);
            time = view.findViewById(R.id.text_shop_desc);
            cardView = view.findViewById(R.id.layout_root);
            this.onShopsListener = onShopsListener;
        }

        @Override
        public void onClick(View v) {
            onShopsListener.onShopsListener(getAdapterPosition());
        }
    }
    public interface OnShopsListener{
        void onShopsListener(int position);
    }
}

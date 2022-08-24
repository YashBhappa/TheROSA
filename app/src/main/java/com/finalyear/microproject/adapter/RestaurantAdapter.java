package com.finalyear.microproject.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.finalyear.microproject.R;
import com.finalyear.microproject.fragment.RestaurantFragment;
import com.finalyear.microproject.model.Restaurant;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.jetbrains.annotations.NotNull;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

public class RestaurantAdapter extends FirebaseRecyclerAdapter<Restaurant, RestaurantAdapter.ResViewHolder> implements View.OnClickListener {
    Context context;
    /**
     * Initialize a {@link RecyclerView.Adapter} that listens to a Firebase query. See
     * {@link FirebaseRecyclerOptions} for configuration options.
     *
     * @param options
     */
    public RestaurantAdapter(@NonNull @NotNull FirebaseRecyclerOptions<Restaurant> options, Context context) {
        super(options);
        this.context = context;
    }

    @Override
    public void onClick(View v) {

    }

    @Override
    protected void onBindViewHolder(@NonNull @NotNull ResViewHolder holder, int position, @NonNull @NotNull Restaurant model) {
        holder.res_name.setText(model.getName());
        holder.res_desc.setText(model.getDescription());

        holder.restaurantView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                FragmentManager manager = ((AppCompatActivity)context).getSupportFragmentManager();
                RestaurantFragment restaurantFragment = new RestaurantFragment();

                restaurantFragment.setArguments(bundle);
                bundle.putString("name",model.getName());

                FragmentTransaction transaction = manager.beginTransaction();
                manager.beginTransaction().addToBackStack("Restaurant");
                manager.beginTransaction().setCustomAnimations(
                        R.anim.slide_in,  // enter
                        R.anim.fade_out,  // exit
                        R.anim.fade_in,   // popEnter
                        R.anim.slide_out)
                        .replace(R.id.container,restaurantFragment, "Restaurant")
                        .commit();
            }
        });

    }

    @NonNull
    @NotNull
    @Override
    public ResViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_shop, parent, false);
        ResViewHolder resViewHolder = new ResViewHolder(view);
        return resViewHolder;

    }

    public static class ResViewHolder extends RecyclerView.ViewHolder {
        public TextView res_name, res_desc;
        CardView restaurantView;

        public ResViewHolder(@NonNull View itemView){
            super(itemView);
            res_name = itemView.findViewById(R.id.text_shop_name);
            res_desc = itemView.findViewById(R.id.text_shop_desc);
            restaurantView = itemView.findViewById(R.id.layout_root);
        }
    }

    public interface OnRestaurantListener {
        void onRestaurantClicked(int position);
    }
}

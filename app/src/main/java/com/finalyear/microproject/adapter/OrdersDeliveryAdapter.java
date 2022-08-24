package com.finalyear.microproject.adapter;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.finalyear.microproject.R;
import com.finalyear.microproject.fragment.OrderDetailsFragment;
import com.finalyear.microproject.model.Orders;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;

import org.jetbrains.annotations.NotNull;

import java.text.MessageFormat;

public class OrdersDeliveryAdapter extends FirebaseRecyclerAdapter<Orders, OrdersDeliveryAdapter.OrdersViewHolder> {
    private static final String TAG = "OrdersFragment";
    Context context;

    public OrdersDeliveryAdapter(@NonNull FirebaseRecyclerOptions<Orders> options, Context context){
        super(options);
        this.context = context;
    }

    @Override
    protected void onBindViewHolder(@NonNull @NotNull OrdersViewHolder holder, int position, @NonNull @NotNull Orders model) {
        holder.orderName.setText(model.getName());
        holder.orderDateTime.setText((model.getDate() + " " + model.getTime()));
        holder.orderAmount.setText(MessageFormat.format("₹{0}", model.getOrderAmount()));
        if(model.getAddress() != null) {
            holder.orderAddress.setText(MessageFormat.format("{0}\n{1}", model.getAddress(), model.getCityState()));
        }else{
            holder.addressLayout.setVisibility(View.GONE);
        }
        holder.orderStatus.setText(model.getState());

        holder.trackOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                FragmentTransaction fragmentTransaction = ((AppCompatActivity) context).getSupportFragmentManager().beginTransaction();
                OrderDetailsFragment orderDetailsFragment = new OrderDetailsFragment();
                orderDetailsFragment.setArguments(bundle);
                bundle.putString("uuid", model.getUuid());
                bundle.putString("type", "Delivery");
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.setCustomAnimations(
                        R.anim.slide_in,
                        R.anim.fade_out,
                        R.anim.fade_in,
                        R.anim.slide_out
                );
                fragmentTransaction.replace(R.id.container, orderDetailsFragment);
                fragmentTransaction.commit();
            }
        });
    }

    @NonNull
    @NotNull
    @Override
    public OrdersViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_order, parent, false);
        return new OrdersViewHolder(view);
    }

    public static class OrdersViewHolder extends RecyclerView.ViewHolder {
        TextView orderName, orderDateTime, orderAmount, orderStatus, orderAddress;
        Button trackOrder;
        LinearLayout addressLayout;

        public OrdersViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);

            orderName = itemView.findViewById(R.id.text_order_name);
            orderAmount = itemView.findViewById(R.id.text_order_price);
            orderDateTime = itemView.findViewById(R.id.text_order_time);
            orderStatus = itemView.findViewById(R.id.text_order_status);

            addressLayout = itemView.findViewById(R.id.address_layout);
            orderAddress = itemView.findViewById(R.id.text_order_address);

            trackOrder = itemView.findViewById(R.id.button_track_order);
        }
    }
}

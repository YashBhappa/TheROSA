package com.finalyear.microproject.adapter;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.finalyear.microproject.R;
import com.finalyear.microproject.model.OrderDetails;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;

import org.jetbrains.annotations.NotNull;

public class OrderDetailsAdapter extends FirebaseRecyclerAdapter<OrderDetails, OrderDetailsAdapter.OrdersDetailsAdapter> {
    Context context;
    int TotalOrderAmount = 0;

    public OrderDetailsAdapter(@NonNull @NotNull FirebaseRecyclerOptions<OrderDetails> options, Context context) {
        super(options);
        this.context = context;
    }

    @Override
    protected void onBindViewHolder(@NonNull @NotNull OrdersDetailsAdapter holder, int position, @NonNull @NotNull OrderDetails model) {
        holder.foodName.setText(model.getName());
        holder.foodQuantity.setText(String.valueOf(model.getQuantity()));

        Uri uri = checkCategory(model.getCategory());
        holder.foodCategory.setImageURI(uri);

        holder.foodPrice.setText(String.valueOf(calculateTotal(model)));
    }

    private Uri checkCategory(String category) {
        if (category.equalsIgnoreCase("veg")) {
            return Uri.parse("android.resource://" + context.getPackageName() + "/" + R.drawable.ic_veg);
        }
        else if (category.equalsIgnoreCase("non veg")) {
            return Uri.parse("android.resource://" + context.getPackageName() + "/" + R.drawable.ic_non_veg);
        }
        return null;
    }

    private int calculateTotal(OrderDetails model) {
        int total = Integer.parseInt(model.getPrice()) * model.getQuantity();
        TotalOrderAmount += total;
        return total;
    }

    @NonNull
    @NotNull
    @Override
    public OrdersDetailsAdapter onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_order_product, parent, false);
        return new OrdersDetailsAdapter(view);
    }

    public static class OrdersDetailsAdapter extends RecyclerView.ViewHolder {
        ImageView foodCategory;
        TextView foodName, foodQuantity, foodPrice;

        public OrdersDetailsAdapter(@NonNull @NotNull View itemView) {
            super(itemView);

            foodCategory = itemView.findViewById(R.id.image_category);
            foodQuantity = itemView.findViewById(R.id.text_order_quantity);
            foodName = itemView.findViewById(R.id.text_food_name);
            foodPrice = itemView.findViewById(R.id.text_food_price);

        }
    }
}

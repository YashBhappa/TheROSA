package com.finalyear.microproject.adapter;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.finalyear.microproject.R;
import com.finalyear.microproject.misc.SharedPrefManager;
import com.finalyear.microproject.model.Cart;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Objects;
import java.util.UUID;

public class CartAdapter extends FirebaseRecyclerAdapter<Cart, CartAdapter.cartViewHolder> {
    Context context;
    int finalCartAmount = 0;
    int itemCount = 1;
    SharedPrefManager sharedPrefManager;
    String uuid;

    public CartAdapter(@NonNull FirebaseRecyclerOptions<Cart> options, Context context) {
        super(options);
        this.context = context;
        this.sharedPrefManager = new SharedPrefManager(context);
        this.uuid = sharedPrefManager.getUUID();
    }

    @Override
    protected void onBindViewHolder(@NonNull cartViewHolder holder, int position, @NonNull Cart model) {
        holder.foodName.setText(model.getName());
        holder.foodPrice.setText(model.getPrice());
        holder.foodResName.setText(model.getRes_name());

        if(itemCount == 1) {
            calculateTotalCartAmount(model);
        }

        int foodTotal = Integer.parseInt(model.getPrice()) * model.getQuantity();
        holder.foodTotalPrice.setText(String.valueOf(foodTotal));

        Uri uri = checkCategory(model.getCategory());
        holder.foodCategory.setImageURI(uri);

        holder.foodQuantity.setText(String.valueOf(model.getQuantity()));
        holder.quantity.setText(String.valueOf(model.getQuantity()));
        holder.minusFromCart.setVisibility(View.VISIBLE);

        holder.addToCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                {   itemCount++;
                    if (model.getQuantity() == 0) {
                        model.setQuantity(model.getQuantity() + 1);
                    }
                    if (!(model.getQuantity() >= 10)) {
                        if (model.getQuantity() >= 1) {
                            int Quantity = model.getQuantity();
                            Quantity += 1;
                            model.setQuantity(Quantity);
                            holder.quantity.setText(String.valueOf(Quantity));
                            holder.foodQuantity.setText(String.valueOf(Quantity));
                            updatingCart(model);
                        }
                    } else {
                        Toast.makeText(context, "Maximum limit reached", Toast.LENGTH_SHORT).show();
                        holder.addToCart.setEnabled(false);
                    }
                }
                updateCartAmount(Integer.parseInt(model.getPrice()));
            }
        });

        holder.minusFromCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    int Quantity = model.getQuantity();
                    if (Quantity == 1) {
                        subtractCartAmount(Integer.parseInt(model.getPrice()));
                        removeFromCart(model);
                    } else if (Quantity > 1) {
                        Quantity -= 1;
                        model.setQuantity(Quantity);
                        holder.quantity.setText(String.valueOf(Quantity));
                        holder.foodQuantity.setText(String.valueOf(Quantity));
                        subtractingFromCart(model);
                        subtractCartAmount(Integer.parseInt(model.getPrice()));
                    }
            }
        });

    }

    private void calculateTotalCartAmount(Cart model) {
        int cartTotal = (Integer.parseInt(model.getPrice()))*model.getQuantity();
        finalCartAmount += cartTotal;
        addCartAmount(finalCartAmount);
    }

    private void addCartAmount(int finalCartAmount) {
        final DatabaseReference amountReference = FirebaseDatabase.getInstance().getReference().child("Cart");

        final HashMap<String, Object> amountCart = new HashMap<>();
        amountCart.put("Cart Amount", finalCartAmount);

        amountReference.child("User").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child(uuid)
                .updateChildren(amountCart)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull @NotNull Task<Void> task) {
                        if(task.isSuccessful()){
                            amountReference.child("Admin").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child(uuid)
                                    .updateChildren(amountCart)
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull @NotNull Task<Void> task) {
                                        }
                                    });
                        }
                    }
                });
        }

    private void updateCartAmount(int price) {
        finalCartAmount += price;

        final DatabaseReference amountReference = FirebaseDatabase.getInstance().getReference().child("Cart")
                .child("User").child(Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid()).child(uuid);

        amountReference.child("Cart Amount").setValue(finalCartAmount);

        final DatabaseReference amountReference1 = FirebaseDatabase.getInstance().getReference().child("Cart")
                .child("Admin").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child(uuid);

        amountReference1.child("Cart Amount").setValue(finalCartAmount);
    }

    private void subtractCartAmount(int price) {
        finalCartAmount -= price;

        final DatabaseReference amountReference = FirebaseDatabase.getInstance().getReference().child("Cart")
                .child("User").child(Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid()).child(uuid);

        amountReference.child("Cart Amount").setValue(finalCartAmount);

        final DatabaseReference amountReference1 = FirebaseDatabase.getInstance().getReference().child("Cart")
                .child("Admin").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child(uuid);

        amountReference1.child("Cart Amount").setValue(finalCartAmount);
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

    private void updatingCart(Cart model) {

        final DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference()
                .child("Cart")
                .child("User")
                .child(Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid()).child(uuid)
                .child("Food").child(model.getName());

        databaseReference.child("quantity").setValue(model.getQuantity());

        final DatabaseReference databaseReference1 = FirebaseDatabase.getInstance().getReference()
                .child("Cart")
                .child("Admin")
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child(uuid)
                .child("Food").child(model.getName());

        databaseReference1.child("quantity").setValue(model.getQuantity());

    }

    private void removeFromCart(Cart model) {

        final DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("Cart")
                .child("User")
                .child(Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid()).child(uuid)
                .child("Food").child(model.getName());

        databaseReference.removeValue();

        final DatabaseReference databaseReference1 = FirebaseDatabase.getInstance().getReference().child("Cart")
                .child("Admin")
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child(uuid)
                .child("Food").child(model.getName());

        databaseReference1.removeValue();
    }

    private void subtractingFromCart(Cart model) {
        final DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("Cart")
                .child("User")
                .child(Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid()).child(uuid)
                .child("Food").child(model.getName());

        databaseReference.child("quantity").setValue(model.getQuantity());

        final DatabaseReference databaseReference1 = FirebaseDatabase.getInstance().getReference().child("Cart")
                .child("Admin")
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child(uuid)
                .child("Food").child(model.getName());

        databaseReference1.child("quantity").setValue(model.getQuantity());


    }

    @NonNull
    @Override
    public cartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_cart_product, parent, false);
        return new cartViewHolder(view);
    }

    class cartViewHolder extends RecyclerView.ViewHolder {
        TextView foodName, foodPrice, foodQuantity, foodTotalPrice;
        ImageView foodCategory;
        TextView foodResName;
        ImageView addToCart, minusFromCart;
        TextView quantity,multiply;

        public cartViewHolder(@NonNull View itemView)
        {
            super(itemView);
            foodName = itemView.findViewById(R.id.text_food_name);
            foodPrice = itemView.findViewById(R.id.text_food_price);
            foodQuantity = itemView.findViewById(R.id.text_quantity);
            foodCategory = itemView.findViewById(R.id.image_category);
            foodTotalPrice = itemView.findViewById(R.id.text_cart_food_price);
            foodResName = itemView.findViewById(R.id.cart_shop_name);

            addToCart = itemView.findViewById(R.id.image_add);
            minusFromCart = itemView.findViewById(R.id.image_sub);

            multiply = itemView.findViewById(R.id.multiply);
            quantity = itemView.findViewById(R.id.cart_quantity);
        }
    }
}

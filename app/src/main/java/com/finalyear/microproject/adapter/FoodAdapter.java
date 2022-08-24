package com.finalyear.microproject.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
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
import com.finalyear.microproject.model.Food;
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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.UUID;

public class FoodAdapter extends FirebaseRecyclerAdapter<Food, FoodAdapter.foodViewHolder> {
    Context context;
    String res_name;
    SharedPrefManager sharedPrefManager;

    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    String uuid;
    int count;

    public FoodAdapter(@NonNull FirebaseRecyclerOptions<Food> options, Context context, String res_name) {
        super(options);
        this.context = context;
        this.res_name = res_name;
        this.sharedPrefManager = new SharedPrefManager(context);
        if (sharedPrefManager.getUUID().equals("")) {
            sharedPrefManager.saveUUID(context, (UUID.randomUUID().toString()));
            this.uuid = sharedPrefManager.getUUID();
        } else {
            sharedPreferences = context.getSharedPreferences("uuid", Context.MODE_PRIVATE);
            if((sharedPreferences.getString("uniqueID", "")).equals("")){
                sharedPrefManager.saveUUID(context, (UUID.randomUUID().toString()));
                this.uuid = sharedPrefManager.getUUID();
            }
            else {
                this.uuid = sharedPreferences.getString("uniqueID", "");
            }
        }
    }

    protected void onBindViewHolder(@NonNull foodViewHolder holder, int position, @NonNull Food model) {
        sharedPreferences = context.getSharedPreferences("uuid", Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();

        holder.foodName.setText(model.getName());
        holder.foodPrice.setText(model.getPrice());

        Uri uri = checkCategory(model.getCategory());
        holder.foodCategory.setImageURI(uri);

        Uri uri_type = checkType(model.getType());
        holder.foodImage.setImageURI(uri_type);

        if (model.getQuantity() == 0) {
            model.setQuantity(model.getQuantity() + 1);
        }

        holder.addToCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String quantityText = (String) holder.quantity.getText();
                if (model.getQuantity() == 0) {
                    model.setQuantity(model.getQuantity() + 1);
                }
                if (!(model.getQuantity() >= 10)) {
                    if (quantityText.equalsIgnoreCase("Add")) {
                        holder.quantity.setText(String.valueOf(model.getQuantity()));
                        holder.minusFromCart.setVisibility(View.VISIBLE);
                        addingToCart(model);
                    } else if (model.getQuantity() >= 1) {
                        int Quantity = model.getQuantity();
                        Quantity += 1;
                        model.setQuantity(Quantity);
                        holder.quantity.setText(String.valueOf(Quantity));
                        updatingCart(model);
                    }
                } else {
                    Toast.makeText(context, "Maximum limit reached", Toast.LENGTH_SHORT).show();
                    holder.addToCart.setEnabled(false);
                }
            }

        });

        holder.minusFromCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int Quantity = model.getQuantity();
                if (Quantity == 1) {
                    Quantity -= 1;
                    model.setQuantity(Quantity);
                    holder.quantity.setText("Add");
                    holder.minusFromCart.setVisibility(View.GONE);
                    removeFromCart(model);
                } else if (Quantity > 1) {
                    Quantity -= 1;
                    model.setQuantity(Quantity);
                    holder.quantity.setText(String.valueOf(Quantity));
                    subtractingFromCart(model);
                }
            }
        });
    }

    private void addingToCart(Food model) {
        String saveCurrentDate, saveCurrentTime;

        Calendar calForDate = Calendar.getInstance();
        @SuppressLint("SimpleDateFormat") SimpleDateFormat currentDate = new SimpleDateFormat("MMM dd, yyyy");
        saveCurrentDate = currentDate.format(calForDate.getTime());

        @SuppressLint("SimpleDateFormat") SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm:ss");
        saveCurrentTime = currentTime.format(calForDate.getTime());

        final DatabaseReference cartReference = FirebaseDatabase.getInstance().getReference().child("Cart");

        final HashMap<String, Object> cartMap = new HashMap<>();
        cartMap.put("res_name", res_name);
        cartMap.put("name", model.getName());
        cartMap.put("price", model.getPrice());
        cartMap.put("type", model.getType());
        cartMap.put("category", model.getCategory());
        cartMap.put("quantity", model.getQuantity());
        cartMap.put("date", saveCurrentDate);
        cartMap.put("time", saveCurrentTime);

        final HashMap<String, Object> amountMap = new HashMap<>();
        amountMap.put("Cart Amount", 0);

        cartReference.child("User").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child(uuid)
                .child("Food").child(model.getName())
                .updateChildren(cartMap)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull @NotNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            editor.putString("uniqueID", uuid);
                            editor.commit();
                            sharedPrefManager.saveUUID(context, uuid);
                            cartReference.child("Admin").child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                    .child(uuid).child("Food").child(model.getName())
                                    .updateChildren(cartMap)
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull @NotNull Task<Void> task) {
                                            editor.putString("uniqueID", uuid);
                                            editor.commit();
                                            sharedPrefManager.saveUUID(context, uuid);
                                            if (task.isSuccessful()) {
                                                Toast.makeText(context, "Added", Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    });
                        }
                    }
                });

        cartReference.child("User").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child(uuid)
                .updateChildren(amountMap)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull @NotNull Task<Void> task) {
                        if (task.isSuccessful()) {
                        }
                    }
                });
    }

    private void updatingCart(Food model) {

        final DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference()
                .child("Cart")
                .child("User")
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child(uuid)
                .child("Food").child(model.getName());

        final DatabaseReference databaseReference1 = FirebaseDatabase.getInstance().getReference()
                .child("Cart")
                .child("Admin")
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child(uuid)
                .child("Food").child(model.getName());

        databaseReference1.child("quantity").setValue(model.getQuantity());

        databaseReference.child("quantity").setValue(model.getQuantity());

    }

    private void removeFromCart(Food model) {
        final DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("Cart")
                .child("User")
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child(uuid)
                .child("Food").child(model.getName());

        final DatabaseReference databaseReference1 = FirebaseDatabase.getInstance().getReference().child("Cart")
                .child("Admin")
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child(uuid)
                .child("Food").child(model.getName());

        databaseReference.removeValue();

        databaseReference1.removeValue();
    }

    private void subtractingFromCart(Food model) {
        final DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("Cart")
                .child("User")
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child(uuid)
                .child("Food").child(model.getName());

        databaseReference.child("quantity").setValue(model.getQuantity());

        final DatabaseReference databaseReference1 = FirebaseDatabase.getInstance().getReference().child("Cart")
                .child("Admin")
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child(uuid)
                .child("Food").child(model.getName());

        databaseReference1.child("quantity").setValue(model.getQuantity());
    }

    private Uri checkCategory(String category) {
        if (category.equalsIgnoreCase("veg")) {
            return Uri.parse("android.resource://" + context.getPackageName() + "/" + R.drawable.ic_veg);
        } else if (category.equalsIgnoreCase("non veg")) {
            return Uri.parse("android.resource://" + context.getPackageName() + "/" + R.drawable.ic_non_veg);
        }
        return null;
    }

    private Uri checkType(String type) {
        if (type.equalsIgnoreCase("pizza")) {
            return Uri.parse("android.resource://" + context.getPackageName() + "/" + R.drawable.pizza);
        } else if (type.equalsIgnoreCase("burger")) {
            return Uri.parse("android.resource://" + context.getPackageName() + "/" + R.drawable.burger);
        } else if (type.equalsIgnoreCase("barbecue")) {
            return Uri.parse("android.resource://" + context.getPackageName() + "/" + R.drawable.barbecue);
        } else if (type.equalsIgnoreCase("bread")) {
            return Uri.parse("android.resource://" + context.getPackageName() + "/" + R.drawable.bread);
        } else if (type.equalsIgnoreCase("breakfast")) {
            return Uri.parse("android.resource://" + context.getPackageName() + "/" + R.drawable.breakfast);
        } else if (type.equalsIgnoreCase("cake")) {
            return Uri.parse("android.resource://" + context.getPackageName() + "/" + R.drawable.cake);
        } else if (type.equalsIgnoreCase("cheese")) {
            return Uri.parse("android.resource://" + context.getPackageName() + "/" + R.drawable.cheese);
        } else if (type.equalsIgnoreCase("chicken")) {
            return Uri.parse("android.resource://" + context.getPackageName() + "/" + R.drawable.chicken);
        } else if (type.equalsIgnoreCase("bbq")) {
            return Uri.parse("android.resource://" + context.getPackageName() + "/" + R.drawable.barbecue);
        } else if (type.equalsIgnoreCase("cream")) {
            return Uri.parse("android.resource://" + context.getPackageName() + "/" + R.drawable.cream);
        } else if (type.equalsIgnoreCase("dog")) {
            return Uri.parse("android.resource://" + context.getPackageName() + "/" + R.drawable.dog);
        } else if (type.equalsIgnoreCase("donut")) {
            return Uri.parse("android.resource://" + context.getPackageName() + "/" + R.drawable.donut);
        } else if (type.equalsIgnoreCase("dumpling")) {
            return Uri.parse("android.resource://" + context.getPackageName() + "/" + R.drawable.dumpling);
        } else if (type.equalsIgnoreCase("egg")) {
            return Uri.parse("android.resource://" + context.getPackageName() + "/" + R.drawable.egg);
        } else if (type.equalsIgnoreCase("fries")) {
            return Uri.parse("android.resource://" + context.getPackageName() + "/" + R.drawable.fries);
        } else if (type.equalsIgnoreCase("jam")) {
            return Uri.parse("android.resource://" + context.getPackageName() + "/" + R.drawable.jam);
        } else if (type.equalsIgnoreCase("kebab")) {
            return Uri.parse("android.resource://" + context.getPackageName() + "/" + R.drawable.kebab);
        } else if (type.equalsIgnoreCase("onigiri")) {
            return Uri.parse("android.resource://" + context.getPackageName() + "/" + R.drawable.onigiri);
        } else if (type.equalsIgnoreCase("pancake")) {
            return Uri.parse("android.resource://" + context.getPackageName() + "/" + R.drawable.pancake);
        } else if (type.equalsIgnoreCase("popsicle")) {
            return Uri.parse("android.resource://" + context.getPackageName() + "/" + R.drawable.popsicle);
        } else if (type.equalsIgnoreCase("ramen")) {
            return Uri.parse("android.resource://" + context.getPackageName() + "/" + R.drawable.ramen);
        } else if (type.equalsIgnoreCase("salad")) {
            return Uri.parse("android.resource://" + context.getPackageName() + "/" + R.drawable.salad);
        } else if (type.equalsIgnoreCase("shrimp")) {
            return Uri.parse("android.resource://" + context.getPackageName() + "/" + R.drawable.shrimp);
        } else if (type.equalsIgnoreCase("soup")) {
            return Uri.parse("android.resource://" + context.getPackageName() + "/" + R.drawable.soup);
        } else if (type.equalsIgnoreCase("steak")) {
            return Uri.parse("android.resource://" + context.getPackageName() + "/" + R.drawable.steak);
        } else if (type.equalsIgnoreCase("sushi")) {
            return Uri.parse("android.resource://" + context.getPackageName() + "/" + R.drawable.sushi);
        } else if (type.equalsIgnoreCase("sushis")) {
            return Uri.parse("android.resource://" + context.getPackageName() + "/" + R.drawable.sushis);
        } else if (type.equalsIgnoreCase("tempura")) {
            return Uri.parse("android.resource://" + context.getPackageName() + "/" + R.drawable.tempura);
        } else if (type.equalsIgnoreCase("barbeque")) {
            return Uri.parse("android.resource://" + context.getPackageName() + "/" + R.drawable.bbq);
        } else if (type.equalsIgnoreCase("burrito")) {
            return Uri.parse("android.resource://" + context.getPackageName() + "/" + R.drawable.burrito);
        } else if (type.equalsIgnoreCase("piece of cake")) {
            return Uri.parse("android.resource://" + context.getPackageName() + "/" + R.drawable.pieceofcake);
        } else if (type.equalsIgnoreCase("drinks")) {
            return Uri.parse("android.resource://" + context.getPackageName() + "/" + R.drawable.coffeecup);
        } else if (type.equalsIgnoreCase("burger1")) {
            return Uri.parse("android.resource://" + context.getPackageName() + "/" + R.drawable.burger1);
        } else if (type.equalsIgnoreCase("donut")) {
            return Uri.parse("android.resource://" + context.getPackageName() + "/" + R.drawable.donut);
        } else if (type.equalsIgnoreCase("fast food")) {
            return Uri.parse("android.resource://" + context.getPackageName() + "/" + R.drawable.fastfood);
        } else if (type.equalsIgnoreCase("french fries")) {
            return Uri.parse("android.resource://" + context.getPackageName() + "/" + R.drawable.frenchfries);
        } else if (type.equalsIgnoreCase("fish and chips")) {
            return Uri.parse("android.resource://" + context.getPackageName() + "/" + R.drawable.fishandchips);
        } else if (type.equalsIgnoreCase("fried chicken")) {
            return Uri.parse("android.resource://" + context.getPackageName() + "/" + R.drawable.friedchicken);
        } else if (type.equalsIgnoreCase("sandwich")) {
            return Uri.parse("android.resource://" + context.getPackageName() + "/" + R.drawable.sandwich1);
        } else if (type.equalsIgnoreCase("hot dog")) {
            return Uri.parse("android.resource://" + context.getPackageName() + "/" + R.drawable.hotdog);
        } else if (type.equalsIgnoreCase("ice cream")) {
            return Uri.parse("android.resource://" + context.getPackageName() + "/" + R.drawable.icecream);
        } else if (type.equalsIgnoreCase("chocolate")) {
            return Uri.parse("android.resource://" + context.getPackageName() + "/" + R.drawable.chocolate);
        } else if (type.equalsIgnoreCase("kebab")) {
            return Uri.parse("android.resource://" + context.getPackageName() + "/" + R.drawable.kebab);
        } else if (type.equalsIgnoreCase("ketchup")) {
            return Uri.parse("android.resource://" + context.getPackageName() + "/" + R.drawable.ketchup);
        } else if (type.equalsIgnoreCase("muffin")) {
            return Uri.parse("android.resource://" + context.getPackageName() + "/" + R.drawable.muffin);
        } else if (type.equalsIgnoreCase("noodles")) {
            return Uri.parse("android.resource://" + context.getPackageName() + "/" + R.drawable.noodles);
        } else if (type.equalsIgnoreCase("nuggets")) {
            return Uri.parse("android.resource://" + context.getPackageName() + "/" + R.drawable.nuggets);
        } else if (type.equalsIgnoreCase("onion rings")) {
            return Uri.parse("android.resource://" + context.getPackageName() + "/" + R.drawable.onionrings);
        } else if (type.equalsIgnoreCase("pancakes")) {
            return Uri.parse("android.resource://" + context.getPackageName() + "/" + R.drawable.pancakes);
        } else if (type.equalsIgnoreCase("pizza")) {
            return Uri.parse("android.resource://" + context.getPackageName() + "/" + R.drawable.pizza1);
        } else if (type.equalsIgnoreCase("sausage")) {
            return Uri.parse("android.resource://" + context.getPackageName() + "/" + R.drawable.sausage);
        } else if (type.equalsIgnoreCase("soft drink")) {
            return Uri.parse("android.resource://" + context.getPackageName() + "/" + R.drawable.softdrink);
        } else if (type.equalsIgnoreCase("icecream 1")) {
            return Uri.parse("android.resource://" + context.getPackageName() + "/" + R.drawable.icecream1);
        } else if (type.equalsIgnoreCase("steak")) {
            return Uri.parse("android.resource://" + context.getPackageName() + "/" + R.drawable.steak);
        } else if (type.equalsIgnoreCase("waffle")) {
            return Uri.parse("android.resource://" + context.getPackageName() + "/" + R.drawable.waffle);
        }
        return null;
    }

    @NonNull
    @Override
    public foodViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_food, parent, false);
        return new foodViewHolder(view);
    }

    public static class foodViewHolder extends RecyclerView.ViewHolder {
        TextView foodName, foodPrice;
        ImageView foodImage, foodCategory;
        ImageView addToCart, minusFromCart;
        TextView quantity;

        public foodViewHolder(@NonNull View itemView) {
            super(itemView);
            foodName = itemView.findViewById(R.id.text_food_name);
            foodPrice = itemView.findViewById(R.id.text_food_price);
            foodImage = itemView.findViewById(R.id.image_food);
            foodCategory = itemView.findViewById(R.id.image_category);
            addToCart = itemView.findViewById(R.id.image_add);
            minusFromCart = itemView.findViewById(R.id.image_sub);
            quantity = itemView.findViewById(R.id.text_quantity);
        }
    }
}
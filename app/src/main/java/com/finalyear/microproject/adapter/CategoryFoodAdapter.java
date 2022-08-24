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
import com.finalyear.microproject.model.CategoryFoodModel;

import java.text.MessageFormat;
import java.util.ArrayList;

public class CategoryFoodAdapter extends RecyclerView.Adapter<CategoryFoodAdapter.ViewHolder> {

    private ArrayList<CategoryFoodModel> items;
    private Context context;


    public CategoryFoodAdapter(ArrayList<CategoryFoodModel> items, Context context) {
        this.items = items;
        this.context = context;
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryFoodAdapter.ViewHolder holder, int position) {
        // setting data to our text views from our modal class.
        CategoryFoodModel foodItems = items.get(position);

        holder.foodName.setText(foodItems.getName());

        Uri uri_type = checkType(foodItems.getType());
        holder.foodTypeImage.setImageURI(uri_type);

        holder.foodPrice.setText(MessageFormat.format("₹{0}", foodItems.getPrice()));
    }

    private Uri checkType(String type){
        if(type.equalsIgnoreCase("pizza")){
            return Uri.parse("android.resource://" + context.getPackageName() + "/" + R.drawable.pizza);
        }
        else if(type.equalsIgnoreCase("burger")){
            return Uri.parse("android.resource://" + context.getPackageName() + "/" + R.drawable.burger);
        }
        else if(type.equalsIgnoreCase("barbecue")){
            return Uri.parse("android.resource://" + context.getPackageName() + "/" + R.drawable.barbecue);
        }
        else if(type.equalsIgnoreCase("bread")){
            return Uri.parse("android.resource://" + context.getPackageName() + "/" + R.drawable.bread);
        }
        else if(type.equalsIgnoreCase("breakfast")){
            return Uri.parse("android.resource://" + context.getPackageName() + "/" + R.drawable.breakfast);
        }
        else if(type.equalsIgnoreCase("cake")){
            return Uri.parse("android.resource://" + context.getPackageName() + "/" + R.drawable.cake);
        }
        else if(type.equalsIgnoreCase("cheese")){
            return Uri.parse("android.resource://" + context.getPackageName() + "/" + R.drawable.cheese);
        }
        else if(type.equalsIgnoreCase("chicken")){
            return Uri.parse("android.resource://" + context.getPackageName() + "/" + R.drawable.chicken);
        }
        else if(type.equalsIgnoreCase("bbq")){
            return Uri.parse("android.resource://" + context.getPackageName() + "/" + R.drawable.barbecue);
        }
        else if(type.equalsIgnoreCase("cream")){
            return Uri.parse("android.resource://" + context.getPackageName() + "/" + R.drawable.cream);
        }
        else if(type.equalsIgnoreCase("dog")){
            return Uri.parse("android.resource://" + context.getPackageName() + "/" + R.drawable.dog);
        }
        else if(type.equalsIgnoreCase("donut")){
            return Uri.parse("android.resource://" + context.getPackageName() + "/" + R.drawable.donut);
        }
        else if(type.equalsIgnoreCase("dumpling")){
            return Uri.parse("android.resource://" + context.getPackageName() + "/" + R.drawable.dumpling);
        }
        else if(type.equalsIgnoreCase("egg")){
            return Uri.parse("android.resource://" + context.getPackageName() + "/" + R.drawable.egg);
        }
        else if(type.equalsIgnoreCase("fries")){
            return Uri.parse("android.resource://" + context.getPackageName() + "/" + R.drawable.fries);
        }
        else if(type.equalsIgnoreCase("jam")){
            return Uri.parse("android.resource://" + context.getPackageName() + "/" + R.drawable.jam);
        }
        else if(type.equalsIgnoreCase("kebab")){
            return Uri.parse("android.resource://" + context.getPackageName() + "/" + R.drawable.kebab);
        }
        else if(type.equalsIgnoreCase("onigiri")){
            return Uri.parse("android.resource://" + context.getPackageName() + "/" + R.drawable.onigiri);
        }
        else if(type.equalsIgnoreCase("pancake")){
            return Uri.parse("android.resource://" + context.getPackageName() + "/" + R.drawable.pancake);
        }
        else if(type.equalsIgnoreCase("popsicle")){
            return Uri.parse("android.resource://" + context.getPackageName() + "/" + R.drawable.popsicle);
        }
        else if(type.equalsIgnoreCase("ramen")){
            return Uri.parse("android.resource://" + context.getPackageName() + "/" + R.drawable.ramen);
        }
        else if(type.equalsIgnoreCase("salad")){
            return Uri.parse("android.resource://" + context.getPackageName() + "/" + R.drawable.salad);
        }
        else if(type.equalsIgnoreCase("shrimp")){
            return Uri.parse("android.resource://" + context.getPackageName() + "/" + R.drawable.shrimp);
        }
        else if(type.equalsIgnoreCase("soup")){
            return Uri.parse("android.resource://" + context.getPackageName() + "/" + R.drawable.soup);
        }
        else if(type.equalsIgnoreCase("steak")){
            return Uri.parse("android.resource://" + context.getPackageName() + "/" + R.drawable.steak);
        }
        else if(type.equalsIgnoreCase("sushi")){
            return Uri.parse("android.resource://" + context.getPackageName() + "/" + R.drawable.sushi);
        }
        else if(type.equalsIgnoreCase("sushis")){
            return Uri.parse("android.resource://" + context.getPackageName() + "/" + R.drawable.sushis);
        }
        else if(type.equalsIgnoreCase("tempura")){
            return Uri.parse("android.resource://" + context.getPackageName() + "/" + R.drawable.tempura);
        }
        else if(type.equalsIgnoreCase("barbeque")){
            return Uri.parse("android.resource://" + context.getPackageName() + "/" + R.drawable.bbq);
        }
        else if(type.equalsIgnoreCase("burrito")){
            return Uri.parse("android.resource://" + context.getPackageName() + "/" + R.drawable.burrito);
        }
        else if(type.equalsIgnoreCase("piece of cake")){
            return Uri.parse("android.resource://" + context.getPackageName() + "/" + R.drawable.pieceofcake);
        }
        else if(type.equalsIgnoreCase("drinks")){
            return Uri.parse("android.resource://" + context.getPackageName() + "/" + R.drawable.coffeecup);
        }
        else if(type.equalsIgnoreCase("burger1")){
            return Uri.parse("android.resource://" + context.getPackageName() + "/" + R.drawable.burger1);
        }
        else if(type.equalsIgnoreCase("donut")){
            return Uri.parse("android.resource://" + context.getPackageName() + "/" + R.drawable.donut);
        }
        else if(type.equalsIgnoreCase("fast food")){
            return Uri.parse("android.resource://" + context.getPackageName() + "/" + R.drawable.fastfood);
        }
        else if(type.equalsIgnoreCase("french fries")){
            return Uri.parse("android.resource://" + context.getPackageName() + "/" + R.drawable.frenchfries);
        }
        else if(type.equalsIgnoreCase("fish and chips")){
            return Uri.parse("android.resource://" + context.getPackageName() + "/" + R.drawable.fishandchips);
        }
        else if(type.equalsIgnoreCase("fried chicken")){
            return Uri.parse("android.resource://" + context.getPackageName() + "/" + R.drawable.friedchicken);
        }
        else if(type.equalsIgnoreCase("sandwich")){
            return Uri.parse("android.resource://" + context.getPackageName() + "/" + R.drawable.sandwich1);
        }
        else if(type.equalsIgnoreCase("hot dog")){
            return Uri.parse("android.resource://" + context.getPackageName() + "/" + R.drawable.hotdog);
        }
        else if(type.equalsIgnoreCase("ice cream")){
            return Uri.parse("android.resource://" + context.getPackageName() + "/" + R.drawable.icecream);
        }
        else if(type.equalsIgnoreCase("chocolate")){
            return Uri.parse("android.resource://" + context.getPackageName() + "/" + R.drawable.chocolate);
        }
        else if(type.equalsIgnoreCase("kebab")){
            return Uri.parse("android.resource://" + context.getPackageName() + "/" + R.drawable.kebab);
        }
        else if(type.equalsIgnoreCase("ketchup")){
            return Uri.parse("android.resource://" + context.getPackageName() + "/" + R.drawable.ketchup);
        }
        else if(type.equalsIgnoreCase("muffin")){
            return Uri.parse("android.resource://" + context.getPackageName() + "/" + R.drawable.muffin);
        }
        else if(type.equalsIgnoreCase("noodles")){
            return Uri.parse("android.resource://" + context.getPackageName() + "/" + R.drawable.noodles);
        }
        else if(type.equalsIgnoreCase("nuggets")){
            return Uri.parse("android.resource://" + context.getPackageName() + "/" + R.drawable.nuggets);
        }
        else if(type.equalsIgnoreCase("onion rings")){
            return Uri.parse("android.resource://" + context.getPackageName() + "/" + R.drawable.onionrings);
        }
        else if(type.equalsIgnoreCase("pancakes")){
            return Uri.parse("android.resource://" + context.getPackageName() + "/" + R.drawable.pancakes);
        }
        else if(type.equalsIgnoreCase("pizza")){
            return Uri.parse("android.resource://" + context.getPackageName() + "/" + R.drawable.pizza1);
        }
        else if(type.equalsIgnoreCase("sausage")){
            return Uri.parse("android.resource://" + context.getPackageName() + "/" + R.drawable.sausage);
        }
        else if(type.equalsIgnoreCase("soft drink")){
            return Uri.parse("android.resource://" + context.getPackageName() + "/" + R.drawable.softdrink);
        }
        else if(type.equalsIgnoreCase("icecream 1")){
            return Uri.parse("android.resource://" + context.getPackageName() + "/" + R.drawable.icecream1);
        }
        else if(type.equalsIgnoreCase("steak")){
            return Uri.parse("android.resource://" + context.getPackageName() + "/" + R.drawable.steak);
        }
        else if(type.equalsIgnoreCase("waffle")){
            return Uri.parse("android.resource://" + context.getPackageName() + "/" + R.drawable.waffle);
        }
        return null;
    }

    @NonNull
    @Override
    public CategoryFoodAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.category_food_item_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public int getItemCount() {
        // returning the size of our array list.
        return items.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        // creating variables for our text views.
        TextView foodName, foodPrice;
        ImageView foodTypeImage;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            // initializing our text views.
            foodName = itemView.findViewById(R.id.name);
            foodTypeImage = itemView.findViewById(R.id.image);
            foodPrice = itemView.findViewById(R.id.details);
        }
    }
    public void filterList(ArrayList<CategoryFoodModel> filterllist) {
        items = filterllist;
        notifyDataSetChanged();
    }
}
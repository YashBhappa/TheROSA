package com.finalyear.microproject.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.finalyear.microproject.R;
import com.finalyear.microproject.interfaces.UpdateRecyclerView;
import com.finalyear.microproject.model.CategoryModel;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder> {

    private final ArrayList<CategoryModel> items;
    int row_index = -1;
    UpdateRecyclerView updateRecyclerView;
    Activity activity;
    boolean check = true;
    boolean select = true;
    String selectedItem;

    public CategoryAdapter(ArrayList<CategoryModel> items, Activity activity, UpdateRecyclerView updateRecyclerView) {
        this.items = items;
        this.activity = activity;
        this.updateRecyclerView = updateRecyclerView;
    }

    @NonNull
    @NotNull
    @Override
    public CategoryViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.category_item,parent,false);
        return new CategoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull CategoryViewHolder holder, int position) {
        CategoryModel currentItem = items.get(position);
        holder.imageView.setImageResource(currentItem.getImage());
        holder.textView.setText(currentItem.getText());

        if(check){
            check=false;
        }

        holder.linearLayout.setOnClickListener(v -> {
            row_index = position;
            notifyDataSetChanged();

            {
                if (position == 0) {
                    selectedItem = "";
                } else {
                    selectedItem = (currentItem.getText());
                }
                updateRecyclerView.callback(position, selectedItem);
            }
        });

        if(select){
            if(position == 0){
                holder.linearLayout.setBackgroundResource(R.drawable.category_selected);
            select =false;
            }
        }
        else {
            if (row_index == position) {
                holder.linearLayout.setBackgroundResource(R.drawable.category_selected);  //colored bg
            } else {
                holder.linearLayout.setBackgroundResource(R.drawable.category_bg); //white bg
            }
        }
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public static class CategoryViewHolder extends RecyclerView.ViewHolder{

        TextView textView;
        ImageView imageView;
        LinearLayout linearLayout;

        public CategoryViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);

            textView=itemView.findViewById(R.id.text);
            imageView=itemView.findViewById(R.id.image);
            linearLayout = itemView.findViewById(R.id.linear_layout);
        }
    }
}

package com.finalyear.microproject.extras;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.ImageView;

import com.finalyear.microproject.adapter.CategoryAdapter;
import com.finalyear.microproject.adapter.CategoryFoodAdapter;
import com.finalyear.microproject.model.CategoryFoodModel;
import com.finalyear.microproject.R;
import com.finalyear.microproject.model.CategoryModel;

import java.util.ArrayList;
import java.util.List;

public class DashBoardActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private CategoryAdapter categoryAdapter;

    List<CategoryFoodModel> items = new ArrayList();
    CategoryFoodAdapter categoryFoodAdapter;
    ImageView cart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dash_board);

        ArrayList<CategoryModel> item =new ArrayList<>();
        item.add(new CategoryModel(R.drawable.pizza,"Pizza"));
        item.add(new CategoryModel(R.drawable.sand,"Sandwich"));
        item.add(new CategoryModel(R.drawable.fries,"Fries"));
        item.add(new CategoryModel(R.drawable.burger,"Burger"));
        item.add(new CategoryModel(R.drawable.cream,"Ice Cream"));
        item.add(new CategoryModel(R.drawable.ramen,"Ramen"));
        item.add(new CategoryModel(R.drawable.chicken,"Chicken"));
        item.add(new CategoryModel(R.drawable.egg,"Egg"));
        item.add(new CategoryModel(R.drawable.cake,"Cake"));
        item.add(new CategoryModel(R.drawable.breakfast,"Breakfast"));
        item.add(new CategoryModel(R.drawable.dumpling,"Dumpling"));
        item.add(new CategoryModel(R.drawable.donut,"Donut"));
        item.add(new CategoryModel(R.drawable.salad,"Salad"));
        item.add(new CategoryModel(R.drawable.soup,"Soup"));
        item.add(new CategoryModel(R.drawable.shrimp,"Shrimp"));
        item.add(new CategoryModel(R.drawable.sushis,"Sushi"));
        item.add(new CategoryModel(R.drawable.onigiri,"Onigiri"));
        item.add(new CategoryModel(R.drawable.kebab,"Kebab"));
        item.add(new CategoryModel(R.drawable.jam,"Jam"));
        item.add(new CategoryModel(R.drawable.pancake,"Pancake"));
        item.add(new CategoryModel(R.drawable.sausage,"Sausage"));


        recyclerView = findViewById(R.id.rv_1);
      //  categoryAdapter = new CategoryAdapter(item);
        recyclerView.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false));
        recyclerView.setAdapter(categoryAdapter);


       // items.add(new CategoryFoodModel("Burger"));
       // items.add(new CategoryFoodModel("Burger"));
       // items.add(new CategoryFoodModel("Burger"));
       // items.add(new CategoryFoodModel("Burger"));
       // items.add(new CategoryFoodModel("Burger"));
       // items.add(new CategoryFoodModel("Burger"));
       // items.add(new CategoryFoodModel("Burger"));
       // items.add(new CategoryFoodModel("Burger"));
       // items.add(new CategoryFoodModel("Burger"));
       // items.add(new CategoryFoodModel("Burger"));


        RecyclerView drv = findViewById(R.id.rv_2);
        drv.setLayoutManager(new LinearLayoutManager(this));
     //   categoryFoodAdapter = new CategoryFoodAdapter(drv,this,items);
        drv.setAdapter(categoryFoodAdapter);

       // categoryFoodAdapter.setLoadMore(new LoadMore() {
       //     @Override
       //     public void onLoadMore() {
       //         if (items.size() <= 10) {
       //             item.add(null);
       //             categoryFoodAdapter.notifyItemInserted(items.size() - 1);
       //             new Handler().postDelayed(new Runnable() {
       //                 @Override
       //                 public void run() {
       //                     items.remove(items.size() - 1);
       //                     categoryFoodAdapter.notifyItemRemoved(items.size());

       //                     int index = items.size();
       //                     int end = index + 10;
       //                     for (int i = index; i<end; i++)
       //                     {
       //                       String name = UUID.randomUUID().toString();
       //                       CategoryFoodModel item = new CategoryFoodModel(name);
       //                       items.add(item);
       //                     }
       //                     categoryFoodAdapter.notifyDataSetChanged();
       //                     categoryFoodAdapter.setLoaded();

       //                 }
       //             }, 4000);

       //         }
       //         else
       //             Toast.makeText(DashBoardActivity.this,"Data Completed",Toast.LENGTH_LONG).show();
       //     }

        }

    }
package com.finalyear.microproject.fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.facebook.shimmer.ShimmerFrameLayout;
import com.finalyear.microproject.R;
import com.finalyear.microproject.adapter.FoodAdapter;
import com.finalyear.microproject.adapter.MainSliderAdapter;
import com.finalyear.microproject.adapter.RestaurantAdapter;
import com.finalyear.microproject.misc.PicassoImageLoadingService;
import com.finalyear.microproject.model.Food;
import com.finalyear.microproject.model.Restaurant;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.switchmaterial.SwitchMaterial;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import java.util.ArrayList;

import ss.com.bannerslider.Slider;

import static android.content.ContentValues.TAG;

public class RestaurantFragment extends Fragment {

    RecyclerView foodView;
    FoodAdapter foodAdapter;
    CollapsingToolbarLayout title;
    private DatabaseReference foodReference;
    ShimmerFrameLayout shimmerFrameLayout;
    boolean join = false;
    ArrayList<Food> items = new ArrayList<>();

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_restaurant, container, false);

        shimmerFrameLayout = view.findViewById(R.id.layout_states);
        shimmerFrameLayout.setVisibility(View.VISIBLE);
        shimmerFrameLayout.startShimmer();

        Bundle bundle = getArguments();
        assert bundle != null;
        String name = bundle.getString("name");
        Log.d(TAG, "onCreateView: "+name);
        title = (CollapsingToolbarLayout) view.findViewById(R.id.toolbar_layout);
        title.setTitle(name);

        foodReference = FirebaseDatabase.getInstance().getReference().child("Food").child(name);

        Slider.init(new PicassoImageLoadingService(getActivity()));
        Slider slider = view.findViewById(R.id.cover_slider);
        slider.setAdapter(new MainSliderAdapter());

        foodView = view.findViewById(R.id.recycler_food_items);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        foodView.setLayoutManager(layoutManager);

        FirebaseRecyclerOptions<Food> options =
                new FirebaseRecyclerOptions.Builder<Food>()
                        .setQuery(foodReference, Food.class)
                        .build();
        foodAdapter = new FoodAdapter(options, getActivity(), name);
        foodView.setAdapter(foodAdapter);
        foodView.setVisibility(View.INVISIBLE);

        foodAdapter = new FoodAdapter(options, getActivity(), name);
        foodView.setAdapter(foodAdapter);

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                shimmerFrameLayout.stopShimmer();
                shimmerFrameLayout.setVisibility(View.GONE);
                foodView.setVisibility(View.VISIBLE);
            }
        }, 2200);

        SwitchMaterial vegSwitch = view.findViewById(R.id.switch_veg);
        vegSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked) {
                    DatabaseReference mbase = FirebaseDatabase.getInstance().getReference("Food").child(name);
                    Query query = mbase.orderByChild("category").equalTo("Veg");

                    FirebaseRecyclerOptions<Food> foodOptions = new FirebaseRecyclerOptions.Builder<Food>()
                            .setQuery(query, Food.class)
                            .build();

                    foodAdapter.notifyDataSetChanged();
                    foodAdapter = new FoodAdapter(foodOptions, getActivity(), name);
                    foodView.setAdapter(foodAdapter);
                    foodAdapter.startListening();
                }
                else {
                    foodReference = FirebaseDatabase.getInstance().getReference().child("Food").child(name);

                    FirebaseRecyclerOptions<Food> allOptions =
                            new FirebaseRecyclerOptions.Builder<Food>()
                                    .setQuery(foodReference, Food.class)
                                    .build();

                    foodAdapter.notifyDataSetChanged();
                    foodAdapter = new FoodAdapter(allOptions, getActivity(), name);
                    foodView.setAdapter(foodAdapter);
                    foodAdapter.startListening();
                }
            }
        });

        SearchView searchMenu = view.findViewById(R.id.text_search_menu);
        searchMenu.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if(!newText.isEmpty()){
                    DatabaseReference foodReference = FirebaseDatabase.getInstance().getReference("Food").child(name);
                    Query query = foodReference.orderByChild("name").equalTo(newText);

                    FirebaseRecyclerOptions<Food> foodOptions = new FirebaseRecyclerOptions.Builder<Food>()
                            .setQuery(query, Food.class)
                            .build();

                    foodAdapter.notifyDataSetChanged();
                    foodAdapter = new FoodAdapter(foodOptions, getActivity(), name);
                    foodView.setAdapter(foodAdapter);
                    foodAdapter.startListening();
                }
                else{
                    foodReference = FirebaseDatabase.getInstance().getReference().child("Food").child(name);
                    FirebaseRecyclerOptions<Food> options =
                            new FirebaseRecyclerOptions.Builder<Food>()
                                    .setQuery(foodReference, Food.class)
                                    .build();

                    foodAdapter.notifyDataSetChanged();
                    foodAdapter = new FoodAdapter(options, getActivity(), name);
                    foodView.setAdapter(foodAdapter);
                    foodAdapter.startListening();
                }
                return false;
            }
        });

        ImageView close = view.findViewById(R.id.image_close);
        close.setOnClickListener(v -> {
            FragmentTransaction fragmentTransaction = requireActivity().getSupportFragmentManager().beginTransaction();
            fragmentTransaction.setCustomAnimations(
                    R.anim.fade_in,  // enter
                    R.anim.slide_out,  // exit
                    R.anim.slide_in,   // popEnter
                    R.anim.fade_out);  // popExit
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.replace(R.id.container, new NearbyResFragment());
            fragmentTransaction.commit();
        });
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        foodAdapter.startListening();
    }
}



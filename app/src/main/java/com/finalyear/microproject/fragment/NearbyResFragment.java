package com.finalyear.microproject.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.SearchView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.facebook.shimmer.ShimmerFrameLayout;
import com.finalyear.microproject.R;
import com.finalyear.microproject.adapter.FoodAdapter;
import com.finalyear.microproject.adapter.RestaurantAdapter;
import com.finalyear.microproject.model.Food;
import com.finalyear.microproject.model.Restaurant;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

public class NearbyResFragment extends Fragment {

    ShimmerFrameLayout shimmerFrameLayout;
    //private List<Restaurant> restaurantList = new ArrayList<>();
    RecyclerView resView;
   // private ShopAdapter sAdapter;
    RestaurantAdapter restaurantAdapter;
    DatabaseReference RestaurantReference;
    SearchView searchRestaurant;

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_nearby_restaurant, container, false);

        shimmerFrameLayout = view.findViewById(R.id.layout_states);
        shimmerFrameLayout.setVisibility(View.VISIBLE);
        shimmerFrameLayout.startShimmer();

        RestaurantReference = FirebaseDatabase.getInstance().getReference().child("Restaurants");

        resView = view.findViewById(R.id.recycler_shops);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        resView.setLayoutManager(layoutManager);

        FirebaseRecyclerOptions<Restaurant> options
                = new FirebaseRecyclerOptions.Builder<Restaurant>()
                .setQuery(RestaurantReference, Restaurant.class)
                .build();
        // Connecting object of required Adapter class to
        // the Adapter class itself
        restaurantAdapter = new RestaurantAdapter(options, getActivity());
        // Connecting Adapter class with the Recycler view*/
        resView.setAdapter(restaurantAdapter);
        resView.setVisibility(View.INVISIBLE);

        //sAdapter = new ShopAdapter(restaurantList, getActivity(), this);
        //RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        //shops.setLayoutManager(layoutManager);
        //shops.setItemAnimator(new DefaultItemAnimator());
        //shops.setAdapter(sAdapter);

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                shimmerFrameLayout.stopShimmer();
                shimmerFrameLayout.setVisibility(View.GONE);
                resView.setVisibility(View.VISIBLE);
            }
        }, 2200);
        //prepareShopList();

        searchRestaurant = view.findViewById(R.id.search_restaurants);
        searchRestaurant.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                DatabaseReference restaurantsReference = FirebaseDatabase.getInstance().getReference("Restaurants");
                Query resQuery = restaurantsReference.orderByChild("name").equalTo(query);

                FirebaseRecyclerOptions<Restaurant> restaurantOptions = new FirebaseRecyclerOptions.Builder<Restaurant>()
                        .setQuery(resQuery, Restaurant.class)
                        .build();

                restaurantAdapter.notifyDataSetChanged();
                restaurantAdapter = new RestaurantAdapter(restaurantOptions, getActivity());
                resView.setAdapter(restaurantAdapter);
                restaurantAdapter.startListening();
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if(!newText.isEmpty()){
                DatabaseReference restaurantsReference = FirebaseDatabase.getInstance().getReference("Restaurants");
                Query query = restaurantsReference.orderByChild("name").equalTo(newText.toLowerCase());

                FirebaseRecyclerOptions<Restaurant> restaurantOptions = new FirebaseRecyclerOptions.Builder<Restaurant>()
                        .setQuery(query, Restaurant.class)
                        .build();

                restaurantAdapter.notifyDataSetChanged();
                restaurantAdapter = new RestaurantAdapter(restaurantOptions, getActivity());
                resView.setAdapter(restaurantAdapter);
                restaurantAdapter.startListening();
                return false;
                }
                else{
                    RestaurantReference = FirebaseDatabase.getInstance().getReference().child("Restaurants");

                    FirebaseRecyclerOptions<Restaurant> options
                            = new FirebaseRecyclerOptions.Builder<Restaurant>()
                            .setQuery(RestaurantReference, Restaurant.class)
                            .build();

                    restaurantAdapter.notifyDataSetChanged();
                    restaurantAdapter = new RestaurantAdapter(options, getActivity());
                    resView.setAdapter(restaurantAdapter);
                    restaurantAdapter.startListening();
                    return false;
                }
            }
        });
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        restaurantAdapter.startListening();
    }

    @Override
    public void onStop()
    {
        super.onStop();
        restaurantAdapter.stopListening();
    }

    /*private void prepareShopList() {
        Restaurant shop = new Restaurant("Dominos","Opens at 9AM");
        restaurantList.add(shop);
        shop = new Restaurant("Pizza Hut","Opens at 10AM");
        restaurantList.add(shop);
        shop = new Restaurant("Pizza By the Bay","Opens at 9AM");
        restaurantList.add(shop);
        shop = new Restaurant("Deliure & The Eatrium","Opens at 11AM");
        restaurantList.add(shop);
        shop = new Restaurant("The Oriental Blossom","Opens at 8AM");
        restaurantList.add(shop);
        shop = new Restaurant("KFC","Opens at 10AM");
        restaurantList.add(shop);
        shop = new Restaurant("Bharat Chicken House","Opens at 9AM");
        restaurantList.add(shop);
    } */


}

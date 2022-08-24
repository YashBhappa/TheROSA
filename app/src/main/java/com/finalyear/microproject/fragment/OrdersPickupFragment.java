package com.finalyear.microproject.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.facebook.shimmer.ShimmerFrameLayout;
import com.finalyear.microproject.R;
import com.finalyear.microproject.adapter.OrdersDeliveryAdapter;
import com.finalyear.microproject.adapter.OrdersPickupAdapter;
import com.finalyear.microproject.model.Orders;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Objects;

public class OrdersPickupFragment extends Fragment {
    ShimmerFrameLayout shimmerFrameLayout;

    RecyclerView ordersPickupRv;
    DatabaseReference ordersPickupReference;
    OrdersPickupAdapter ordersPickupAdapter;

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.pickup_order_tab, container, false);

        shimmerFrameLayout = view.findViewById(R.id.layout_states);
        shimmerFrameLayout.setVisibility(View.VISIBLE);
        shimmerFrameLayout.startShimmer();

        {
            ordersPickupRv = view.findViewById(R.id.recycler_pickup_orders);
            RecyclerView.LayoutManager pickupLayout = new LinearLayoutManager(getContext());
            ordersPickupRv.setLayoutManager(pickupLayout);

            ordersPickupReference = FirebaseDatabase.getInstance().getReference().child("Orders")
                    .child("Pickup").child(Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid());
            FirebaseRecyclerOptions<Orders> options_pickup =
                    new FirebaseRecyclerOptions.Builder<Orders>()
                            .setQuery(ordersPickupReference, Orders.class)
                            .build();

            ordersPickupAdapter = new OrdersPickupAdapter(options_pickup, getActivity());
            ordersPickupRv.setAdapter(ordersPickupAdapter);
            ordersPickupRv.setVisibility(View.GONE);
        }

        Handler handler = new Handler();
        handler.postDelayed(() -> {
            shimmerFrameLayout.stopShimmer();
            shimmerFrameLayout.setVisibility(View.GONE);
            ordersPickupRv.setVisibility(View.VISIBLE);
        }, 2200);


      //  trackOrder = view.findViewById(R.id.button_track_rate);
      //  trackOrder.setOnClickListener(v -> {
      //      Bundle bundle = new Bundle();
      //      FragmentTransaction transaction = requireActivity().getSupportFragmentManager().beginTransaction();
      //      OrderDetailsFragment orderDetailsFragment = new OrderDetailsFragment();
      //      orderDetailsFragment.setArguments(bundle);
      //      bundle.putString("type", "Pickup");
      //      transaction.replace(R.id.container, orderDetailsFragment);
      //      transaction.addToBackStack(getActivity().getClass().getName());
      //      transaction.commit();
      //  });

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        ordersPickupAdapter.startListening();
    }

    @Override
    public void onStop()
    {
        super.onStop();
        ordersPickupAdapter.stopListening();
    }
}
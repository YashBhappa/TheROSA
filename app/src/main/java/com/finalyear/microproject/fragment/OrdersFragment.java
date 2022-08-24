package com.finalyear.microproject.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.viewpager.widget.ViewPager;

import com.facebook.shimmer.ShimmerFrameLayout;
import com.finalyear.microproject.R;
import com.finalyear.microproject.activities.LoginActivity;
import com.finalyear.microproject.adapter.LoginAdapter;
import com.finalyear.microproject.adapter.OrderTabAdapter;
import com.finalyear.microproject.adapter.OrdersDeliveryAdapter;
import com.finalyear.microproject.adapter.OrdersPickupAdapter;
import com.finalyear.microproject.model.Orders;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Objects;

public class OrdersFragment extends Fragment {

    TabLayout orderTabLayout;
    ViewPager orderViewPager;

    SwipeRefreshLayout swipeRefreshLayout;

    ImageView close;

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_orders, container, false);

        swipeRefreshLayout = view.findViewById(R.id.swipe_refresh_layout);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipeRefreshLayout.setRefreshing(false);
                refreshItems();
            }
        });

        orderTabLayout = view.findViewById(R.id.tab_order_layout);
        orderViewPager = view.findViewById(R.id.order_view_pager);

        orderTabLayout.addTab(orderTabLayout.newTab().setText("PICKUP(S)"));
        orderTabLayout.addTab(orderTabLayout.newTab().setText("DELIVERY(S)"));
        orderTabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        OrderTabAdapter adapter = new OrderTabAdapter(getChildFragmentManager(), getActivity(), orderTabLayout.getTabCount());
        orderViewPager.setAdapter(adapter);

        orderViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(orderTabLayout));

        orderTabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if(tab.getPosition() == 0){
                    orderViewPager.setCurrentItem(0);
                }
                else{
                    orderViewPager.setCurrentItem(1);
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                Toast.makeText(getActivity(),"Already on this tab", Toast.LENGTH_SHORT).show();
            }
        });

        close = view.findViewById(R.id.image_close);
        close.setOnClickListener(v -> {
            DashBoardFragment dashBoardFragment = new DashBoardFragment();
            FragmentTransaction transaction = requireActivity().getSupportFragmentManager().beginTransaction();
            transaction.setCustomAnimations(
                    R.anim.fade_in,  // enter
                    R.anim.slide_out,  // exit
                    R.anim.slide_in,   // popEnter
                    R.anim.fade_out);  // popExit
            transaction.replace(R.id.container, dashBoardFragment);
            transaction.addToBackStack(null);
            transaction.commit();
        });

        return view;
    }

    private void refreshItems() {
    }

    @Override
    public void onStart() {
        super.onStart();
    }
}
package com.finalyear.microproject.adapter;

import android.content.Context;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.finalyear.microproject.fragment.LoginTabFragment;
import com.finalyear.microproject.fragment.OrdersDeliveryFragment;
import com.finalyear.microproject.fragment.OrdersPickupFragment;
import com.finalyear.microproject.fragment.SignUpTabFragment;

public class OrderTabAdapter extends FragmentPagerAdapter {

    Context context;
    int totalTabs;

    public OrderTabAdapter(FragmentManager fm, Context context, int totalTabs){
        super(fm);
        this.context=context;
        this.totalTabs=totalTabs;
    }

    @Override
    public int getCount() {
        return totalTabs;
    }

    public Fragment getItem(int position){
        switch (position){
            case 0:
                return new OrdersPickupFragment();

            case 1:
                return new OrdersDeliveryFragment();

            default:
                return null;
        }
    }



}

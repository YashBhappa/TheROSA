package com.finalyear.microproject.model;

import android.view.ViewGroup;

import com.finalyear.microproject.adapter.DrawerAdapter;

public abstract class DrawerItem<T extends DrawerAdapter.ViewHolder> {

    protected boolean isChecked;
    public abstract T createViewHolder(ViewGroup parent);

    public abstract void bindViewHolder(T holder);

    public DrawerItem<T>setChecked(boolean isChecked){
        this.isChecked = isChecked;
        return this;
    }

    public boolean isChecked(){
        return true;
    }

    public boolean isSelectable(){
        return true;
    }

}

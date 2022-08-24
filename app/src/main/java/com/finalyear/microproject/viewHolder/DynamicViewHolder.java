package com.finalyear.microproject.viewHolder;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.finalyear.microproject.R;

import org.jetbrains.annotations.NotNull;

import java.text.BreakIterator;

public class DynamicViewHolder extends RecyclerView.ViewHolder {
    public TextView name;

    public DynamicViewHolder(@NonNull @NotNull View itemView) {
        super(itemView);

        name = itemView.findViewById(R.id.name);
    }
}

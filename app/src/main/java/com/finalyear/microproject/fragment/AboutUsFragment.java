package com.finalyear.microproject.fragment;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.finalyear.microproject.R;

import java.util.Objects;

public class AboutUsFragment extends Fragment {

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_contributors, container, false);

        LinearLayout C1 = view.findViewById(R.id.contributor_1);
        LinearLayout C2 = view.findViewById(R.id.contributor_2);
        LinearLayout C3 = view.findViewById(R.id.contributor_3);

        ContributorDetailFragment fragment = new ContributorDetailFragment();
        Bundle bundle = new Bundle();
        FragmentTransaction fragmentTransaction = requireActivity().getSupportFragmentManager().beginTransaction();

        C1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextView T1 = view.findViewById(R.id.contributor1_name);
                String t1 = (String) T1.getText();

                Uri uri = Uri.parse("android.resource://" + requireActivity().getPackageName() + "/" + R.drawable.yash);

                fragment.setArguments(bundle);
                bundle.putString("name1",t1);
                bundle.putString("image", uri.toString());
                bundle.putString("github","YashBhappa");
                bundle.putString("linkedin","yashbhappa");
                bundle.putString("email","ybhappa@gmail.com");
                bundle.putString("website","not yet");
                fragmentTransaction.addToBackStack(this.getClass().getName());
                fragmentTransaction.setCustomAnimations(
                        R.anim.slide_in,  // enter
                        R.anim.fade_out,  // exit
                        R.anim.fade_in,   // popEnter
                        R.anim.slide_out);
                fragmentTransaction.replace(R.id.container,fragment);
                fragmentTransaction.commit();
            }
        });

        C2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextView T1 = view.findViewById(R.id.contributor2_name);
                String t1 = (String) T1.getText();

                Uri uri = Uri.parse("android.resource://" + requireActivity().getPackageName() + "/" + R.drawable.sikpa);

                fragment.setArguments(bundle);
                bundle.putString("name1",t1);
                bundle.putString("github","siddhesh0309");
                bundle.putString("image", uri.toString());
                bundle.putString("linkedin","siddheshparkhe");
                bundle.putString("email","siddheshparkhe123@gmail.com");
                bundle.putString("website","not yet");
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.setCustomAnimations(
                        R.anim.slide_in,  // enter
                        R.anim.fade_out,  // exit
                        R.anim.fade_in,   // popEnter
                        R.anim.slide_out
                );
                fragmentTransaction.replace(R.id.container,fragment);
                fragmentTransaction.commit();
            }
        });

        C3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextView T1 = view.findViewById(R.id.contributor3_name);
                String t1 = (String) T1.getText();

                Uri uri = Uri.parse("android.resource://" + requireActivity().getPackageName() + "/" + R.drawable.bishwa);

                fragment.setArguments(bundle);
                bundle.putString("name1",t1);
                bundle.putString("image", uri.toString());
                bundle.putString("github","BishwajitPvt");
                bundle.putString("linkedin","bishwajitsamanta");
                bundle.putString("email","bishwajitsamanta386@gmail.com");
                bundle.putString("website","not yet");
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.setCustomAnimations(
                        R.anim.slide_in,  // enter
                        R.anim.fade_out,  // exit
                        R.anim.fade_in,   // popEnter
                        R.anim.slide_out
                );
                fragmentTransaction.replace(R.id.container,fragment);
                fragmentTransaction.commit();
            }
        });
        return view;
    }
}

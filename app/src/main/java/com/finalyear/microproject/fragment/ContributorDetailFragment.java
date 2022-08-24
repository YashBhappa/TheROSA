package com.finalyear.microproject.fragment;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.finalyear.microproject.R;

public class ContributorDetailFragment extends Fragment {
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = (ViewGroup) inflater.inflate(R.layout.fragment_contributor_detail, container, false);

        ImageView close= view.findViewById(R.id.image_close);
        TextView Name = view.findViewById(R.id.text_name);
        ImageView image_user = view.findViewById(R.id.image_user);

        TextView github = view.findViewById(R.id.text_github);
        TextView linkedIn = view.findViewById(R.id.text_linkedin);
        TextView Email = view.findViewById(R.id.text_email);
        TextView Website = view.findViewById(R.id.text_website);

        Bundle bundle = getArguments();
        assert bundle != null;

        String name = bundle.getString("name1");
        Uri imageUser = Uri.parse(bundle.getString("image"));
        String gId = bundle.getString("github");
        String lId = bundle.getString("linkedin");
        String email = bundle.getString("email");
        String wId = bundle.getString("website");

        Name.setText(name);

        image_user.setImageURI(imageUser);

        github.setText(gId);
        github.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent =new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.github.com/"+gId));
                startActivity(intent);
            }
        });

        linkedIn.setText(lId);
        linkedIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent =new Intent(Intent.ACTION_VIEW, Uri.parse(""+lId));
                startActivity(intent);
            }
        });

        Email.setText(email);
        Email.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent =new Intent(Intent.ACTION_VIEW, Uri.parse(""+email));
                startActivity(intent);
            }
        });

        Website.setText(wId);
        Website.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent =new Intent(Intent.ACTION_VIEW, Uri.parse(""+wId));
                startActivity(intent);
            }
        });

        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                replace();
            }
        });
        return view;
    }

    public void replace() {
        AboutUsFragment fragment = new AboutUsFragment();
        FragmentManager manager = getActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = manager.beginTransaction();
        fragmentTransaction.setCustomAnimations(
                R.anim.fade_in,  // enter
                R.anim.slide_out,  // exit
                R.anim.fade_out,   // popEnter
                R.anim.slide_in
        );
        manager.popBackStackImmediate();
        fragmentTransaction.commit();
    }
}
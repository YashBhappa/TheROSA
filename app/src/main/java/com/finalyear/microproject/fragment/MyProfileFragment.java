package com.finalyear.microproject.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.finalyear.microproject.R;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class MyProfileFragment extends Fragment{
    TextInputEditText name, email;
    TextView number;

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = (ViewGroup) inflater.inflate(R.layout.fragment_profile, container, false);

        name = view.findViewById(R.id.edit_name);
        email = view.findViewById(R.id.edit_email);
        number = view.findViewById(R.id.text_mobile);

        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser user = firebaseAuth.getCurrentUser();
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference myRef = firebaseDatabase.getReference("UserInfo");

        if(user.getEmail() != null) {
            Query query = myRef.orderByChild("email").equalTo(user.getEmail());
            query.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        String Username = (String) dataSnapshot.child("username").getValue();
                        String Name = (String) dataSnapshot.child("name").getValue();
                        String Email = (String) dataSnapshot.child("email").getValue();
                        String Phone = (String) dataSnapshot.child("phoneNo").getValue();
                        String Password = (String) dataSnapshot.child("password").getValue();
                        //ProfileName.setText(Name);
                        //Username.setText(Username);
                        name.setText(Name);
                        email.setText(Email);
                        number.setText(Phone);
                        //Password.setText(Password);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                }
            });
        }


        TextView order = view.findViewById(R.id.text_your_orders);
        order.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    FragmentTransaction fragmentTransaction = requireActivity().getSupportFragmentManager().beginTransaction();
                    OrdersFragment ordersFragment = new OrdersFragment();
                    fragmentTransaction.addToBackStack(null);
                    fragmentTransaction.setCustomAnimations(
                            R.anim.slide_in,
                            R.anim.fade_out,
                            R.anim.fade_in,
                            R.anim.slide_out
                    );
                    fragmentTransaction.replace(R.id.container, ordersFragment);
                    fragmentTransaction.commit();
            }
        });
        return view;
    }
}

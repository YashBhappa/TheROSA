package com.finalyear.microproject.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.finalyear.microproject.R;

public class PaymentFragment extends Fragment {
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_payment, container, false);


        Button creditButtonPay = view.findViewById(R.id.button_credit_pay);
        Button debitButtonPay = view.findViewById(R.id.button_debit_pay);
        Button bhimButtonPay = view.findViewById(R.id.button_bhim_pay);
        Button paytmButtonPay = view.findViewById(R.id.button_paytm_pay);

        creditButtonPay.setText("Pay");
        debitButtonPay.setText("Pay");
        paytmButtonPay.setText("Pay");
        bhimButtonPay.setText("Pay");

        RadioButton creditCard = view.findViewById(R.id.radio_credit);
        RadioButton debitCard = view.findViewById(R.id.radio_debit);
        RadioButton bhimUPI = view.findViewById(R.id.radio_bhim);
        RadioButton paytm = view.findViewById(R.id.radio_paytm);

        CardView creditView = view.findViewById(R.id.card_credit);
        CardView debitView = view.findViewById(R.id.card_debit_details);
        CardView bhimView = view.findViewById(R.id.card_bhim);
        CardView paytmView = view.findViewById(R.id.card_paytm);

        creditCard.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(creditCard.isChecked()) {
                    creditView.setVisibility(View.VISIBLE);
                    debitCard.setChecked(false);
                    bhimUPI.setChecked(false);
                    paytm.setChecked(false);
                }
                else{
                    creditView.setVisibility(View.GONE);
                }
            }
        });

        debitCard.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(debitCard.isChecked()) {
                    debitView.setVisibility(View.VISIBLE);
                    bhimUPI.setChecked(false);
                    paytm.setChecked(false);
                    creditCard.setChecked(false);
                }else{
                    debitView.setVisibility(View.GONE);
                }
            }
        });

        bhimUPI.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(bhimUPI.isChecked()) {
                    bhimView.setVisibility(View.VISIBLE);
                    paytm.setChecked(false);
                    creditCard.setChecked(false);
                    debitCard.setChecked(false);
                }
                else{
                    bhimView.setVisibility(View.GONE);
                }
            }
        });

        paytm.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(paytm.isChecked()) {
                    paytmView.setVisibility(View.VISIBLE);
                    bhimUPI.setChecked(false);
                    creditCard.setChecked(false);
                    debitCard.setChecked(false);
                }
                else{
                    paytmView.setVisibility(View.GONE);
                }
            }
        });

        ImageView close = view.findViewById(R.id.image_close);
        close.setOnClickListener(v -> {
            FragmentTransaction transaction = requireActivity().getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.container, new CartFragment());
            transaction.setCustomAnimations(
                    R.anim.slide_in,  // enter
                    R.anim.fade_out,  // exit
                    R.anim.fade_in,   // popEnter
                    R.anim.slide_out
            );
            transaction.commit();
        });

        creditButtonPay.setOnClickListener(v -> {

            Toast.makeText(getActivity(), "Placed order..." , Toast.LENGTH_SHORT).show();
            DashBoardFragment fragment = new DashBoardFragment();
            FragmentTransaction transaction = requireActivity().getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.container, fragment);
            transaction.commit();
        });
        debitButtonPay.setOnClickListener(v -> {

            Toast.makeText(getActivity(), "Placed order..." , Toast.LENGTH_SHORT).show();
            DashBoardFragment fragment = new DashBoardFragment();
            FragmentTransaction transaction = requireActivity().getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.container, fragment);
            transaction.commit();
        });
        paytmButtonPay.setOnClickListener(v -> {

            Toast.makeText(getActivity(), "Placed order..." , Toast.LENGTH_SHORT).show();
            DashBoardFragment fragment = new DashBoardFragment();
            FragmentTransaction transaction = requireActivity().getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.container, fragment);
            transaction.commit();
        });
        bhimButtonPay.setOnClickListener(v -> {

            Toast.makeText(getActivity(), "Placed order..." , Toast.LENGTH_SHORT).show();
            DashBoardFragment fragment = new DashBoardFragment();
            FragmentTransaction transaction = requireActivity().getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.container, fragment);
            transaction.commit();
        });

        return view;
    }
}

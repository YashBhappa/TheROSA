package com.finalyear.microproject.fragment;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.finalyear.microproject.R;
import com.finalyear.microproject.adapter.CartAdapter;
import com.finalyear.microproject.misc.SharedPrefManager;
import com.finalyear.microproject.model.Cart;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Objects;

public class CartFragment extends Fragment {

    LinearLayout cartLayout, emptyCartLayout;

    RecyclerView cartRv;
    CartAdapter cartAdapter;

    int cartTotal,totalAmount;
    TextView conveyInfo;
    TextView total;

    SharedPrefManager sharedPrefManager;
    SharedPreferences sharedPreferences;
    String uuid;

    int childCount;

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_cart, container, false);
        sharedPreferences = requireActivity().getSharedPreferences("uuid", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        //uuid  = sharedPreferences.getString("uniqueID", "");

        cartLayout = view.findViewById(R.id.layout_cart_content);
        emptyCartLayout = view.findViewById(R.id.layout_empty);

        sharedPrefManager = new SharedPrefManager(getActivity());
        uuid = sharedPrefManager.getUUID();

        MaterialToolbar toolbar = view.findViewById(R.id.toolbar);
        toolbar.setNavigationOnClickListener(v -> getActivity().onBackPressed());

        MaterialButton next = view.findViewById(R.id.button_confirm_order);

        cartRv = view.findViewById(R.id.recycler_food_items);
        cartRv.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));

        DatabaseReference cartReference = FirebaseDatabase.getInstance().getReference().child("Cart");
        FirebaseRecyclerOptions<Cart> options =
                new FirebaseRecyclerOptions.Builder<Cart>()
                        .setQuery(cartReference.child("User")
                                .child(Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid())
                                .child(uuid)
                                .child("Food"), Cart.class)
                        .build();

        cartAdapter = new CartAdapter(options, getActivity());
        cartRv.setAdapter(cartAdapter);

        total = view.findViewById(R.id.text_total);
        total.setText(String.valueOf(fetchTotal()));

        RadioGroup takeOutMethod = view.findViewById(R.id.takeOutMethod);
        RadioButton pickUp = view.findViewById(R.id.radio_pickup);
        RadioButton delivery = view.findViewById(R.id.radio_delivery);

        TextView delivery_price = view.findViewById(R.id.text_delivery_price);

        conveyInfo = view.findViewById(R.id.text_info);
        conveyInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                enterInfoToConvey();
            }
        });

        takeOutMethod.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                int selectedId = takeOutMethod.getCheckedRadioButtonId();

                if (selectedId == pickUp.getId()) {
                    delivery_price.setText("0");
                    int finalTotal = totalAmount + Integer.parseInt((String) delivery_price.getText());
                    total.setText(String.valueOf(finalTotal));
                    if (Integer.parseInt((String) total.getText()) == 0) {
                        next.setVisibility(View.GONE);
                    } else {
                        next.setVisibility(View.VISIBLE);
                    }
                } else if (selectedId == delivery.getId()) {
                    delivery_price.setText("50");
                    int finalTotal = totalAmount + Integer.parseInt((String) delivery_price.getText());
                    total.setText(String.valueOf(finalTotal));
                    if (Integer.parseInt((String) total.getText()) > 0) {
                        next.setVisibility(View.VISIBLE);
                    }
                }
            }
        });

        next.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (takeOutMethod.getCheckedRadioButtonId() == pickUp.getId()) {
                    LayoutInflater inflater = LayoutInflater.from(getActivity());
                    View subView = inflater.inflate(R.layout.dialog_pickup_personal_details, null);

                    Dialog dialog = new Dialog(getActivity());
                    dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                    Window window = dialog.getWindow();
                    window.setGravity(Gravity.CENTER);
                    window.getAttributes().windowAnimations = R.style.DialogAnimation;
                    dialog.setContentView(subView);
                    //dialog.setCanceledOnTouchOutside(false);
                    dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

                    TextInputEditText Name = subView.findViewById(R.id.edit_pickup_customer_name);
                    TextInputEditText Number = subView.findViewById(R.id.edit_pickup_customer_number);
                    Button makePayment = subView.findViewById(R.id.button_make_payment);

                    makePayment.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            String name = Name.getEditableText().toString().trim();
                            String number = Number.getEditableText().toString().trim();
                            String info = conveyInfo.getText().toString().trim();
                            if (TextUtils.isEmpty(name) || TextUtils.isEmpty(number)) {
                                if (TextUtils.isEmpty(name)) {
                                    Toast.makeText(getActivity(), "Please enter your name", Toast.LENGTH_SHORT).show();
                                    Name.setError("Please enter your Name");
                                } else if (TextUtils.isEmpty(number)) {
                                    Toast.makeText(getActivity(), "Please enter your number", Toast.LENGTH_SHORT).show();
                                    Number.setError("Please enter your number");
                                }
                            } else {
                                addPickup(name, number, info);
                                PaymentFragment paymentFragment = new PaymentFragment();
                                FragmentTransaction fragmentTransaction = requireActivity().getSupportFragmentManager().beginTransaction();
                                fragmentTransaction.replace(R.id.container, paymentFragment);
                                fragmentTransaction.commit();
                                dialog.dismiss();
                            }
                        }
                    });
                    dialog.show();
                }

                if (takeOutMethod.getCheckedRadioButtonId() == delivery.getId()) {
                    LayoutInflater inflater = LayoutInflater.from(getActivity());
                    View subView = inflater.inflate(R.layout.dialog_personal_details, null);

                    Dialog dialog = new Dialog(getActivity());
                    dialog.setContentView(subView);
                    Window window = dialog.getWindow();
                    window.setGravity(Gravity.CENTER);
                    window.getAttributes().windowAnimations = R.style.DialogAnimation;
                    //dialog.setCanceledOnTouchOutside(false);
                    dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

                    TextInputEditText Name = subView.findViewById(R.id.edit_customer_name);
                    TextInputEditText Number = subView.findViewById(R.id.edit_customer_number);
                    TextInputEditText Address = subView.findViewById(R.id.edit_customer_address);
                    TextInputEditText CityState = subView.findViewById(R.id.edit_customer_city_state);
                    Button makePayment = subView.findViewById(R.id.button_make_payment);


                    makePayment.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            String name = Name.getEditableText().toString();
                            String number = Number.getEditableText().toString();
                            String address = Address.getEditableText().toString();
                            String cityState = CityState.getEditableText().toString();

                            String info = conveyInfo.getText().toString().trim();
                            if (TextUtils.isEmpty(name) || TextUtils.isEmpty(number) || TextUtils.isEmpty(address) || TextUtils.isEmpty(cityState)) {
                                if (TextUtils.isEmpty(name)) {
                                    Toast.makeText(getActivity(), "Please enter your name", Toast.LENGTH_SHORT).show();
                                    Name.setError("Please enter your Name");
                                } else if (TextUtils.isEmpty(number)) {
                                    Toast.makeText(getActivity(), "Please enter your number", Toast.LENGTH_SHORT).show();
                                    Number.setError("Please enter your number");
                                } else if (TextUtils.isEmpty(address)) {
                                    Toast.makeText(getActivity(), "Please enter your address", Toast.LENGTH_SHORT).show();
                                    Address.setError("Please enter your address");
                                } else if (TextUtils.isEmpty(cityState)) {
                                    Toast.makeText(getActivity(), "Please enter your city & state", Toast.LENGTH_SHORT).show();
                                    CityState.setError("Please enter your city & state");
                                }
                            } else {
                                placeDeliveryOrder(name, number, address, cityState, info);
                                PaymentFragment paymentFragment = new PaymentFragment();
                                FragmentTransaction fragmentTransaction = requireActivity().getSupportFragmentManager().beginTransaction();
                                fragmentTransaction.replace(R.id.container, paymentFragment);
                                fragmentTransaction.commit();
                                dialog.dismiss();
                            }
                        }
                    });
                    dialog.show();
                }
            }
        });

        return view;
    }


    @Override
    public void onStart() {
        super.onStart();
        itemCount();
        cartAdapter.startListening();
    }

    private int itemCount() {
        {
            DatabaseReference itemReference = FirebaseDatabase.getInstance().getReference().child("Cart")
                    .child("User").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child(uuid).child("Food");

            itemReference.addValueEventListener(new ValueEventListener() {
                private static final String TAG = "CartFragment";

                @Override
                public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                    if (snapshot.exists()) {
                        int count = Math.toIntExact(snapshot.getChildrenCount());
                        Log.d(TAG, "onDataChange: " + count);
                        childCount = count;
                        Log.d(TAG, "onDataChange: " + childCount);
                    }
                    else {
                        childCount = 0;
                        Log.d(TAG, "onDataChange: " + childCount);
                    }
                }

                @Override
                public void onCancelled(@NonNull @NotNull DatabaseError error) {

                }
            });
        }
        return childCount;
    }

    private int fetchTotal() {
        DatabaseReference amountReference = FirebaseDatabase.getInstance().getReference()
                .child("Cart")
                .child("User")
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .child(uuid);

        amountReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot data : dataSnapshot.getChildren()) {
                    if (data.getKey().equals("Cart Amount")) {
                        long cart_total = (long) data.getValue();
                        cartTotal = (int) cart_total;
                        totalAmount = Integer.parseInt(String.valueOf(cart_total));
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
        return totalAmount;
    }



    private void placeDeliveryOrder(String name, String number, String address, String cityState, String info) {
        String saveCurrentDate, saveCurrentTime;

        Calendar calForDate = Calendar.getInstance();
        @SuppressLint("SimpleDateFormat") SimpleDateFormat currentDate = new SimpleDateFormat("MMM dd, yyyy");
        saveCurrentDate = currentDate.format(calForDate.getTime());

        @SuppressLint("SimpleDateFormat") SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm:ss");
        saveCurrentTime = currentTime.format(calForDate.getTime());

        final DatabaseReference orderReference = FirebaseDatabase.getInstance().getReference().child("Orders").child("Delivery");

        final HashMap<String, Object> orderMap = new HashMap<>();
        orderMap.put("uuid", uuid);
        orderMap.put("cartAmount", cartTotal);
        orderMap.put("orderAmount", Integer.parseInt((String) total.getText()));
        orderMap.put("deliveryCharge", 50);
        orderMap.put("name", name);
        orderMap.put("number", number);
        orderMap.put("address", address);
        orderMap.put("cityState", cityState);
        orderMap.put("Info", info);
        orderMap.put("date", saveCurrentDate);
        orderMap.put("time", saveCurrentTime);
        orderMap.put("state", "Not Shipped");

        orderReference.child(Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid()).child(saveCurrentDate + " " + saveCurrentTime)
                .updateChildren(orderMap)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull @NotNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            FirebaseDatabase.getInstance().getReference()
                                    .child("Cart")
                                    .child("User")
                                    .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                    .child(uuid)
                                    .removeValue();
                            sharedPrefManager.clear();
                            sharedPreferences = requireActivity().getSharedPreferences("uuid", Context.MODE_PRIVATE);
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.clear();
                            editor.commit();
                        }
                    }
                });
    }

    private void addPickup(String name, String number, String info) {
        String saveCurrentDate, saveCurrentTime;

        Calendar calForDate = Calendar.getInstance();
        @SuppressLint("SimpleDateFormat") SimpleDateFormat currentDate = new SimpleDateFormat("MMM dd, yyyy");
        saveCurrentDate = currentDate.format(calForDate.getTime());

        @SuppressLint("SimpleDateFormat") SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm:ss");
        saveCurrentTime = currentTime.format(calForDate.getTime());

        final DatabaseReference orderReference = FirebaseDatabase.getInstance().getReference().child("Orders").child("Pickup");

        final HashMap<String, Object> orderMap = new HashMap<>();
        orderMap.put("uuid", uuid);
        orderMap.put("cartAmount", cartTotal);
        orderMap.put("orderAmount", totalAmount);
        orderMap.put("name", name);
        orderMap.put("number", number);
        orderMap.put("Info", info);
        orderMap.put("date", saveCurrentDate);
        orderMap.put("time", saveCurrentTime);
        orderMap.put("state", "Not Shipped");

        orderReference.child(Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid()).child(saveCurrentDate + " " + saveCurrentTime)
                .updateChildren(orderMap)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull @NotNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            FirebaseDatabase.getInstance().getReference()
                                    .child("Cart")
                                    .child("User")
                                    .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                    .child(uuid)
                                    .removeValue();
                            sharedPrefManager.clear();
                            sharedPreferences = requireActivity().getSharedPreferences("uuid", Context.MODE_PRIVATE);
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.clear();
                            editor.commit();
                        }
                    }
                });
    }

    private void enterInfoToConvey() {
        LayoutInflater inflater = LayoutInflater.from(getActivity());
        View subView = inflater.inflate(R.layout.dialog_shop_info, null);

        Dialog dialog = new Dialog(getActivity());
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        Window window = dialog.getWindow();
        window.setGravity(Gravity.CENTER);
        window.getAttributes().windowAnimations = R.style.DialogAnimation;
        dialog.setContentView(subView);
        //AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        //builder.setView(subView);
        //builder.create();

        final EditText edit_info = subView.findViewById(R.id.edit_text_info);
        Button saveButton = subView.findViewById(R.id.button_save_text_info);

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String info = edit_info.getText().toString().trim();
                conveyInfo.setText(info);
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    @Override
    public void onStop() {
        super.onStop();
        cartAdapter.stopListening();
    }
}
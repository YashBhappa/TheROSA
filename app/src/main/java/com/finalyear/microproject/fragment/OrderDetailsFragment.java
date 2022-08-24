package com.finalyear.microproject.fragment;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.finalyear.microproject.R;
import com.finalyear.microproject.adapter.OrderDetailsAdapter;
import com.finalyear.microproject.model.OrderDetails;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textview.MaterialTextView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.hsalf.smileyrating.SmileyRating;

import org.jetbrains.annotations.NotNull;

import java.text.MessageFormat;
import java.util.HashMap;

public class OrderDetailsFragment extends Fragment   {
    LinearLayout deliveryChargeLayout;
    TextView orderName, orderDateTime, orderInfo, orderAddress, orderAmount, orderItemTotal, orderDeliveryCharge, orderId, transactionId;
    String uuid, type;
    ImageView close;

    OrderDetailsAdapter orderDetailsAdapter;
    RecyclerView orderDetailsRv;
    DatabaseReference orderDetailsReference;

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_order_detail, container, false);

        Bundle bundle = getArguments();
        uuid = bundle.getString("uuid", "");
        type = bundle.getString("type", "");

        orderDetailsRv = view.findViewById(R.id.recycler_order_items);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        orderDetailsRv.setLayoutManager(layoutManager);

        orderDetailsReference = FirebaseDatabase.getInstance().getReference().child("Cart").child("Admin")
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .child(uuid).child("Food");

        FirebaseRecyclerOptions<OrderDetails> options =
                new FirebaseRecyclerOptions.Builder<OrderDetails>()
                .setQuery(orderDetailsReference, OrderDetails.class)
                .build();

        orderDetailsAdapter = new OrderDetailsAdapter(options, getActivity());
        orderDetailsRv.setAdapter(orderDetailsAdapter);

        orderName = view.findViewById(R.id.text_shop_name);
        orderDateTime = view.findViewById(R.id.text_order_time);
        orderInfo = view.findViewById(R.id.text_info);

        orderId = view.findViewById(R.id.text_order_id);
        orderId.setText(uuid);

        orderAddress = view.findViewById(R.id.text_delivery_location);
        orderAmount = view.findViewById(R.id.text_total_price);
        orderItemTotal = view.findViewById(R.id.text_item_total_price);
        orderDeliveryCharge = view.findViewById(R.id.text_delivery_price);

        transactionId = view.findViewById(R.id.text_transaction_id);
        transactionId.setText(FirebaseAuth.getInstance().getCurrentUser().getUid());

        {
            DatabaseReference amountReference = FirebaseDatabase.getInstance().getReference()
                    .child("Orders")
                    .child(type)
                    .child(FirebaseAuth.getInstance().getCurrentUser().getUid());

            amountReference.orderByChild(uuid);

            amountReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    for (DataSnapshot data : dataSnapshot.getChildren()) {
                        String name = (String) data.child("name").getValue();
                        String info = (String) data.child("Info").getValue();
                        String date = (String) data.child("date").getValue();
                        String time = (String) data.child("time").getValue();
                        String address = (String) data.child("address").getValue();
                        String cityState = (String) data.child("cityState").getValue();
                        long cartAmount = (long) data.child("cartAmount").getValue();
                        long OrderAmount = (long) data.child("orderAmount").getValue();
                        String number = (String) data.child("number").getValue();

                        orderName.setText(name);

                        if(!(info.isEmpty())) {
                            orderInfo.setText(info);
                            orderInfo.setVisibility(View.VISIBLE);
                        }

                        orderDateTime.setText(MessageFormat.format("{0} {1}", date, time));

                        if(address != null) {
                            orderAddress.setVisibility(View.VISIBLE);
                            orderAddress.setText(MessageFormat.format("{0} {1}", address, cityState));
                        }

                        orderAmount.setText(String.valueOf(OrderAmount));
                        orderItemTotal.setText(String.valueOf(cartAmount));

                        if(type.equals("Pickup")){
                            deliveryChargeLayout = view.findViewById(R.id.layout_delivery_charge);
                            deliveryChargeLayout.setVisibility(View.INVISIBLE);
                        }
                        else{
                            long ChargeAmount = (long) data.child("deliveryCharge").getValue();
                            orderDeliveryCharge.setText(String.valueOf(ChargeAmount));
                        }
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                }
            });
        }

        MaterialTextView rateFood = view.findViewById(R.id.text_rate);
        rateFood.setOnClickListener(v -> {
            LayoutInflater inflater1 = LayoutInflater.from(getActivity());
            View subView = inflater1.inflate(R.layout.dialog_rate_food, null);

            Dialog dialog = new Dialog(getActivity());
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

            dialog.setContentView(subView);
            dialog.setCanceledOnTouchOutside(false);

            SmileyRating smileyRating = subView.findViewById(R.id.smiley_rating);
            TextInputEditText feedback = subView.findViewById(R.id.edit_feedback);
            Button rate = subView.findViewById(R.id.button_rate);

            rate.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    double rating = 0.0;
                    SmileyRating.Type smiley = smileyRating.getSelectedSmiley();
                    String Feedback = feedback.getText().toString();

                    switch (smiley) {
                        case TERRIBLE:
                            rating = 1.0;
                            break;

                        case BAD:
                            rating = 2.0;
                            break;

                        case OKAY:
                            rating = 3.0;
                            break;

                        case GOOD:
                            rating = 4.0;
                            break;

                        case GREAT:
                            rating = 5.0;
                            break;
                        }


                    if(TextUtils.isEmpty(Feedback)) {
                        addRating(rating);
                    }else{
                        addRatingToFood(rating, feedback);
                    }

                    OrdersFragment ordersFragment = new OrdersFragment();
                    FragmentTransaction fragmentTransaction = requireActivity().getSupportFragmentManager().beginTransaction();
                    fragmentTransaction.setCustomAnimations(
                            R.anim.slide_in,  // enter
                            R.anim.fade_out,  // exit
                            R.anim.fade_in,   // popEnter
                            R.anim.slide_out);  // popExit
                    fragmentTransaction.replace(R.id.container, ordersFragment);
                    fragmentTransaction.commit();
                    dialog.dismiss();
                }
            });
         dialog.show();
        });

        close = view.findViewById(R.id.image_close);
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OrdersFragment ordersFragment = new OrdersFragment();
                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                transaction.setCustomAnimations(
                        R.anim.fade_in,  // enter
                        R.anim.slide_out,  // exit
                        R.anim.slide_in,   // popEnter
                        R.anim.fade_out);  // popExit
                transaction.replace(R.id.container, ordersFragment);
                transaction.commit();
            }
        });
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        orderDetailsAdapter.startListening();
    }

    private void addRating(double rating) {
        DatabaseReference ratingsReference = FirebaseDatabase.getInstance().getReference().child("Ratings");

        HashMap<String, Object> ratingMap = new HashMap<>();
        ratingMap.put("rating", rating);
        ratingMap.put("feedback", "");

        ratingsReference.child(uuid).updateChildren(ratingMap)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull @NotNull Task<Void> task) {
                        if(task.isSuccessful()){
                            Toast.makeText(getActivity(), "", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

    }

    private void addRatingToFood(double rating, TextInputEditText feedback) {
        DatabaseReference ratingsReference = FirebaseDatabase.getInstance().getReference().child("Ratings");

        HashMap<String, Object> ratingMap = new HashMap<>();
        ratingMap.put("rating", rating);
        ratingMap.put("feedback", feedback);

        ratingsReference.child(uuid).updateChildren(ratingMap)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull @NotNull Task<Void> task) {
                        if(task.isSuccessful()){
                            Toast.makeText(getActivity(), "", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}
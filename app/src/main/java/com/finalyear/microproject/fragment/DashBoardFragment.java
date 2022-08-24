package com.finalyear.microproject.fragment;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.view.ViewCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.finalyear.microproject.adapter.CategoryFoodAdapter;
import com.finalyear.microproject.interfaces.UpdateRecyclerView;
import com.finalyear.microproject.model.CategoryFoodModel;
import com.finalyear.microproject.R;
import com.finalyear.microproject.adapter.CategoryAdapter;
import com.finalyear.microproject.model.CategoryModel;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class DashBoardFragment extends Fragment implements UpdateRecyclerView {

    private RecyclerView categoryRv, categoryFoodRv;
    private CategoryAdapter categoryAdapter;

    private ArrayList<CategoryFoodModel> items;
    private CategoryFoodAdapter categoryFoodAdapter;
    private FirebaseFirestore db;
    ImageView searchIcon;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup root = (ViewGroup) inflater.inflate(R.layout.fragment_dashboard,container,false);

        final ArrayList<CategoryModel> item =new ArrayList<>();
        item.add(new CategoryModel(R.drawable.ic_food, "All"));
        item.add(new CategoryModel(R.drawable.pizza,"Pizza"));
        item.add(new CategoryModel(R.drawable.sand,"Sandwich"));
        item.add(new CategoryModel(R.drawable.fries,"Fries"));
        item.add(new CategoryModel(R.drawable.burger,"Burger"));
        item.add(new CategoryModel(R.drawable.cream,"Ice Cream"));
        item.add(new CategoryModel(R.drawable.ramen,"Ramen"));
        item.add(new CategoryModel(R.drawable.chicken,"Chicken"));
        item.add(new CategoryModel(R.drawable.egg,"Egg"));
        item.add(new CategoryModel(R.drawable.cake,"Cake"));
        item.add(new CategoryModel(R.drawable.breakfast,"Breakfast"));
        item.add(new CategoryModel(R.drawable.dumpling,"Dumpling"));
        item.add(new CategoryModel(R.drawable.donut,"Donut"));
        item.add(new CategoryModel(R.drawable.salad,"Salad"));
        item.add(new CategoryModel(R.drawable.soup,"Soup"));
        item.add(new CategoryModel(R.drawable.shrimp,"Shrimp"));
        item.add(new CategoryModel(R.drawable.sushis,"Sushi"));
        item.add(new CategoryModel(R.drawable.onigiri,"Onigiri"));
        item.add(new CategoryModel(R.drawable.kebab,"Kebab"));
        item.add(new CategoryModel(R.drawable.jam,"Jam"));
        item.add(new CategoryModel(R.drawable.pancake,"Pancake"));
        item.add(new CategoryModel(R.drawable.sausage,"Sausage"));

        categoryRv = root.findViewById(R.id.rv_1);
        categoryAdapter = new CategoryAdapter(item, getActivity(), this);
        categoryRv.setLayoutManager(new LinearLayoutManager(getActivity(),LinearLayoutManager.HORIZONTAL,false));
        categoryRv.setAdapter(categoryAdapter);

        //{
        //    DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("Food");
        //    ref.addListenerForSingleValueEvent(
        //            new ValueEventListener() {
        //                @Override
        //                public void onDataChange(DataSnapshot dataSnapshot) {
        //                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
        //                        for (DataSnapshot snapshot1 : snapshot.getChildren()) {
        //                            //Log.d(TAG, "onDataChange: "+snapshot1.getValue());
        //                            HashMap<String, Object> foodItems = new HashMap();
        //                            foodItems.put(snapshot1.getKey(), snapshot1.getValue());
        //                            Log.d(TAG, "foodItems: " + foodItems);
//
        //                            //FirebaseFirestore db = FirebaseFirestore.getInstance();
        //                            //db.collection("Food").document(snapshot1.getKey())
        //                            //        .set(snapshot1.getValue())
        //                            //        .addOnSuccessListener(new OnSuccessListener<Void>() {
        //                            //            @Override
        //                            //            public void onSuccess(Void unused) {
        //                            //                Log.d(TAG, "DocumentSnapshot written with ID: " + snapshot1.getValue());
        //                            //            }
        //                            //        })
        //                            //        .addOnFailureListener(new OnFailureListener() {
        //                            //            @Override
        //                            //            public void onFailure(@NonNull Exception e) {
        //                            //                Log.w(TAG, "Error adding document", e);
        //                            //            }
        //                            //        });
//
        //                            for (DataSnapshot dataSnapshot1 : snapshot1.getChildren()) {
        //                            }
        //                        }
        //                    }
        //                }
//
        //                @Override
        //                public void onCancelled(DatabaseError databaseError) {
        //                    //handle databaseError
        //                }
        //            });
        //}

        {
            db = FirebaseFirestore.getInstance();

            // creating our new array list
            items = new ArrayList<>();
            categoryFoodRv = root.findViewById(R.id.rv_2);
            categoryFoodRv.setHasFixedSize(true);
            categoryFoodRv.setLayoutManager(new LinearLayoutManager(getActivity()));

            // adding our array list to our recycler view adapter class.
            categoryFoodAdapter = new CategoryFoodAdapter(items, getActivity());

            // setting adapter to our recycler view.
            categoryFoodRv.setAdapter(categoryFoodAdapter);

                db.collection("Food")
                        .get()
                        .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                            @Override
                            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                if (!queryDocumentSnapshots.isEmpty()) {
                                    // if the snapshot is not empty we are adding our data in a list.
                                    List<DocumentSnapshot> list = queryDocumentSnapshots.getDocuments();
                                    for (DocumentSnapshot d : list) {
                                        // after getting this list we are passing that list to our object class.
                                        CategoryFoodModel c = d.toObject(CategoryFoodModel.class);
                                        // and we will pass this object class
                                        // inside our arraylist which we have
                                        // created for recycler view.
                                        items.add(c);
                                    }
                                    // after adding the data to recycler view. we are calling recycler view notifyDataSetChanged
                                    // method to notify that data has been changed in recycler view.
                                    categoryFoodAdapter.notifyDataSetChanged();
                                } else {
                                    // if the snapshot is empty we are displaying a toast message.
                                    Toast.makeText(getActivity(), "No data found in Database", Toast.LENGTH_SHORT).show();
                                }
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getActivity(), "Fail to get the data.", Toast.LENGTH_SHORT).show();
                    }
                });
        }

        ImageView cart = root.findViewById(R.id.cart_icon);
        cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction fragmentTransaction = requireActivity().getSupportFragmentManager().beginTransaction();
                CartFragment cartFragment = new CartFragment();
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.replace(R.id.container, cartFragment);
                fragmentTransaction.commit();
            }
        });

        EditText search = root.findViewById(R.id.search);
        ViewCompat.setTransitionName(search, "search");

        search.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                SearchFragment searchFragment = new SearchFragment();
                transaction.setCustomAnimations(
                        R.anim.slide_in,  // enter
                        R.anim.fade_out,  // exit
                        R.anim.fade_in,   // popEnter
                        R.anim.slide_out);  // popExit
                transaction.replace(R.id.container, searchFragment);
                transaction.addSharedElement(search, "search1");
                transaction.commit();
            return false;
            }
        });

        search.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                filter(s.toString());
            }
        });

        searchIcon = root.findViewById(R.id.search_badge);
        searchIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                SearchFragment searchFragment = new SearchFragment();
                transaction.setCustomAnimations(
                        R.anim.slide_in,  // enter
                        R.anim.fade_out,  // exit
                        R.anim.fade_in,   // popEnter
                        R.anim.slide_out);  // popExit
                transaction.replace(R.id.container, searchFragment);
                transaction.commit();
            }
        });
        return root;
    }

    private void filter(String text) {
        ArrayList<CategoryFoodModel> filteredList = new ArrayList<>();
        for (CategoryFoodModel item : items) {
            if (item.getName().toLowerCase().contains(text.toLowerCase())) {
                filteredList.add(item);
            }
        }
        if (filteredList.isEmpty()) {
            Toast.makeText(getActivity(), "No Data Found..", Toast.LENGTH_SHORT).show();
        } else {
            categoryFoodAdapter.filterList(filteredList);
        }
    }

    @Override
    public void onStart() {
        super.onStart();
       // categoryFoodAdapter.startListening();
    }

    @Override
    public void callback(int position, String name) {
        db = FirebaseFirestore.getInstance();
        ArrayList<CategoryFoodModel> items = new ArrayList<>();

        if (name.equals("")) {
            db.collection("Food")
                    .get()
                    .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                        @Override
                        public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                            if (!queryDocumentSnapshots.isEmpty()) {
                                // if the snapshot is not empty we are adding our data in a list.
                                List<DocumentSnapshot> list = queryDocumentSnapshots.getDocuments();
                                for (DocumentSnapshot d : list) {
                                    // after getting this list we are passing that list to our object class.
                                    CategoryFoodModel c = d.toObject(CategoryFoodModel.class);
                                    // and we will pass this object class
                                    // inside our arraylist which we have
                                    // created for recycler view.
                                    items.add(c);
                                }
                                // after adding the data to recycler view. we are calling recycler view notifyDataSetChanged
                                // method to notify that data has been changed in recycler view.
                                categoryFoodAdapter.notifyDataSetChanged();
                            } else {
                                // if the snapshot is empty we are displaying a toast message.
                                Toast.makeText(getActivity(), "No data found in Database", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(getActivity(), "Fail to get the data.", Toast.LENGTH_SHORT).show();
                }
            });
        }
        else{
            db.collection("Food").whereEqualTo("type", name)
                    .get()
                    .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                        @Override
                        public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                            if (!queryDocumentSnapshots.isEmpty()) {
                                // if the snapshot is not empty we are adding our data in a list.
                                List<DocumentSnapshot> list = queryDocumentSnapshots.getDocuments();
                                for (DocumentSnapshot d : list) {
                                    // after getting this list we are passing that list to our object class.
                                    CategoryFoodModel c = d.toObject(CategoryFoodModel.class);
                                    // and we will pass this object class
                                    // inside our arraylist which we have
                                    // created for recycler view.
                                    items.add(c);
                                }
                                // after adding the data to recycler view. we are calling recycler view notifyDataSetChanged
                                // method to notify that data has been changed in recycler view.
                                categoryFoodAdapter.notifyDataSetChanged();
                            } else {
                                // if the snapshot is empty we are displaying a toast message.
                                Toast.makeText(getActivity(), "No data found in Database", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(getActivity(), "Fail to get the data.", Toast.LENGTH_SHORT).show();
                }
            });
        }

        categoryFoodAdapter = new CategoryFoodAdapter(items, getActivity());
        categoryFoodRv.setAdapter(categoryFoodAdapter);
    }
}

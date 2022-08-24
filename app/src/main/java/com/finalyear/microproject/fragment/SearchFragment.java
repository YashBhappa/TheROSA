package com.finalyear.microproject.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.core.view.ViewCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.finalyear.microproject.R;
import com.finalyear.microproject.adapter.CategoryFoodAdapter;
import com.finalyear.microproject.model.CategoryFoodModel;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class SearchFragment extends Fragment {

    private ArrayList<CategoryFoodModel> items;
    private CategoryFoodAdapter categoryFoodAdapter;
    private FirebaseFirestore db;

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search, container, false);

        androidx.appcompat.widget.SearchView search1 = view.findViewById(R.id.edit_search);
        ViewCompat.setTransitionName(search1, "search1");
        search1.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                filter(newText);
                return false;

            }
        });


        RecyclerView searchRv = view.findViewById(R.id.recycler_shops);
        {
            db = FirebaseFirestore.getInstance();

            // creating our new array list
            items = new ArrayList<>();
            searchRv.setHasFixedSize(true);
            searchRv.setLayoutManager(new LinearLayoutManager(getActivity()));

            // adding our array list to our recycler view adapter class.
            categoryFoodAdapter = new CategoryFoodAdapter(items, getActivity());

            // setting adapter to our recycler view.
            searchRv.setAdapter(categoryFoodAdapter);

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

        ImageView close = view.findViewById(R.id.image_close);
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager manager = getActivity().getSupportFragmentManager();
                FragmentTransaction transaction = manager.beginTransaction();
                DashBoardFragment dashBoardFragment = new DashBoardFragment();
                transaction.setCustomAnimations(
                        R.anim.fade_in,  // enter
                        R.anim.slide_out,  // exit
                        R.anim.slide_in,   // popEnter
                        R.anim.fade_out);  // popExit
                transaction.replace(R.id.container, dashBoardFragment);
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });

        return view;
    }

    private void filter(String newText) {
            // creating a new array list to filter our data.
            ArrayList<CategoryFoodModel> filteredList = new ArrayList<>();

            // running a for loop to compare elements.
                for (CategoryFoodModel item : items) {
                // checking if the entered string matched with any item of our recycler view.
                if (item.getName().toLowerCase().contains(newText.toLowerCase())) {
                    // if the item is matched we are
                    // adding it to our filtered list.
                    filteredList.add(item);
                }
            }
            if (filteredList.isEmpty()) {
                // if no item is added in filtered list we are
                // displaying a toast message as no data found.
                Toast.makeText(getActivity(), "No Data Found..", Toast.LENGTH_SHORT).show();
            } else {
                // at last we are passing that filtered
                // list to our adapter class.
                categoryFoodAdapter.filterList(filteredList);
            }
        }
    }

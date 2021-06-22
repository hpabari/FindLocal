package com.t.findlocal.activities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.t.findlocal.models.Category;
import com.t.findlocal.R;
import com.t.findlocal.adapters.CategoryAdapter;

import java.util.ArrayList;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

public class CategoriesFragment extends Fragment {

    CategoryAdapter categoryListAdapter;
    //ListView is a type of AdapterView
    ListView listView;

    View rootView;

    // Database variables
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    CollectionReference businessesCollectionRef;
    Map<String, Object> queryResult;

    // Storage variables
    FirebaseStorage storage;
    StorageReference storageReference;
    Bitmap bitmap;

    // List of categories
    ArrayList<Category> categories;

    // Current user location
    double longitude;
    double latitude;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        //get current user location
        latitude = this.getActivity().getIntent().getDoubleExtra("latitude", 0);
        longitude = this.getActivity().getIntent().getDoubleExtra("longitude", 0);

        rootView = inflater.inflate(R.layout.fragment_categories, container, false);

        categories = new ArrayList<Category>();

        // get cloud storage instance
        storage = FirebaseStorage.getInstance();

        // Get all business category documents using businesses collection
        businessesCollectionRef = db.collection("businesses");
        businessesCollectionRef.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                // Once query is completed, if result is not empty
                // For each document, get icon file name and get file from storage
                if (task.isSuccessful() && !task.getResult().isEmpty()) {
                    for (final QueryDocumentSnapshot document : task.getResult()) {
                        queryResult = document.getData();

                        Log.d("CategoriesFragment.java", document.getId());
                        Log.d("CategoriesFragment.java", queryResult.get("icon").toString());

                        storageReference = storage.getReferenceFromUrl("gs://find-local-leic.appspot.com/icons/" + queryResult.get("icon").toString());

                        final long ONE_MEGABYTE = 1024 * 1024;
                        storageReference.getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
                            // Get document name (which is the category name) and icon and category name to Categories list for it to be populated in the list view
                            @Override
                            public void onSuccess(byte[] bytes) {
                                bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                                Log.d("CategoriesFragment.java", "BYTES" + bytes.length);
                                categories.add(new Category(document.getId(), bitmap));

                                categoryListAdapter = new CategoryAdapter(getActivity(), categories);
                                listView = rootView.findViewById(R.id.categories_list_view);
                                listView.setAdapter(categoryListAdapter);

                                // category list view onclick listener
                                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                    //get name (category name) of item at position in the listview
                                    // open CategoryResults activity showing the businesses in this category
                                    @Override
                                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                        Category selectedCategory = (Category)parent.getItemAtPosition(position);
                                        onCategorySelected(selectedCategory);
                                    }
                                });
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Bitmap icon = BitmapFactory.decodeResource(getContext().getResources(), R.mipmap.find_local);
                                categories.add(new Category(document.getId(),icon));

                                categoryListAdapter = new CategoryAdapter(getActivity(), categories);
                                listView = rootView.findViewById(R.id.categories_list_view);
                                listView.setAdapter(categoryListAdapter);

                                // category list view onclick listener
                                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                    //get name (category name) of item at position in the listview
                                    @Override
                                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                        Category selectedCategory = (Category)parent.getItemAtPosition(position);
                                        onCategorySelected(selectedCategory);
                                    }
                                });
                            }
                        });
                    }
                }

            }
        });
        return rootView;
    }

    public void onCategorySelected(Category selectedCategory){
        final String selectedCategoryName =  selectedCategory.getName();
        Log.d("CategoriesFragment.java", selectedCategoryName);

        // send category name and current location to new activity
        Intent intent = new Intent(getActivity(), CategoriesResults.class);
        intent.putExtra("category", selectedCategoryName);
        intent.putExtra("latitude", latitude);
        intent.putExtra("longitude", longitude);
        startActivity(intent);
    }
}

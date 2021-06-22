package com.t.findlocal.activities;

import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.t.findlocal.models.Business;
import com.t.findlocal.R;
import com.t.findlocal.adapters.ResultsAdapter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;


public class TextSearchFragment extends Fragment {

    // Database variables
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference businessesRef;

    // UI elements
    private EditText searchText;
    private Button searchButton;
    private LinearLayout searchLinearLayout;
    private TextView resultsTextView;

    // List of businesses
    public ArrayList<Business> businesses;

    // Listview in UI
    private ResultsAdapter listResultsAdapter;
    private ListView searchResultListView;

    private View rootView;

    private double longitude;
    private double latitude;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        // Get current user location
        latitude = this.getActivity().getIntent().getDoubleExtra("latitude", 0);
        longitude = this.getActivity().getIntent().getDoubleExtra("longitude", 0);

        // Initialise
        businesses = new ArrayList<Business>();

        // Get fragment view
        rootView = inflater.inflate(R.layout.fragment_text_search, container, false);

        // Initialise UI elements
        //https://www.tutorialspoint.com/how-to-use-findviewbyid-in-fragment
        searchButton = rootView.findViewById(R.id.search_button);
        searchText = rootView.findViewById(R.id.search_edit_text);
        resultsTextView = rootView.findViewById(R.id.results_text_view);

        // assign ResultsAdapter to the ListView
        listResultsAdapter = new ResultsAdapter(getActivity(), businesses);
        searchResultListView = rootView.findViewById(R.id.results_list_view);
        searchResultListView.setAdapter(listResultsAdapter);

        // get all businesses and display in listview
        getAllBusinesses();

        // Onclick listener for the search button
        searchButton.setOnClickListener(new View.OnClickListener() {
            //The code in this method will be executed when the search button is clicked on
            @Override
            public void onClick(View v) {
                search();
            }
        });

        // Search on pressing enter on the keyboard
        searchText.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_ENTER) {
                    search();
                    return true;
                }
                return false;
            }
        });

        return rootView;
    }

//    @Override
//    public void onSaveInstanceState(@NonNull Bundle outState) {
//        super.onSaveInstanceState(outState);
//
//        if(outState != null){
//            viewModel.saveState(outState);
//        }
//    }

    // Method for searching
    private void search() {
        // hide keyboard after clicking on search
        InputMethodManager inputManager = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        if (getActivity().getCurrentFocus() != null) {
            inputManager.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }

        // If the user has not entered anything to search for, return all businesses
        if (searchText.getText().toString().equals("")) {
            resultsTextView.setText("");
            getAllBusinesses();
        }
        else {
            // Change search text to lowercase
            String lowercaseSearchText = searchText.getText().toString().toLowerCase();
            // Split each word and save in an array
            String[] splitSearch = lowercaseSearchText.split("\\s+");
            // Create a list of words from the array
            List<String> searchWordsList = new ArrayList<String>(Arrays.asList(splitSearch));
            // Trim white space and add the full text from the search to the list
            searchWordsList.add(lowercaseSearchText.trim());

            // create a query to search for any of the words from the list in the array searchWords of each business categories in the database
            Query query = businessesRef.whereArrayContainsAny("searchWords", searchWordsList);

            Log.d("TextSearchFragment", searchText.getText().toString());

            // Clear the search text view
            resultsTextView.setText("");

            // Execute query
            query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    if (task.isSuccessful()) {
                        // Clear businesses list
                        businesses.clear();
                        // If result is not empty, create and execute query to get all businesses in the matching collections
                        if (!task.getResult().isEmpty()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d("TextSearchFragment", document.getId() + " => " + document.getData());
                                Query query2 = businessesRef.document(document.getId()).collection("collection");
                                query2.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                        Log.d("TextSearchFragment", "second query complete " + task.getResult().getDocuments());

                                        if (task.isSuccessful() && !task.getResult().isEmpty()) {
                                            // For each business document, call addToBusinessArrayList method to add the business to the arraylist
                                            // to be displayed to user in the listview
                                            for (QueryDocumentSnapshot document : task.getResult()) {

                                                String category = document.getData().get("category").toString();
                                                String name = document.getData().get("name").toString();

                                                String url = "gs://find-local-leic.appspot.com/" + category + "/" + document.getId() + "/" + "mainImage.jpg";
                                                // get Firebase cloud storage instance
                                                storage = FirebaseStorage.getInstance();
                                                storageReference = storage.getReferenceFromUrl(url);

//                                                bitmap = BitmapFactory.decodeResource(getContext().getResources(), R.mipmap.find_local);

                                                storageReference.getBytes(1024 * 1024).addOnSuccessListener(new OnSuccessListener<byte[]>() {
                                                    @Override
                                                    public void onSuccess(byte[] bytes) {
                                                        Log.d("SearchActivity.java", "onSuccess: HERE");
                                                        bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                                                        SearchActivity.addToBusinessArrayList(document, businesses, getContext(), latitude, longitude, bitmap);
                                                        listResultsAdapter.notifyDataSetChanged();
                                                    }
                                                }).addOnFailureListener(new OnFailureListener() {
                                                    @Override
                                                    public void onFailure(@NonNull Exception e) {
                                                        Log.d("DetailsActivity.java", "ERROR: " + e.getMessage());
                                                        bitmap = BitmapFactory.decodeResource(getContext().getResources(), R.mipmap.find_local);
                                                        SearchActivity.addToBusinessArrayList(document, businesses, getContext(), latitude, longitude, bitmap);
                                                        listResultsAdapter.notifyDataSetChanged();
                                                    }
                                                });
//                                                SearchActivity.addToBusinessArrayList(document.getData(), businesses, getContext(), latitude, longitude, bitmap);
                                            }
                                        }
                                        else {
                                            resultsTextView = rootView.findViewById(R.id.results_text_view);
                                            resultsTextView.setText("None found");
                                        }
                                        Log.d("TextSearchFragment", "businesses: " + businesses.size());

                                        // https://developer.android.com/reference/android/widget/ArrayAdapter#notifyDataSetChanged()
                                        // Notifies the attached observers that the underlying data has been changed and any View reflecting the data set should refresh itself
                                        listResultsAdapter.notifyDataSetChanged();
                                  }
                                });
                            }
                        }
                        // If there are no matches, inform user of no results
                        else {
                            resultsTextView.setText("None found");
                        }
                        // https://developer.android.com/reference/android/widget/ArrayAdapter#notifyDataSetChanged()
                        // Notifies the attached observers that the underlying data has been changed and any View reflecting the data set should refresh itself
                        listResultsAdapter.notifyDataSetChanged();

                        // Set on click listener on listview
                        // Get user selected item and execute sendToDetailsActivity method
                        searchResultListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                Business business = (Business) parent.getItemAtPosition(position);
                                sendToDetailsActivity(business);
                            }
                        });
                    }
                    // If query is unsuccessful
                    else {
                        Log.d("TextSearchFragment", "Error getting documents: ", task.getException());
                    }
                }
            });
        }
    }

    FirebaseStorage storage;
    StorageReference storageReference;
    Bitmap bitmap;

    private void getAllBusinesses() {
        // Clear businesses arraylist
        businesses.clear();
        // Database businesses collection reference
        businessesRef = db.collection("businesses");
        // Get all businesses in all collections
        // Add to businesses arraylist and display in listview
        businessesRef.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful() && !task.getResult().isEmpty()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        String docId= document.getId();
                        Query getBusinessesQuery = businessesRef.document(docId).collection("collection");
                        getBusinessesQuery.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful() && !task.getResult().isEmpty()) {
                                    for (QueryDocumentSnapshot document : task.getResult()) {
                                        String category = document.getData().get("category").toString();
                                        String name = document.getData().get("name").toString();

                                        String url = "gs://find-local-leic.appspot.com/" + category + "/" + document.getId() + "/" + "mainImage.jpg";
                                        // get Firebase cloud storage instance
                                        storage = FirebaseStorage.getInstance();
                                        storageReference = storage.getReferenceFromUrl(url);

                                        bitmap = BitmapFactory.decodeResource(getContext().getResources(), R.mipmap.find_local);

                                        storageReference.getBytes(1024 * 1024).addOnSuccessListener(new OnSuccessListener<byte[]>() {
                                            @Override
                                            public void onSuccess(byte[] bytes) {
                                                Log.d("SearchActivity.java", "onSuccess: HERE");
                                                bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                                                SearchActivity.addToBusinessArrayList(document, businesses, getContext(), latitude, longitude, bitmap);
                                                listResultsAdapter.notifyDataSetChanged();
                                            }
                                        }).addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                Log.d("DetailsActivity.java", "ERROR: " + e.getMessage());
                                                bitmap = BitmapFactory.decodeResource(getContext().getResources(), R.mipmap.find_local);
                                                SearchActivity.addToBusinessArrayList(document, businesses, getContext(), latitude, longitude, bitmap);
                                                listResultsAdapter.notifyDataSetChanged();
                                            }
                                        });
                                        Log.d("TextSearchFragment", "businesses: " + businesses.size());
                                    }
                                }
                            }
                        });
                    }
                    // Set on click listener on listview
                    // Get user selected item and execute sendToDetailsActivity method
                    searchResultListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            Business business = (Business) parent.getItemAtPosition(position);
                            sendToDetailsActivity(business);
                        }
                    });
                }
            }
        });
    }



    /*
     * add all data of business to new DetailsActivity intent to send to the details screen
     */
    public void sendToDetailsActivity(Business business) {
        // Ordinary Intent for launching a new activity
        Intent intent = new Intent(getContext(), DetailsActivity.class);

//        Log.d("SearchActivity.java", business.getName());
//        Log.d("SearchActivity.java", business.getDescription());
//        Log.d("SearchActivity.java", business.getAddress());
//        Log.d("SearchActivity.java", business.getLongDescription());
//        Log.d("SearchActivity.java", business.getPhoneNumber());
//        Log.d("SearchActivity.java", business.getEmail());
//        Log.d("SearchActivity.java", business.getWebsite());

        intent.putExtra("businessId", business.getId());
        intent.putExtra("businessName", business.getName());
        intent.putExtra("shortDescription", business.getDescription());
        intent.putExtra("address", business.getAddress());
        intent.putExtra("longDescription", business.getLongDescription());
        intent.putExtra("email", business.getEmail());
        intent.putExtra("phoneNumber", business.getPhoneNumber());
        intent.putExtra("website", business.getWebsite());
        intent.putExtra("services", business.getServices());
        intent.putExtra("category", business.getCategory());

        //Start the Intent
        ActivityCompat.startActivity(getContext(), intent, ActivityOptions.makeSceneTransitionAnimation(getActivity()).toBundle());
    }
}

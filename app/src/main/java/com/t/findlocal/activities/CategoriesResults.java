package com.t.findlocal.activities;

import android.app.ActivityOptions;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

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

import java.util.ArrayList;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

public class CategoriesResults extends AppCompatActivity {

    ResultsAdapter listResultsAdapter;
    ListView resultListView;

    // List of businesses
    ArrayList<Business> businesses;

    // Database variables
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    CollectionReference businessesRef;
    Map<String, Object> queryResult;

    // User selected category
    String selectedCategory;

    // Current user location
    double longitude;
    double latitude;
    FirebaseStorage storage;
    StorageReference storageReference;
    Bitmap bitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category_result);

        // get selected category name
        selectedCategory = getIntent().getStringExtra("category");

        // Get current user location values
        latitude = getIntent().getDoubleExtra("latitude", 0);
        longitude = getIntent().getDoubleExtra("longitude", 0);

        // Database businesses collection reference
        businessesRef = db.collection("businesses");

        // for each category document in the database
        // get all the documents in the collection of that category
        // i.e. all the businesses in that category
        Query query = businessesRef.document(selectedCategory).collection("collection");
        query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                businesses = new ArrayList<Business>();
                if (task.isSuccessful() && !task.getResult().isEmpty()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {

                        String category = document.getData().get("category").toString();
                        String name = document.getData().get("name").toString();

                        String url = "gs://find-local-leic.appspot.com/" + category + "/" + document.getId() + "/" + "mainImage.jpg";
                        // get Firebase cloud storage instance
                        storage = FirebaseStorage.getInstance();
                        storageReference = storage.getReferenceFromUrl(url);

                        bitmap = BitmapFactory.decodeResource(getBaseContext().getResources(), R.mipmap.find_local);

                        storageReference.getBytes(1024 * 1024).addOnSuccessListener(new OnSuccessListener<byte[]>() {
                            @Override
                            public void onSuccess(byte[] bytes) {
                                Log.d("SearchActivity.java", "onSuccess: HERE");
                                bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                                SearchActivity.addToBusinessArrayList(document, businesses, getBaseContext(), latitude, longitude, bitmap);
                                listResultsAdapter.notifyDataSetChanged();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.d("DetailsActivity.java", "ERROR: " + e.getMessage());
                                bitmap = BitmapFactory.decodeResource(getBaseContext().getResources(), R.mipmap.find_local);
                                SearchActivity.addToBusinessArrayList(document, businesses, getBaseContext(), latitude, longitude, bitmap);
                                listResultsAdapter.notifyDataSetChanged();
                            }
                        });

                        Log.d("CategoriesResults.java", document.getId());
                    }
                }
                listResultsAdapter = new ResultsAdapter(CategoriesResults .this, businesses);
                resultListView = findViewById(R.id.category_results_list_view);
                // assign ResultsAdapter to the ListView
                resultListView.setAdapter(listResultsAdapter);

                resultListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        Business business = (Business) parent.getItemAtPosition(position);
                        sendToDetailsActivity(business);
                    }
                });
            }
        });
    }

    /*
     * add all data of business to new DetailsActivity intent to send to the details screen
     */
    public void sendToDetailsActivity(Business business) {
        // Ordinary Intent for launching a new activity
        Intent intent = new Intent(CategoriesResults.this, DetailsActivity.class);

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
        ActivityCompat.startActivity(CategoriesResults.this, intent, ActivityOptions.makeSceneTransitionAnimation(CategoriesResults.this).toBundle());
    }
}

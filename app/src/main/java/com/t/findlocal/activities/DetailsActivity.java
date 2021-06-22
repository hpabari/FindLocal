package com.t.findlocal.activities;

import android.app.ActivityOptions;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Paint;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.ListResult;
import com.google.firebase.storage.StorageReference;
import com.synnapps.carouselview.CarouselView;
import com.synnapps.carouselview.ImageListener;
import com.t.findlocal.R;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class DetailsActivity extends AppCompatActivity implements OnMapReadyCallback{

    // GoogleMap instance
    private GoogleMap mMap;

    // Business details
    String businessId;
    String businessName;
    String description;
    String address;
    String longDescription;
    String email;
    String phoneNumber;
    String website;
    ArrayList<String> services;
    String category;

    // Firebase cloud storage
    FirebaseStorage storage;
    StorageReference storageReference;
    Bitmap bitmap;
    String url;

    // Carousel for images
    CarouselView carouselView;
    ArrayList<Bitmap> images = new ArrayList<>();
    ImageListener imageListener = new ImageListener() {
        @Override
        public void setImageForPosition(int position, ImageView imageView) {
            imageView.setImageBitmap(images.get(position));
            imageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
        }
    };

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        // Get all business data from previous activity
        Bundle businessDetailBundle = getIntent().getExtras();
        businessId = businessDetailBundle.getString("businessId");
        businessName = businessDetailBundle.getString("businessName");
        description = businessDetailBundle.getString("shortDescription");
        address = businessDetailBundle.getString("address");
        longDescription = businessDetailBundle.getString("longDescription");
        email = businessDetailBundle.getString("email");
        phoneNumber = businessDetailBundle.getString("phoneNumber");
        website = businessDetailBundle.getString("website");
        services = businessDetailBundle.getStringArrayList("services");
        category = businessDetailBundle.getString("category");


//        Log.d("DetailsActivity.java", businessName);
//        Log.d("DetailsActivity.java", description);
//        Log.d("DetailsActivity.java", address);
//        Log.d("SearchActivity.java", longDescription);
//        Log.d("SearchActivity.java", email);
//        Log.d("SearchActivity.java", phoneNumber);
//        Log.d("SearchActivity.java", website);


        // Set all UI elements with data and images from storage

        // Business Name
        TextView nameView = findViewById(R.id.bus_name);
        nameView.setText(businessName);
        //TODO
        if(this.getActionBar() == null){
            Log.e("DetailsActivity.java", "ACTION BAR NULL");
        }
        else {
            this.getActionBar().setTitle(businessName);
        }

        // Description
        TextView descripView = findViewById(R.id.long_descrip);
        descripView.setText(longDescription);

        // Images
        carouselView = findViewById(R.id.carouselView);
        carouselView.setImageListener(imageListener);

        Log.d("DetailsActivity.java", "CATEGORY: " + category);
        Log.d("DetailsActivity.java", "BUSINESS NAME: " + businessName);

        url = "gs://find-local-leic.appspot.com/" + category + "/" + businessId;

        // get cloud storage instance
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReferenceFromUrl(url);

        carouselView.setVisibility(View.GONE);
        storageReference.listAll().addOnSuccessListener(new OnSuccessListener<ListResult>() {
            @Override
            public void onSuccess(ListResult listResult) {
                if(listResult.getItems().size() > 0) {
                    for (StorageReference item : listResult.getItems()) {
                        storageReference = storage.getReferenceFromUrl("gs://find-local-leic.appspot.com/" + category + "/" + businessId + "/" + item.getName());
                        storageReference.getBytes(1024 * 1024).addOnSuccessListener(new OnSuccessListener<byte[]>() {
                            @Override
                            public void onSuccess(byte[] bytes) {
                                bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                                images.add(bitmap);
                                carouselView.setPageCount(images.size());
                                carouselView.setVisibility(View.VISIBLE);
                            }
                        });
                        Log.d("DetailsActivity.java", "URL: " + item);
                    }
                }
                else{
                    carouselView.setVisibility(View.GONE);
                }
            }
        })
        .addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                carouselView.setVisibility(View.GONE);
                Log.e("DetailsActivity.java", "ERROR: " + e.getMessage());
            }
        });


        // Website if exists
        TextView websiteView = findViewById(R.id.website);
        if(website == null) {
            websiteView.setVisibility(View.GONE);
            ImageView websiteIcon = findViewById(R.id.website_icon);
            websiteIcon.setVisibility(View.GONE);
        }
        else{
            websiteView.setText(website);
            websiteView.setPaintFlags(websiteView.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
            websiteView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent websiteIntent = new Intent(Intent.ACTION_VIEW);
                    websiteIntent.setData(Uri.parse("https://" + website));
                    startActivity(websiteIntent);
                }
            });
        }

        // Email if exists
        TextView emailView = findViewById(R.id.email);
        if(email == null) {
            emailView.setVisibility(View.GONE);
            ImageView emailIcon = findViewById(R.id.email_icon);
            emailIcon.setVisibility(View.GONE);
        }
        else{
            emailView.setText(email);
            emailView.setPaintFlags(emailView.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
            emailView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent emailIntent = new Intent(Intent.ACTION_SEND);
                    emailIntent.setType("text/plain");
                    emailIntent.setData(Uri.parse("mailto:"));
                    emailIntent.putExtra(Intent.EXTRA_EMAIL, new String[]{email});
                    emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Find Local Customer Query");
                    startActivity(emailIntent);
                }
            });
        }

        // Phone Number
        TextView phoneNumberView = findViewById(R.id.phone_number);
        phoneNumberView.setText(phoneNumber);
        phoneNumberView.setPaintFlags(phoneNumberView.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        phoneNumberView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent phoneIntent = new Intent(Intent.ACTION_DIAL);
                phoneIntent.setData(Uri.parse("tel:" + phoneNumber));
                startActivity(phoneIntent);
            }
        });

        // Address
        TextView addressView = findViewById(R.id.address);
        addressView.setText(address);
    }

    // Going back transition between screens
    public void onBackPressed() {
        supportFinishAfterTransition();
    }

    /*
    * On map ready method
    */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        // Get business location
        final LatLng businessLocation = getLatLng(address);
        // Add marker on map using location values
        mMap.addMarker(new MarkerOptions().position(businessLocation).title(businessName));
        // Zoom out
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(businessLocation, 15));

        // On map click, send marker location and business name to MapsActivity and start the activity
        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                Intent intent = new Intent(getBaseContext(), MapsActivity.class);
                intent.putExtra("latitude", businessLocation.latitude);
                intent.putExtra("longitude", businessLocation.longitude);
                intent.putExtra("businessName", businessName);
                startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(DetailsActivity.this).toBundle());
            }
        });
    }

    /*
     * Get latitude and longitude from address
     */
    public LatLng getLatLng(String businessAddress){
        Log.d("DetailsActivity.java", "onMapReady: " + businessAddress);

        Geocoder geocoder = getGeocoder();
                //new Geocoder(this);
        List<Address> addresses;
        LatLng latLng = new LatLng(52.63, -1.13);

        try {
            addresses = geocoder.getFromLocationName( businessAddress, 1);
            if(addresses != null){
                Address address = addresses.get(0);
                latLng = new LatLng(address.getLatitude(), address.getLongitude());
            }
        }
        catch (IOException ioex){
            Log.d("DetailsActivity.java", "onMapReady: " + ioex.getMessage());
        }
        return latLng;
    }

    private Geocoder getGeocoder() {
        return new Geocoder(this);
    }
}

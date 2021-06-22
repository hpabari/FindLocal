package com.t.findlocal.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.t.findlocal.R;

public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback {

    // GoogleMap instance
    private GoogleMap mMap;

    // Business location and name
    private double latitude;
    private double longitude;
    private String businessName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        // Get business location values and name from previous activity
        latitude = this.getIntent().getDoubleExtra("latitude", 52.63);
        longitude = this.getIntent().getDoubleExtra("longitude", -1.13);
        businessName = this.getIntent().getStringExtra("businessName");

        //TODO
        if(this.getActionBar() == null){
            Log.e("DetailsActivity.java", "ACTION BAR NULL");
        }
        else {
            this.getActionBar().setTitle(businessName);
        }
        
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add marker for business location
        LatLng businessLocation = new LatLng(latitude, longitude);
        mMap.addMarker(new MarkerOptions().position(businessLocation).title(businessName));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(businessLocation, 15));
    }
}

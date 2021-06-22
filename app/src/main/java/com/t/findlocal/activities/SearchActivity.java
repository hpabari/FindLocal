package com.t.findlocal.activities;

import android.content.Context;
import android.graphics.Bitmap;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.t.findlocal.models.Business;
import com.t.findlocal.adapters.FragmentPagerAdapter;
import com.t.findlocal.R;

import java.io.IOException;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

public class SearchActivity extends AppCompatActivity {

    static final double METERS_TO_MILES = 0.000621371;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        // Find the view pager that will allow the user to swipe between fragments
        ViewPager viewPager = (ViewPager) findViewById(R.id.viewpager);

        // Create an adapter that knows which fragment should be shown on each page
        FragmentPagerAdapter adapter = new FragmentPagerAdapter(getSupportFragmentManager());

        // Set the adapter onto the view pager
        viewPager.setAdapter(adapter);
    }

    public static void addToBusinessArrayList(QueryDocumentSnapshot documentSnapshot, ArrayList<Business> businesses, Context context,
                                              double latitude, double longitude, Bitmap bitmap) {
        // Get business location using the address from the query result
        LatLng busToAddLatLng = getLatLng(documentSnapshot.getData().get("address").toString(), context);
        Log.d("SearchActivity.java", bitmap.toString());

        // Get distance between current user location and business location
        float busToAddDistance = getDistanceBetween(latitude, longitude, busToAddLatLng);
        String busToAddDistanceInMiles = getDistanceInMiles(busToAddDistance);

        // Create business instance according to data obtained and calculated distance
        Business businessToAdd = createBusinessInstance(documentSnapshot, busToAddLatLng, busToAddDistanceInMiles);
        businessToAdd.setImage(bitmap);

        // If array is empty, add Business and return
        if(businesses.size() == 0){
            businesses.add(businessToAdd);
            return;
        }

        // Iterate through businesses and compare distance between current location and businesses and between the business to add
        // Add the business in the array where distance is less than the business in the array
        // Results in an array of businesses in order of increasing distance to the current user location
        for(int i=0; i<businesses.size(); i++) {
            LatLng thisBusLatLng = businesses.get(i).getDistance();
            float thisBusDistance = getDistanceBetween(latitude, longitude, thisBusLatLng);

            int compareDistances = Float.compare(busToAddDistance, thisBusDistance);

            if(compareDistances < 0){
                businesses.add(i, businessToAdd);
                break;
            }
            else if(i == businesses.size()-1){
                businesses.add(businessToAdd);
                break;
            }
        }
    }

    /*
    * Create business object with data retrieved from database
    */
    public static Business createBusinessInstance(QueryDocumentSnapshot documentSnapshot, LatLng busToAddLatLng, String busToAddDistanceInMiles) {
        Map<String, Object> queryResult = documentSnapshot.getData();
        String category = queryResult.get("category").toString();
        String name = queryResult.get("name").toString();

        Business businessToAdd = new Business(
                documentSnapshot.getId(),
                name,
                queryResult.get("description").toString(),
                queryResult.get("address").toString(),
                queryResult.get("longDescription").toString(),
                queryResult.get("phoneNumber").toString(),
                busToAddLatLng,
                busToAddDistanceInMiles,
                category);

        // If the business has a website, add website
        if(queryResult.get("website") != null){
            businessToAdd.setWebsite(queryResult.get("website").toString());
        }
        // If the business has an email address, add email
        if(queryResult.get("email") != null){
            businessToAdd.setEmail(queryResult.get("email").toString());
        }

        return businessToAdd;
    }

    /*
    * Get distance between current user location and business location
    */
    public static float getDistanceBetween(double latitude, double longitude, LatLng busLatLng) {
        float[] results = new float[3];
        Location.distanceBetween(latitude, longitude, busLatLng.latitude, busLatLng.longitude, results);
//        Log.d("SearchActivity.java", "distance: " + results[0]);

        return results[0];
    }

    /*
     * Get distance in miles from meters
     */
    public static String getDistanceInMiles(float busToAddDistance) {
        double distanceInMiles = busToAddDistance * METERS_TO_MILES;

        DecimalFormat df = new DecimalFormat("#.##");
        df.setRoundingMode(RoundingMode.CEILING);
        return df.format(distanceInMiles);
    }

    /*
    * Get latitude and longitude from address
    */
    public static LatLng getLatLng(String businessAddress, Context context) {
        Geocoder geocoder = new Geocoder(context);
        List<Address> addresses;
        LatLng latLng = new LatLng(52.63, -1.13);

        try {
            addresses = geocoder.getFromLocationName(businessAddress, 1);
            if (addresses.size() != 0) {
                Address address = addresses.get(0);
                latLng = new LatLng(address.getLatitude(), address.getLongitude());
            }
        } catch (IOException ioex) {
            Log.e("DetailsActivity.java", "onMapReady: " + ioex.getMessage());
        }
        return latLng;
    }
}

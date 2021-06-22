package com.t.findlocal;

import android.content.Context;
import android.location.Geocoder;
import android.location.Location;

import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.google.android.gms.maps.model.LatLng;
import com.t.findlocal.activities.SearchActivity;
import com.t.findlocal.models.Business;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.*;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyByte;
import static org.mockito.Matchers.anyCollection;
import static org.mockito.Matchers.anyFloat;
import static org.mockito.Matchers.anyObject;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.anyVararg;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class SearchActivityUnitTests {
//    private Context context = ApplicationProvider.getApplicationContext();

    @Mock
    Context mockContext;

    @Mock
    private SearchActivity searchActivity = new SearchActivity();


    @Test
    public void testGetLatLng(){
        Geocoder geocoder = new Geocoder(mockContext);
        LatLng latLng = SearchActivity.getLatLng("barngate close, LE4 3gf", mockContext.getApplicationContext());
        System.out.println("Lat: " + latLng.latitude);
        System.out.println("Lng: " + latLng.longitude);
    }

    @Test
    public void testGetDistanceInMiles() {
        String miles = SearchActivity.getDistanceInMiles(50000);
        System.out.println(miles);
        assertEquals(miles, "31.07");
    }

    @Test
    public void testGetDistanceBetween() {
        SearchActivity searchActivity = new SearchActivity();
        final double testLeicesterLatitude = 52.636879;
        final double testLeicesterLongitude = -1.139759;
        float distanceBetween = searchActivity.getDistanceBetween(testLeicesterLatitude, testLeicesterLongitude, new LatLng(52.770771, -1.204350));
        System.out.println(distanceBetween);
        assertTrue(distanceBetween == 18668.39);
    }

    @Test
    public void testCreateBusinessInstance() {

        Map<String, Object> businessFromQuery = new HashMap<>();
        businessFromQuery.put("name", "Test Name");
        businessFromQuery.put("description", "Test Description");
        businessFromQuery.put("address", "Belgrave Road, Leicester");
        businessFromQuery.put("longDescription", "Test Long Description");
        businessFromQuery.put("phoneNumber", "123456879");
        businessFromQuery.put("category", "Test Category");
        businessFromQuery.put("website", "Test Website");
        businessFromQuery.put("email", "Test Email");

        LatLng latLng = new LatLng(52.770771, -1.204350);
        String distanceInMiles = "31.07";

        Business business = new Business("Test Id", "Test Name", "Test Description", "Belgrave Road, Leicester",
                "Test Long Description", "123456879", latLng, distanceInMiles,
                "Test Category");
        business.setWebsite("Test Website");
        business.setEmail("Test Email");

//        Business testBusiness = SearchActivity.createBusinessInstance(businessFromQuery, latLng, distanceInMiles);
//
//        assertEquals(testBusiness.getName(), business.getName());
//        assertEquals(testBusiness.getWebsite(), business.getWebsite());
//        assertEquals(testBusiness.getEmail(), business.getEmail());
    }
}
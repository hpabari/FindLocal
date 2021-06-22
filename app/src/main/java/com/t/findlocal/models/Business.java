package com.t.findlocal.models;

import android.graphics.Bitmap;
import android.os.Parcelable;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;

public class Business{
    private String id;
    private String name;
    private String description;
    private String address;
    private String longDescription;
    private String email;
    private String phoneNumber;
    private String website;
    private ArrayList<String> services;
    private LatLng distance;
    private String distanceInMiles;
    private String category;
    private Bitmap image;

    public Business(String id,
                    String name,
                    String description,
                    String address,
                    String longDescription,
                    String phoneNumber,
                    LatLng distance,
                    String distanceInMiles,
                    String category) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.address = address;
        this.longDescription = longDescription;
        this.phoneNumber = phoneNumber;
        this.distance = distance;
        this.distanceInMiles = distanceInMiles;
        this.category = category;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getLongDescription() {
        return longDescription;
    }

    public void setLongDescription(String longDescription) {
        this.longDescription = longDescription;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public ArrayList<String> getServices() {
        return services;
    }

    public void setServices(ArrayList<String> services) {
        this.services = services;
    }

    public LatLng getDistance() {
        return distance;
    }

    public void setDistance(LatLng distance) {
        this.distance = distance;
    }

    public String getDistanceInMiles() {
        return distanceInMiles;
    }

    public void setDistanceInMiles(String distanceInMiles) {
        this.distanceInMiles = distanceInMiles;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public Bitmap getImage() {
        return image;
    }

    public void setImage(Bitmap image) {
        this.image = image;
    }
}



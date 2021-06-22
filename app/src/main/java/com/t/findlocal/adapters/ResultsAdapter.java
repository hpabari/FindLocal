package com.t.findlocal.adapters;

import android.app.Activity;
import android.provider.ContactsContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.t.findlocal.models.Business;
import com.t.findlocal.R;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

// Array Adapter provides the layout for the list of businesses
public class ResultsAdapter extends ArrayAdapter<Business> {

    // https://classroom.udacity.com/courses/ud839/lessons/7709673667/concepts/30b33d8b-2a90-4d39-805f-7975bbc0bcd1
    // https://github.com/udacity/ud839_CustomAdapter_Example
    // Business list is the data that is populated into the ListView
    public ResultsAdapter(Activity context, ArrayList<Business> businesses) {
        // Initialize the ArrayAdapter's internal storage for the context and the list.
        // This is a custom adapter for an image and 3 TextViews so the adapter is not
        // going to use this second argument, so it can be any value.
        super(context, 0, businesses);
    }

    // assigns data to each view in the row
    @NonNull
    @Override
    public View getView(int positionInBusinessArrayList, @Nullable View viewToChange, @NonNull ViewGroup viewGroupForListView) {
        View resultsListItemView = viewToChange;
        if (resultsListItemView == null) {
            resultsListItemView = LayoutInflater.from(getContext()).inflate(R.layout.list_item_search_result, viewGroupForListView, false);
        }

        // business at the current position in the list
        Business currentBusiness = getItem(positionInBusinessArrayList);

        // get each view in the list_item_search_result.xmlult.xml and set them with the current business variables
        TextView busNameTextView = (TextView) resultsListItemView.findViewById(R.id.business_name_text_view);
        busNameTextView.setText(currentBusiness.getName());

        TextView busDescription = (TextView) resultsListItemView.findViewById(R.id.description_text_view);
        busDescription.setText(currentBusiness.getDescription());

        TextView busAddress = (TextView) resultsListItemView.findViewById(R.id.distance_text_view);
        busAddress.setText(currentBusiness.getDistanceInMiles() + " miles");

        ImageView busMainImage = (ImageView) resultsListItemView.findViewById(R.id.image_view);
        busMainImage.setImageBitmap(currentBusiness.getImage());

        return resultsListItemView;
    }
}

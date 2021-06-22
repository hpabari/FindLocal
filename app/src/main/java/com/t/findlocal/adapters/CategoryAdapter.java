package com.t.findlocal.adapters;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.t.findlocal.models.Category;
import com.t.findlocal.R;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class CategoryAdapter extends ArrayAdapter<Category> {

    public CategoryAdapter(Activity context, ArrayList<Category> categories) {
        super(context, 0, categories);
    }

    @NonNull
    @Override
    public View getView(int positionInCategoryArrayList, @Nullable View viewToChange, @NonNull ViewGroup viewGroupForListView) {
        View categoryListItemView = viewToChange;
        if (categoryListItemView == null) {
            categoryListItemView = LayoutInflater.from(getContext()).inflate(R.layout.list_item_categories, viewGroupForListView, false);
        }

        Category currentCategory = getItem(positionInCategoryArrayList);

        TextView catNameTextView = (TextView) categoryListItemView.findViewById(R.id.category_name_text_view);
        catNameTextView.setText(currentCategory.getName());

        ImageView catImageView = (ImageView) categoryListItemView.findViewById(R.id.category_icon);
//        catImageView.setImageResource(currentCategory.getImageId());
        catImageView.setImageBitmap(currentCategory.getImageBitmap());

        return categoryListItemView;
    }
}

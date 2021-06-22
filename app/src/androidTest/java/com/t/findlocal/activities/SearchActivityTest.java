package com.t.findlocal.activities;

import android.view.View;
import android.widget.ListView;

import com.t.findlocal.R;

import org.hamcrest.Matcher;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import androidx.test.espresso.NoMatchingViewException;
import androidx.test.espresso.ViewAssertion;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.rule.ActivityTestRule;

import static androidx.test.espresso.Espresso.*;
import static androidx.test.espresso.action.ViewActions.*;
import static androidx.test.espresso.matcher.ViewMatchers.*;
import static org.hamcrest.Matchers.*;

@RunWith(AndroidJUnit4.class)
public class  SearchActivityTest {

    private final int NUMBER_OF_WINDOW_CLEANERS = 3;

    @Rule
    public ActivityTestRule<MainActivity> mainActivityTestRule = new ActivityTestRule<MainActivity>(MainActivity.class);

    @Before
    public void openSearchNow(){
        onView(withId(R.id.search_now_button)).perform(click());
        try{
            Thread.sleep(2000);
        }
        catch (InterruptedException e){
            e.printStackTrace();
        }
    }

    @Test
    public void textSearchForBusiness(){
        onView(withId(R.id.search_edit_text)).perform(typeText("window"));

        onView(withId(R.id.search_button)).perform(click());

        onView(withId(R.id.results_list_view))
                .check(new ListViewItemCountAssertion(NUMBER_OF_WINDOW_CLEANERS));
    }

    @Test
    public void testCategories(){
        Matcher<View> matcher = allOf(withText("Categories"), isDescendantOfA(withId(R.id.viewpager)));
        onView(matcher).perform(click());

        onData(anything()).inAdapterView(withId(R.id.categories_list_view)).atPosition(7).perform(click());

        onView(withId(R.id.category_results_list_view)).check(new ListViewItemCountAssertion(NUMBER_OF_WINDOW_CLEANERS));
    }
}

//https://stackoverflow.com/questions/36399787/how-to-count-recyclerview-items-with-espresso
class ListViewItemCountAssertion implements ViewAssertion {
    private final int expectedCount;

    public ListViewItemCountAssertion(int expectedCount) {
        this.expectedCount = expectedCount;
    }

    @Override
    public void check(View view, NoMatchingViewException noViewFoundException) {
        if (noViewFoundException != null) {
            throw noViewFoundException;
        }

        ListView listView = (ListView) view;
        int count = listView.getAdapter().getCount();
        assertThat(count, is(expectedCount));
    }
}
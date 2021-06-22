package com.t.findlocal.activities;

import com.t.findlocal.R;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.rule.ActivityTestRule;

import static androidx.test.espresso.Espresso.onData;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.anything;

@RunWith(AndroidJUnit4.class)
public class DetailsActivityTest {
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
    public void openDetailsScreenForBusiness(){
        onView(withId(R.id.search_edit_text)).perform(typeText("accountant"));

        onView(withId(R.id.search_button)).perform(click());

        try{
            Thread.sleep(1000);
        }
        catch (InterruptedException e){
            e.printStackTrace();
        }

        onData(anything()).inAdapterView(withId(R.id.results_list_view)).atPosition(0).perform(click());

        onView(withId(R.id.bus_name)).check(matches(withText("Pabmans")));
        onView(withId(R.id.address)).check(matches(withText("Barngate Close, LE4 3GF")));
        onView(withId(R.id.website)).perform(click());
    }
}

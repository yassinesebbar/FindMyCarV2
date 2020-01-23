package UnitTests;

import android.os.Build;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;
import androidx.test.uiautomator.UiDevice;


import com.example.findmycarv2.GoToDialog;
import com.example.findmycarv2.MapsActivity;
import com.example.findmycarv2.R;
import com.example.findmycarv2.SaveLocationDialog;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import junit.framework.TestCase;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */


@RunWith(AndroidJUnit4.class)

public class UiTesting extends TestCase  {

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        UiDevice device = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation());
    }

//

    @Rule
    public ActivityTestRule<MapsActivity> activityRule =
            new ActivityTestRule<>(MapsActivity.class);

    @Test
    public void testMapIsDisplayed() {
        onView(withId(R.id.map)).check(matches(isDisplayed()));
    }



    @Test
    public void testGoToDialogIsDisplayed() throws InterruptedException {

        GoToDialog goToDialog = new GoToDialog();

        Bundle data = new Bundle();//create bundle instance
        data.putString("street", "Kruisherenborch 24");
        data.putString("dateTime", "23-01-2020 09:50");
        data.putString("imageUrl", "");

        goToDialog.setArguments(data);
        goToDialog.show(activityRule.getActivity().getSupportFragmentManager(), "goTo dialog");

        Fragment prev = activityRule.getActivity().getSupportFragmentManager().findFragmentByTag("goTo dialog");

        Boolean fragmentAlive = false;

        if(prev != null){
            fragmentAlive = true;
        }

        assertTrue(fragmentAlive);
    }

    @Test
    public void testSaveLocationIsDisplayed() throws InterruptedException {

        SaveLocationDialog saveLocationDialog = new SaveLocationDialog();

        Bundle data = new Bundle();//create bundle instance
        data.putString("street", "Kruisherenborch 24");

        saveLocationDialog.setArguments(data);
        saveLocationDialog.show(activityRule.getActivity().getSupportFragmentManager(), "saveLocation dialog");

        Fragment prev = activityRule.getActivity().getSupportFragmentManager().findFragmentByTag("saveLocation dialog");

        Boolean fragmentAlive = false;

        if(prev != null){
            fragmentAlive = true;
        }

        assertTrue(fragmentAlive);
    }
}
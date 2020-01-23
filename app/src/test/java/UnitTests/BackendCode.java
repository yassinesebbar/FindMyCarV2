package UnitTests;


import androidx.fragment.app.FragmentActivity;

import com.example.findmycarv2.CarLocation;
import com.example.findmycarv2.DatabaseHandler;
import com.example.findmycarv2.GoToDialog;
import com.example.findmycarv2.SaveLocationDialog;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import static org.junit.Assert.*;

@RunWith(JUnit4.class)

public class BackendCode extends FragmentActivity implements SaveLocationDialog.SaveLocationDialogListener {

    DatabaseHandler databaseHandler;
    CarLocation carLocation;
    GoToDialog goToDialog;
    SaveLocationDialog saveLocationDialog;

    @Before
    public void dbSetup(){
         databaseHandler = DatabaseHandler.getInstance(this);
    }

    @Test
    public void testGetDatabaseObjectPositive(){
        dbSetup();
        assertNotNull(databaseHandler);
    }

    @Before
    public void setCarLocation(){

        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yy HH:mm", Locale.getDefault());
        String currentDateandTime = sdf.format(new Date());

        carLocation = new CarLocation.Builder()
                .setTimeLocation(currentDateandTime)
                .setLocationInfo("51.5843529", "4.7898239")
                .setAddress("Lovensdijkstraat 61 - 63, 4818 AJ Breda")
                .setImagePath("folder/my images/210120201650")
                .build();
    }

    @Test
    public void testGetCarLocationPositive(){
        setCarLocation();
        assertNotNull(carLocation);

        // Check locationInfo lat
        assertEquals("51.5843529", carLocation.getLat());

        // check locationInfo long
        assertEquals("4.7898239", carLocation.getLon());

        // check Address
        assertEquals("Lovensdijkstraat 61 - 63, 4818 AJ Breda", carLocation.getStreet());

        // check imagePath
        assertEquals("folder/my images/210120201650", carLocation.getImagePath());

    }

    @Override
    public void saveLocation(String pathTofile) {

    }


    Thread thread = new Thread() {
        @Override
        public void run() {
            saveLocationDialog = new SaveLocationDialog();
            saveLocationDialog.show(getSupportFragmentManager(), "Test");
        }
    };

    @Test
    public void testSaveLocationDialog(){

        thread.start();
        assertTrue(saveLocationDialog.isVisible());

    }

    public void testGoToObserver(){

    }

}

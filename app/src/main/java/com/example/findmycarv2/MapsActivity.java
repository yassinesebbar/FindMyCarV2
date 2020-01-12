package com.example.findmycarv2;

import android.Manifest;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;

import com.example.findmycarv2.directionhelpers.FetchURL;
import com.example.findmycarv2.directionhelpers.TaskLoadedCallback;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import static android.content.pm.PackageManager.PERMISSION_GRANTED;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, GoogleMap.OnMyLocationButtonClickListener, GoogleMap.OnMyLocationClickListener, GoogleMap.OnMapClickListener, GoogleMap.OnMarkerClickListener, SaveLocationDialog.SaveLocationDialogListener, TaskLoadedCallback, GoToDialog.GoToDialogListener {

    private GoogleMap mMap;
    private Location currentLocation;
    private FusedLocationProviderClient fusedLocationProviderClient;
    private DatabaseHandler databaseHandler;
    private static final int REQUEST_CODE = 101;
    private Geocoder geocoder;
    private CarLocation[] carLocations;
    private List<Address> addres;
    private Location currentCarLocation;
    private Polyline currentPolyline;
    private LatLng goToLocation;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);


        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        fetchLastLocation();
        databaseHandler = DatabaseHandler.getInstance(this);
      //  databaseHandler.clearDatabase();
    //   databaseHandler.insertDummyData();

        geocoder = new Geocoder(this, Locale.getDefault());

    }

    private void fetchLastLocation() {

        if(PERMISSION_GRANTED != ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)){
            ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_CODE);
            return;
        }

        Task<Location> task = fusedLocationProviderClient.getLastLocation();

        task.addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {

                if(location != null){
                    currentLocation = location;
                    Toast.makeText(getApplicationContext(), currentLocation.getLatitude() + " " + currentLocation.getLongitude(), Toast.LENGTH_SHORT).show();
                    SupportMapFragment supportMapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
                    supportMapFragment.getMapAsync(MapsActivity.this);
                }

            }
        });

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
        createMap();
        mMap.setOnMarkerClickListener(this);

        HistoryLocationsMarkers();
    }

    private void createMap() {
        mMap.setMyLocationEnabled(true);
        mMap.setOnMyLocationButtonClickListener(this);
        mMap.setOnMyLocationClickListener(this);
        mMap.setOnMapClickListener(this);
        UiSettings mapsUisettings = mMap.getUiSettings();
        mapsUisettings.setMapToolbarEnabled(true);
        mapsUisettings.setZoomControlsEnabled(true);
        mapsUisettings.setCompassEnabled(true);

        if(currentLocation != null){
            LatLng clocation = new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude());
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(clocation, 10));
        }


//        mMap.moveCamera();

    }

    private void HistoryLocationsMarkers(){
        //         Add a marker in Sydney and move the camera
        carLocations = databaseHandler.retrieveData();


        for(int i = 0; i < carLocations.length; i++){
            LatLng HistoryLocation = new LatLng(Double.parseDouble(carLocations[i].getLat()), Double.parseDouble(carLocations[i].getLon()));

            if(i != (carLocations.length - 1)){
                mMap.addMarker(new MarkerOptions().position(HistoryLocation).title(carLocations[i].getStreet())).setTag(carLocations[i]);
            }else{
                mMap.addMarker(new MarkerOptions().position(HistoryLocation).title(carLocations[i].getStreet()).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN))).setTag(carLocations[i]);
            }

            Log.i("photo", carLocations[i].getImagePath());
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch(requestCode){
            case REQUEST_CODE:
                if (grantResults.length > 0 &&  grantResults[0] == PERMISSION_GRANTED  ){
                    fetchLastLocation();
                }
                break;
        }
    }

    @Override
    public boolean onMyLocationButtonClick() {
        Toast.makeText(this, "Hier ben je", Toast.LENGTH_SHORT).show();
        // Return false so that we don't consume the event and the default behavior still occurs
        // (the camera animates to the user's current position).
        return false;
    }

    @Override
    public void onMyLocationClick(@NonNull Location location) {
        try {

            currentCarLocation = location;
            addres = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5

            if(!addres.isEmpty()){
                openSaveLocationDialog();
            }


        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    @Override
    public void onMapClick(LatLng latLng) {
//        Marker marker = mMap.addMarker(new MarkerOptions()
//                .position(latLng)
//                .title("Hier heb je geklikt lul")
//        );
//        marker.setTag(0);
    }

    private void openSaveLocationDialog(){

        SaveLocationDialog saveLocationDialog = new SaveLocationDialog();

        Bundle data = new Bundle();//create bundle instance
        data.putString("street", addres.get(0).getAddressLine(0));

        saveLocationDialog.setArguments(data);
        saveLocationDialog.show(getSupportFragmentManager(), "saveLocation dialog");

    }

    private void openGoToDialog(CarLocation carLocation){
        GoToDialog goToDialog = new GoToDialog();

        Bundle data = new Bundle();//create bundle instance
        data.putString("street", carLocation.getStreet());
        data.putString("dateTime", carLocation.getDateTime());
        data.putString("imageUrl", carLocation.getImagePath());

        goToDialog.setArguments(data);
        goToDialog.show(getSupportFragmentManager(), "goTo dialog");
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        CarLocation carLocationMarker = (CarLocation) marker.getTag();

        goToLocation = marker.getPosition();

        openGoToDialog(carLocationMarker);
        return true;
    }


    @Override
    public void saveLocation(String pathTofile) {
        String addressLine = addres.get(0).getAddressLine(0);
        String latitude = Double.toString(currentCarLocation.getLatitude());
        String longitude = Double.toString(currentCarLocation.getLongitude());

        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yy HH:mm", Locale.getDefault());
        String currentDateandTime = sdf.format(new Date());
        databaseHandler.insertData(latitude, longitude, addressLine,currentDateandTime,pathTofile);
        mMap.clear();

        HistoryLocationsMarkers();
    }

    private String getUrl(Location origin, LatLng dest, String directionMode) {
        // Origin of route
        String str_origin = "origin=" + origin.getLatitude() + "," + origin.getLongitude();
        // Destination of route
        String str_dest = "destination=" + dest.latitude + "," + dest.longitude;
        // Mode
        String mode = "mode=" + directionMode;
        // Building the parameters to the web service
        String parameters = str_origin + "&" + str_dest + "&" + mode;
        // Output format
        String output = "json";
        // Building the url to the web service
        String url = "https://maps.googleapis.com/maps/api/directions/" + output + "?" + parameters + "&key=" + getString(R.string.google_maps_key);
        return url;
    }

    @Override
    public void onTaskDone(Object... values) {
        if (currentPolyline != null)
            currentPolyline.remove();
            currentPolyline = mMap.addPolyline((PolylineOptions) values[0]);
    }

    @Override
    public void GoToLocation() {

        Task<Location> task = fusedLocationProviderClient.getLastLocation();

        task.addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {

                if(location != null){
                    currentLocation = location;
                }

            }
        });

        if(currentLocation != null && goToLocation != null){
            new FetchURL(this).execute(getUrl(currentLocation, goToLocation, "walking"), "walking");
        }
    }
}


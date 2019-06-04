package com.example.alannalucas.travelfyp;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.Typeface;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.libraries.places.api.Places;
/*import com.google.android.gms.location.places.ui.PlacePicker;

import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;*/

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.Api;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.model.RectangularBounds;
import com.google.android.libraries.places.api.model.TypeFilter;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.android.libraries.places.widget.Autocomplete;
import com.google.android.libraries.places.widget.AutocompleteActivity;
import com.google.android.libraries.places.widget.AutocompleteFragment;
import com.google.android.libraries.places.widget.AutocompleteSupportFragment;
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMapOptions;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import java.io.FileDescriptor;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class EventsMap extends AppCompatActivity implements
        OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener {

    private GoogleMap mMap;
    private GoogleApiClient googleApiClient;
    private LocationRequest locationRequest;
    private Location lastLocation;
    private String name, selPlace;
    private double latitude, longitude;
    private Marker currentUserLocationMarker;
    private static final int Request_User_Location_Code = 99;
    private ImageButton mStar, mEvents, mSaved;
    private LatLng selPlaceLatLng;
    private String apiKey = "AIzaSyAiXcwMQY9v2ba4GvxLPsF_G-FPUJA5DUU";
    private BottomNavigationView mBottomNav;
    private String title;
    private FirebaseAuth mAuth;
    private FirebaseDatabase mFirebaseDatabase;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private DatabaseReference saveLocations, viewLocations;

    private int ProximityRadius = 10000;

    private static final int PLACE_PICKER_REQUEST = 123;
    private PlacesClient placesClient;
    private TextView responseView;
    //private FieldSelector fieldSelector;
    private RequestQueue mQueue;
    private ArrayList eventsList;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_events_map);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            checkUserLocationPermission();
        }

        Places.initialize(getApplicationContext(), apiKey);

        // Create a new Places client instance.
        placesClient = Places.createClient(this);


        mAuth = FirebaseAuth.getInstance();
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        saveLocations = mFirebaseDatabase.getReference();

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mQueue = Volley.newRequestQueue(this);

        getData();

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        mSaved = (ImageButton) findViewById(R.id.btnSaved);




        mBottomNav = (BottomNavigationView) findViewById(R.id.navigation);
        mBottomNav.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                selectNavigation(item);
                return true;
            }
        });

        mSaved.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mMap.clear();

                FirebaseUser user = mAuth.getCurrentUser();
                String userID = user.getUid();

                Toast.makeText(EventsMap.this, "saved locations...", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(EventsMap.this, NearbyLocations.class);
                startActivity(intent);
                finish();


            }
        });


    }


    public void getData() {

        eventsList = new ArrayList<Event>();

        String url = "https://app.ticketmaster.com/discovery/v2/events.json?countryCode=IE&apikey=OBfaBREyutatiLUjh3XJ5oW51KAQwYCQ&size=30";
        //String url = "https://app.ticketmaster.com/discovery/v2/events.json?countryCode=IE&apikey=pJx9cAKP7Jrcf1SXAFU2ejqXXKFvGTJF&size=30";


        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        String name = "";
                        String ticketsURL = "";
                        String eventDate = "";
                        String eventTime = "";
                        String venue = "";
                        String image = "";
                        double longitude = 0.0;
                        double latitude = 0.0;
                        if (response != null) {
                            try {

                                JSONObject jsonObject = new JSONObject(response);
                                JSONObject jsonObj = jsonObject.getJSONObject("_embedded");

                                //ArrayList<Event> list = new ArrayList<Event>();


                                JSONArray array = jsonObj.getJSONArray("events");
                                // ArrayList<String> venues = new  ArrayList<String>();
                                String name1 = "";
                                String venue1 = "";

                                for (int i = 0; i < array.length(); i++) {
                                    name = array.getJSONObject(i).getString("name");
                                    ticketsURL = array.getJSONObject(i).getString("url");
                                    JSONObject img = (JSONObject) array.get(i);
                                    JSONArray arrayImages = img.getJSONArray("images");
                                    for (int k = 0; k < 1; k++) {
                                        image = arrayImages.getJSONObject(0).getString("url");
                                    }
                                    JSONObject dateTime = array.getJSONObject(i).getJSONObject("dates");
                                    JSONObject arrayDate = dateTime.getJSONObject("start");
                                    eventDate = arrayDate.getString("localDate");
                                    eventTime = arrayDate.getString("localTime");

                                    JSONObject jsonObjEmbedded = array.getJSONObject(i).getJSONObject("_embedded");
                                    JSONArray array2 = jsonObjEmbedded.getJSONArray("venues");
                                    for (int j = 0; j < array2.length(); j++) {
                                        venue = array2.getJSONObject(j).getString("name");
                                        //venues.add(array2.getJSONObject(j).getString("name"));
                                    }

                                    JSONObject location = array2.getJSONObject(0).getJSONObject("location");
                                    latitude = location.getDouble("latitude");
                                    longitude = location.getDouble("longitude");


                                    //Toast.makeText(getApplicationContext(), String.valueOf(i), Toast.LENGTH_LONG ).show();
                                    // name1 = name1 + venues.get(i) +"/" +name;
//                                mTextViewResult.setText(name);
//                                mTextViewVenue.setText(venues.get(i));

                                    eventsList.add(new Event(name, eventDate, venue, latitude, longitude));
                                }
                                //    Toast.makeText(this, eventsList.size(), Toast.LENGTH_LONG ).show();
//
                                openMapEvents(eventsList);
                                // mTextViewResult.setText(list.get(0).getName());

                            } catch (JSONException e) {
                                Toast.makeText(getApplicationContext(), "Error", Toast.LENGTH_LONG).show();
                            }
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }
        });
        mQueue.add(stringRequest);

    }


    public void openMapEvents(ArrayList<Event> eventsList) {

        String eventNames = "";
        String date;
        boolean[] visited = new boolean[eventsList.size()];
        for (int i = 0; i < visited.length; i++)
            visited[i] = false;
        for (int i = 0; i < eventsList.size(); i++) {
            if (!visited[i]) {

                String venueName = eventsList.get(i).getVenue();

                double latitude = eventsList.get(i).getLatitude();
                double longitude = eventsList.get(i).getLongitude();
                LatLng venue = new LatLng(latitude, longitude);
                MarkerOptions marker = new MarkerOptions();

                String events = "";
                date = eventsList.get(i).getDate();
                eventNames = eventsList.get(i).getName() + " " + date;
                events = events + eventNames + "\n";
                visited[i] = true;

//            LatLng venue = new LatLng(latitude, longitude);
//            MarkerOptions marker = new MarkerOptions();

//            marker.position(venue).title(eventsList.get(i).getName());
//            marker.position(venue).snippet(eventsList.get(i).getVenue());
//            mMap.addMarker(marker);


//            Marker venueMarker = new Marker();
//            mMap.setOnMarkerClickListener(venueMarker);

                for (int j = 0; j < eventsList.size(); j++) {

                    if ((i != j) && (!visited[j])) {


                        if (eventsList.get(j).getVenue().equals(venueName)) {
                            date = eventsList.get(j).getDate();
                            events = events + eventsList.get(j).getName() + " " + date + "\n";

                            visited[j] = true;
                            //     marker.position(venue).title(eventNames);
                            //    marker.position(venue).snippet(eventsList.get(j).getVenue());
                            //   mMap.addMarker(marker);
                            //  Marker locationMarker = mMap.addMarker(marker);
                            //  locationMarker.showInfoWindow();
                        }
                        /*else {
                            date = eventsList.get(j).getDate();

                            marker.position(venue).title(eventsList.get(j).getName() + " " + date);
                            marker.position(venue).snippet(eventsList.get(j).getVenue());
                            Marker locationMarker = mMap.addMarker(marker);
                            locationMarker.showInfoWindow();


                        } */
                    }
                }

                marker.position(venue).title(venueName);
                marker.position(venue).snippet(events);
                mMap.addMarker(marker);
                Marker locationMarker = mMap.addMarker(marker);
                locationMarker.showInfoWindow();

            } //end if

        }
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

        //if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION))

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {


            buildGoogleApiClient();

            mMap.setMyLocationEnabled(true);
        }

        FirebaseUser user = mAuth.getCurrentUser();
        String userID = user.getUid();



        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {

                latitude = marker.getPosition().latitude;
                longitude = marker.getPosition().longitude;
                name = marker.getTitle();
                return false;

            }
        });

        mMap.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {

            @Override
            public View getInfoWindow(Marker arg0) {
                return null;
            }

            @Override
            public View getInfoContents(Marker marker) {

                LinearLayout info = new LinearLayout(EventsMap.this);
                info.setOrientation(LinearLayout.VERTICAL);

                TextView title = new TextView(EventsMap.this);
                title.setTextColor(Color.BLACK);
                title.setGravity(Gravity.CENTER);
                title.setTypeface(null, Typeface.BOLD);
                title.setText(marker.getTitle());

                TextView snippet = new TextView(EventsMap.this);
                snippet.setTextColor(Color.GRAY);
                snippet.setText(marker.getSnippet());

                info.addView(title);
                info.addView(snippet);

                return info;
            }
        });


    }




    private void selectNavigation(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.btmHome:
                Intent intent = new Intent(this, AllUsers.class);
                this.startActivity(intent);
                break;

            case R.id.btmLocation:
                Intent intent1 = new Intent(this, NearbyLocations.class);
                this.startActivity(intent1);
                break;

            case R.id.btmProfile:
                Intent intent3 = new Intent(this, ProfilePage.class);
                this.startActivity(intent3);
                break;

        }
    }

    protected synchronized void buildGoogleApiClient() {

        googleApiClient = new GoogleApiClient.Builder(this).addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();

        googleApiClient.connect();



    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1001) {
            if (resultCode == RESULT_OK) {
                Place place = (Place) Autocomplete.getPlaceFromIntent(data);
                Toast.makeText(getApplicationContext(),place.getName()+ ", " + place.getId(), Toast.LENGTH_SHORT).show();
            } else if (resultCode == AutocompleteActivity.RESULT_ERROR) {
                // TODO: Handle the error.
                Status status = Autocomplete.getStatusFromIntent(data);
                Toast.makeText(getApplicationContext(), status.getStatusMessage(), Toast.LENGTH_SHORT).show();
            } else if (resultCode == RESULT_CANCELED) {
                Toast.makeText(getApplicationContext(), "Please select a location", Toast.LENGTH_SHORT).show();
            }
        }

    }





    public boolean checkUserLocationPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION)) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, Request_User_Location_Code);

            } else {
                ActivityCompat.requestPermissions(this, new String[]
                        {Manifest.permission.ACCESS_FINE_LOCATION}, Request_User_Location_Code);

            }
            return false;
        } else {
            return true;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode){
            case Request_User_Location_Code:
                if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    if(ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED){
                        if (googleApiClient == null ){
                            buildGoogleApiClient();
                        }
                        mMap.setMyLocationEnabled(true);
                    }
                }
                else {
                    Toast.makeText(this, "Permission Denied", Toast.LENGTH_SHORT).show();
                }
                return;
        }
    }

    @Override
    public void onLocationChanged(Location location) {

        //latitude = location.getLatitude();
        //longitude = location.getLongitude();

        lastLocation = location;

        if (currentUserLocationMarker != null){
            currentUserLocationMarker.remove();
        }

        LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());


        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(latLng);
        markerOptions.title("User Current Location");
        markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));

        currentUserLocationMarker = mMap.addMarker(markerOptions);

        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
        mMap.animateCamera(CameraUpdateFactory.zoomBy(12));

        if(googleApiClient != null){
            LocationServices.FusedLocationApi.removeLocationUpdates(googleApiClient, this);

        }

    }


    @Override
    public void onConnected(@Nullable Bundle bundle) {

        locationRequest = new LocationRequest();
        locationRequest.setInterval(1100);
        locationRequest.setFastestInterval(1100);
        locationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);

        if(ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

            LocationServices.FusedLocationApi.requestLocationUpdates(googleApiClient, locationRequest, this);
        }
    }


    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.menuLogout:
                FirebaseAuth.getInstance().signOut();
                finish();
                startActivity(new Intent(this, LoginActivity.class));
                break;


            case R.id.menuAllUsers:
                Intent intent1 = new Intent(this, AllUsers.class);
                this.startActivity(intent1);
                break;

        }


        return true;
    }




}
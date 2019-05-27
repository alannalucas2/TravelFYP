package com.example.alannalucas.travelfyp;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
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
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
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
//import com.google.android.gms.location.places.ui.PlaceAutocomplete;
//import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.android.libraries.places.api.Places;

//import com.google.android.gms.location.places.Place;
//import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment;
//import com.google.android.gms.location.places.ui.PlaceSelectionListener;

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
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener;
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

import com.google.android.libraries.places.api.Places;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import java.io.FileDescriptor;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class NearbyLocations extends AppCompatActivity implements
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
    private Marker currentUserLocationMarker, marker;
    private static final int Request_User_Location_Code = 99;
    private ImageButton mStar, mEvents, mSaved;
    private LatLng selPlaceLatLng;
    private String apiKey = "AIzaSyAiXcwMQY9v2ba4GvxLPsF_G-FPUJA5DUU";
    private BottomNavigationView mBottomNav;
    private static final String TAG = "MapsActivity";
    private String searchPlace;

    private static Button btnAlertRating;

    final String currentDate = DateFormat.getDateTimeInstance().format(new Date());

    private FirebaseAuth mAuth;
    private FirebaseDatabase mFirebaseDatabase;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private DatabaseReference saveLocations, mSavedLocations;

    private int ProximityRadius = 10000;
    //private PlaceAutocompleteFragment placeAutocompleteFragment;

    private static final int PLACE_PICKER_REQUEST = 123;
    private PlacesClient placesClient;
    List<Place.Field> placeFields = Arrays.asList(Place.Field.ID, Place.Field.NAME, Place.Field.ADDRESS);
    AutocompleteSupportFragment places_fragment;


    private TextView responseView;
    //private FieldSelector fieldSelector;
    private RequestQueue mQueue;
    private ArrayList eventsList;
    com.getbase.floatingactionbutton.FloatingActionButton fab1, fab2, fab3, fab4, fab5;


    public ArrayList<String> locationlist = new ArrayList<String>();




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nearby_locations);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            checkUserLocationPermission();
        }

        Places.initialize(getApplicationContext(), apiKey);
        
        initPlaces();
        setupPlaceAutocomplete();


        //placeAutocompleteFragment = (PlaceAutocompleteFragment)getSupportFragmentManager().findFragmentById(R.id.place_autocomplete_fragment);

        fab1 = findViewById(R.id.fab_action_hotel);
        fab2 = findViewById(R.id.fab_action_nightclub);
        fab3 = findViewById(R.id.fab_action_restaurant);
        fab4 = findViewById(R.id.fab_action_events);
        fab5 = findViewById(R.id.fab_action_saved);



        fab4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(NearbyLocations.this, "Ticketmaster events...", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(NearbyLocations.this, EventsMap.class);
                startActivity(intent);
                finish();
            }
        });

        fab5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                FirebaseUser user = mAuth.getCurrentUser();
                String userID = user.getUid();


                mSavedLocations.child(userID).child("Locations").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for (DataSnapshot s : dataSnapshot.getChildren()) {

                            LocationInformation locInfo = s.getValue(LocationInformation.class);
                            LatLng location = new LatLng(locInfo.latitude, locInfo.longitude);
                            name = locInfo.name;


                            //Toast.makeText(NearbyLocations.this, "toast1" + s.getKey() + s.getValue(), Toast.LENGTH_LONG).show();
                            locationlist.add(s.getKey());
                            findLocations(s.getKey());

                            mMap.addMarker(new MarkerOptions().position(location).title(name)).setIcon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ROSE));

                            mMap.moveCamera(CameraUpdateFactory.newLatLng(location));
                            mMap.animateCamera(CameraUpdateFactory.zoomBy(12));
                        }
                        //Toast.makeText(NearbyLocations.this, "toast2" + Integer.toString(locationlist.size()), Toast.LENGTH_LONG).show();
                    }


                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                    }
                });
            }
        });


        // Create a new Places client instance.
        placesClient = Places.createClient(this);


        mAuth = FirebaseAuth.getInstance();
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        saveLocations = mFirebaseDatabase.getReference();


        mAuth = FirebaseAuth.getInstance();
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mSavedLocations = mFirebaseDatabase.getReference();
        mSavedLocations.push().setValue(marker);

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    // User is signed in
                    Log.d(TAG, "onAuthStateChanged:signed_in:" + user.getUid());
                    //toastMessage("Successfully signed in with: " + user.getEmail());
                } else {
                    // User is signed out
                    Log.d(TAG, "onAuthStateChanged:signed_out");
                    //toastMessage("Successfully signed out.");
                }
                // ...
            }
        };

        mSavedLocations.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                Object value = dataSnapshot.getValue();
                Log.d(TAG, "Value is: " + value);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });


        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mQueue = Volley.newRequestQueue(this);

        //getData();

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        mStar = (ImageButton) findViewById(R.id.btnStar);
        mEvents = (ImageButton) findViewById(R.id.btnEvents);
        mSaved = (ImageButton) findViewById(R.id.btnSaved);


        mStar.setVisibility(View.GONE);
        //mHeart.setVisibility(View.GONE);


        mBottomNav = (BottomNavigationView) findViewById(R.id.navigation);
        mBottomNav.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                selectNavigation(item);
                return true;
            }
        });

        mStar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                LocationInformation locInfo = new LocationInformation(name, latitude, longitude);


                FirebaseUser user = mAuth.getCurrentUser();
                String userID = user.getUid();
                saveLocations.child("User Locations").child(userID).child(name).setValue(locInfo).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(NearbyLocations.this, "Location saved", Toast.LENGTH_LONG).show();
                        alertRating();
                    }
                })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(NearbyLocations.this, "Error location not saved", Toast.LENGTH_LONG).show();

                            }
                        });


            }
        });

        mEvents.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(NearbyLocations.this, "Ticketmaster events...", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(NearbyLocations.this, EventsMap.class);
                startActivity(intent);
                finish();
            }
        });


    }

    public void alertRating(){

        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(this);
        alertBuilder.setMessage("Rate this location: ")
                .setCancelable(true)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .setNegativeButton("Later", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
        AlertDialog alert = alertBuilder.create();
        alert.setTitle("Rate: ");
        alert.show();

    }


    private void setupPlaceAutocomplete() {

        places_fragment = (AutocompleteSupportFragment)getSupportFragmentManager()
                .findFragmentById(R.id.places_autocomplete_fragment);
        places_fragment.setPlaceFields(placeFields);
        places_fragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(@NonNull Place place) {
                Toast.makeText(NearbyLocations.this, "" + place.getName() , Toast.LENGTH_SHORT).show();



                mMap.addMarker(new MarkerOptions().position(place.getLatLng()).title(place.getName())).setIcon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ROSE));
                mMap.moveCamera(CameraUpdateFactory.newLatLng(place.getLatLng()));
                mMap.animateCamera(CameraUpdateFactory.zoomBy(12));

            }

            @Override
            public void onError(@NonNull Status status) {
                Toast.makeText(NearbyLocations.this, "" + status.getStatusMessage() , Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void initPlaces() {
        Places.initialize(this, getString(R.string.places_api_key));
        placesClient = Places.createClient(this);
    }


    public void findLocations(String friendID) {


        for (int i = 0; i < locationlist.size(); i++) {
            friendID = locationlist.get(i);
            mSavedLocations.child(friendID).child("Locations").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    for (DataSnapshot s : dataSnapshot.getChildren()) {

                        LocationInformation userlocation = s.getValue(LocationInformation.class);
                        LatLng location = new LatLng(userlocation.latitude, userlocation.longitude);

                        //Toast.makeText(FriendMapsActivity.this, Double.toString(userlocation.latitude), Toast.LENGTH_LONG).show();


                        mMap.addMarker(new MarkerOptions().position(location).title(userlocation.name)).setIcon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ROSE));

                        mMap.moveCamera(CameraUpdateFactory.newLatLng(location));
                        mMap.animateCamera(CameraUpdateFactory.zoomBy(12));
                    }
                }


                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
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

        mMap.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {

            @Override
            public View getInfoWindow(Marker arg0) {
                return null;
            }

            @Override
            public View getInfoContents(Marker marker) {

                LinearLayout info = new LinearLayout(NearbyLocations.this);
                info.setOrientation(LinearLayout.VERTICAL);

                TextView title = new TextView(NearbyLocations.this);
                title.setTextColor(Color.BLACK);
                title.setGravity(Gravity.CENTER);
                title.setTypeface(null, Typeface.BOLD);
                title.setText(marker.getTitle());

                TextView snippet = new TextView(NearbyLocations.this);
                snippet.setTextColor(Color.GRAY);
                snippet.setText(marker.getSnippet());

                info.addView(title);
                info.addView(snippet);

                return info;
            }
        });


        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {

                mStar.setVisibility(View.VISIBLE);

                latitude = marker.getPosition().latitude;
                longitude = marker.getPosition().longitude;
                name = marker.getTitle();

                Toast.makeText(NearbyLocations.this, name, Toast.LENGTH_SHORT).show();
                return false;


            }
        });


    }




    private void selectNavigation(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.btmHome:
                Intent intent = new Intent(this, MainActivity.class);
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

    public void FABclick(View v){
        String hotel = "hotel";
        String night_club = "night_club";
        String restaurant = "restaurant";
        Object transferData[] = new Object[2];
        GetNearbyPlaces getNearbyPlaces = new GetNearbyPlaces();

        switch (v.getId())
        {

            case R.id.fab_action_hotel:
                mMap.clear();
                String url = getUrl(latitude, longitude, hotel);
                transferData[0] = mMap;
                transferData[1] = url;

                getNearbyPlaces.execute(transferData);
                Toast.makeText(this, "Showing Nearby hotels...", Toast.LENGTH_SHORT).show();
                break;


            case R.id.fab_action_nightclub:
                mMap.clear();
                url = getUrl(latitude, longitude, night_club);
                transferData[0] = mMap;
                transferData[1] = url;

                getNearbyPlaces.execute(transferData);

                Toast.makeText(this, "Showing Nearby Night Clubs...", Toast.LENGTH_SHORT).show();
                break;


            case R.id.fab_action_restaurant:
                mMap.clear();
                url = getUrl(latitude, longitude, restaurant);
                transferData[0] = mMap;
                transferData[1] = url;

                getNearbyPlaces.execute(transferData);

                Toast.makeText(this, "Showing Nearby Restaurants...", Toast.LENGTH_SHORT).show();
                break;
        }


    }

    public void onClick(View v)
    {
        String hotel = "hotel";
        String night_club = "night_club";
        String restaurant = "restaurant";
        Object transferData[] = new Object[2];
        GetNearbyPlaces getNearbyPlaces = new GetNearbyPlaces();

        switch (v.getId())
        {
            /*case R.id.search_address:
                EditText addressField = (EditText) findViewById(R.id.location_search);
                String address = addressField.getText().toString();

                List<Address> addressList = null;
                MarkerOptions userMarkerOptions = new MarkerOptions();

                if (!TextUtils.isEmpty(address))
                {
                    Geocoder geocoder = new Geocoder(this);

                    try
                    {
                        addressList = geocoder.getFromLocationName(address, 6);

                        if (addressList != null)
                        {
                            for (int i=0; i<addressList.size(); i++)
                            {
                                Address userAddress = addressList.get(i);
                                LatLng latLng = new LatLng(userAddress.getLatitude(), userAddress.getLongitude());

                                userMarkerOptions.position(latLng);
                                userMarkerOptions.title(address);
                                userMarkerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE));
                                mMap.addMarker(userMarkerOptions);
                                mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
                                mMap.animateCamera(CameraUpdateFactory.zoomTo(10));
                            }
                        }
                        else
                        {
                            Toast.makeText(this, "Location not found...", Toast.LENGTH_SHORT).show();
                        }
                    }
                    catch (IOException e)
                    {
                        e.printStackTrace();
                    }
                }
                else
                {
                    Toast.makeText(this, "please write any location name...", Toast.LENGTH_SHORT).show();
                }
                break;*/


            case R.id.hotels_nearby:
                mMap.clear();
                String url = getUrl(latitude, longitude, hotel);
                transferData[0] = mMap;
                transferData[1] = url;

                getNearbyPlaces.execute(transferData);
                Toast.makeText(this, "Showing Nearby hotels...", Toast.LENGTH_SHORT).show();
                break;


            case R.id.clubs_nearby:
            mMap.clear();
            url = getUrl(latitude, longitude, night_club);
            transferData[0] = mMap;
            transferData[1] = url;

            getNearbyPlaces.execute(transferData);

            Toast.makeText(this, "Showing Nearby Night Clubs...", Toast.LENGTH_SHORT).show();
            break;

            /*case R.id.btnEvents:
                mMap.clear();
                url = getUrl(latitude, longitude, night_club);
                transferData[0] = mMap;
                transferData[1] = url;

                getNearbyPlaces.execute(transferData);

                Toast.makeText(this, "Showing Nearby Night Clubs...", Toast.LENGTH_SHORT).show();
                break;*/


            case R.id.restaurants_nearby:
                mMap.clear();
                url = getUrl(latitude, longitude, restaurant);
                transferData[0] = mMap;
                transferData[1] = url;

                getNearbyPlaces.execute(transferData);

                Toast.makeText(this, "Showing Nearby Restaurants...", Toast.LENGTH_SHORT).show();
                break;
        }


        }



    private String getUrl(double latitude, double longitude, String nearbyPlace)
    {
        StringBuilder googleURL = new StringBuilder("https://maps.googleapis.com/maps/api/place/nearbysearch/json?");
        googleURL.append("location=" + latitude + "," + longitude);
        googleURL.append("&radius=" + ProximityRadius);
        googleURL.append("&type=" + nearbyPlace);
        googleURL.append("&sensor=true");
        googleURL.append("&key=" + "AIzaSyAb0rD1B23s0hTEBwBuZ5oJjs4jPPp6IQA");

        Log.d("NearbyLocations", "url = " + googleURL.toString());

        return googleURL.toString();
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

            case R.id.menuProfile:
                Intent intent = new Intent(this, ProfileActivity.class);
                this.startActivity(intent);
                break;

            case R.id.menuAllUsers:
                Intent intent1 = new Intent(this, AllUsers.class);
                this.startActivity(intent1);
                break;

            case R.id.accountDetails:
                Intent intent3 = new Intent(this, UpdateProfile.class);
                this.startActivity(intent3);
                break;

            case R.id.menuLocation:
                Intent intent2 = new Intent(this, MapsActivity.class);
                this.startActivity(intent2);
                break;

        }


        return true;
    }




}
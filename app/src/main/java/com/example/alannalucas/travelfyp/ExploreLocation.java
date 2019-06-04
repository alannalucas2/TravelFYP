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
import android.media.Rating;
import android.net.Uri;
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
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RatingBar;
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
import org.w3c.dom.Text;

import java.io.IOException;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import java.io.FileDescriptor;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class ExploreLocation extends AppCompatActivity implements
        OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener {

    private GoogleMap mMap;
    private GoogleApiClient googleApiClient;
    private LocationRequest locationRequest;
    private Location lastLocation;
    private String name, selPlace, placeID, time, address;
    private double latitude, longitude;
    private Marker currentUserLocationMarker, marker;
    private static final int Request_User_Location_Code = 99;
    private ImageButton mHeart, mEvents, mSaved;
    private LatLng selPlaceLatLng;
    private String apiKey = "AIzaSyAiXcwMQY9v2ba4GvxLPsF_G-FPUJA5DUU";
    private BottomNavigationView mBottomNav;
    private static final String TAG = "MapsActivity";


    private String searchedPlace, mUsername;
    private RatingBar mRatingBar;
    private TextView ratingDisplay;
    private float rateValue, rating;


    //private static Button search;

    final String currentDate = DateFormat.getDateTimeInstance().format(new Date());

    private FirebaseAuth mAuth;
    private FirebaseDatabase mFirebaseDatabase;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private DatabaseReference saveLocations, mSavedLocations, saveRating, userReference, saveRating1;

    private int ProximityRadius = 2500;
    //private PlaceAutocompleteFragment placeAutocompleteFragment;

    private static final int PLACE_PICKER_REQUEST = 123;
    private PlacesClient placesClient;
    List<Place.Field> placeFields = Arrays.asList(Place.Field.ID, Place.Field.NAME, Place.Field.ADDRESS);
    AutocompleteSupportFragment places_fragment;


    private String username;
    private double passLat, passLong;
    private TextView responseView;
    //private FieldSelector fieldSelector;
    private RequestQueue mQueue;
    private ArrayList eventsList;
    com.getbase.floatingactionbutton.FloatingActionButton fab1, fab2, fab3, fab4, fab5, fab6, manualEnter;


    public ArrayList<String> locationlist = new ArrayList<String>();




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nearby_locations);

        //FirebaseUser user = mAuth.getCurrentUser();
        //String userID = user.getUid();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            checkUserLocationPermission();
        }

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


        username = getIntent().getStringExtra("username");
        passLat = Double.parseDouble(getIntent().getStringExtra("latitude"));
        passLong = Double.parseDouble(getIntent().getStringExtra("longitde"));




        Places.initialize(getApplicationContext(), apiKey);

        initPlaces();
        //setupPlaceAutocomplete();
        //RatingBar mRatingBar = (RatingBar) findViewById(R.id.ratingBar);
        //TextView ratingDisplay = (TextView) findViewById(R.id.displayRating);


        //events
        mQueue = Volley.newRequestQueue(this);
        //getData();
        Places.initialize(getApplicationContext(), apiKey);
        // Create a new Places client instance.
        placesClient = Places.createClient(this);


        //placeAutocompleteFragment = (PlaceAutocompleteFragment)getSupportFragmentManager().findFragmentById(R.id.place_autocomplete_fragment);

        fab1 = findViewById(R.id.fab_action_hotel);
        fab2 = findViewById(R.id.fab_action_nightclub);
        fab3 = findViewById(R.id.fab_action_restaurant);
        fab4 = findViewById(R.id.fab_action_events);
        fab5 = findViewById(R.id.fab_action_saved);
        fab6 = findViewById(R.id.fab_action_clear);
        manualEnter = findViewById(R.id.btnManualEnter);
        //search = (Button)findViewById(R.id.search_address);


        //Intent tmasterIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.google.com"));
        //                        startActivity(tmasterIntent);

        fab4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getData();

                mMap.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {
                    @Override
                    public void onMapLongClick(LatLng latLng) {
                        Toast.makeText(ExploreLocation.this, "long click" , Toast.LENGTH_LONG).show();

                        alertWebsiteIntent();

                    }
                });

                /*Toast.makeText(NearbyLocations.this, "Ticketmaster events...", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(NearbyLocations.this, EventsMap.class);
                startActivity(intent);
                finish();*/
            }
        });

        fab6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mMap.clear();

                if (ContextCompat.checkSelfPermission(ExploreLocation.this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {


                    buildGoogleApiClient();

                    mMap.setMyLocationEnabled(true);
                }


            }
        });

        fab5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                FirebaseUser user = mAuth.getCurrentUser();
                String userID = user.getUid();


                mSavedLocations.child("User Locations").child(userID).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for (DataSnapshot s : dataSnapshot.getChildren()) {

                            LocationInformation locInfo = s.getValue(LocationInformation.class);
                            LatLng location = new LatLng(locInfo.latitude, locInfo.longitude);
                            name = locInfo.name;


                            //Toast.makeText(NearbyLocations.this, "toast1" + s.getKey() + s.getValue(), Toast.LENGTH_LONG).show();
                            locationlist.add(s.getKey());
                            findLocations(s.getKey());

                            mMap.addMarker(new MarkerOptions().position(location).title(name)).setIcon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_VIOLET));



                            mMap.moveCamera(CameraUpdateFactory.newLatLng(location));
                            //mMap.animateCamera(CameraUpdateFactory.zoomBy(12));
                        }
                        //Toast.makeText(NearbyLocations.this, "toast2" + Integer.toString(locationlist.size()), Toast.LENGTH_LONG).show();
                    }


                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                    }
                });
            }
        });

        manualEnter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertManualEnter();
            }
        });


        // Create a new Places client instance.
        placesClient = Places.createClient(this);


        mAuth = FirebaseAuth.getInstance();
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        saveLocations = mFirebaseDatabase.getReference();
        saveRating = mFirebaseDatabase.getReference();


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


        FirebaseUser user = mAuth.getCurrentUser();
        String userID = user.getUid();
        userReference = FirebaseDatabase.getInstance().getReference().child("Users").child(userID);
        userReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    Users users = dataSnapshot.getValue(Users.class);

                    mUsername = users.getUsername();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //getSupportActionBar().setTitle("    AlannaLou's Map");

        mQueue = Volley.newRequestQueue(this);

        //getData();

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        mHeart = (ImageButton) findViewById(R.id.btnSave);
        //mSaved = (ImageButton) findViewById(R.id.btnSaved);

        //rateBar = (RatingBar) findViewById(R.id.ratingBar);


        mHeart.setVisibility(View.GONE);
        manualEnter.setVisibility(View.GONE);
        //mHeart.setVisibility(View.GONE);


        mBottomNav = (BottomNavigationView) findViewById(R.id.navigation);
        mBottomNav.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                selectNavigation(item);
                return true;
            }
        });

        mHeart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                LocationInformation locInfo = new LocationInformation(name, latitude, longitude, rating, placeID, time, address, mUsername);



                FirebaseUser user = mAuth.getCurrentUser();
                String userID = user.getUid();
                saveLocations.child("AllUsersLocations").child(name).setValue(locInfo);
                saveLocations.child("User Locations").child(userID).child(name).setValue(locInfo).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(ExploreLocation.this, "Location saved", Toast.LENGTH_LONG).show();
                        // mRatingBar = (RatingBar) findViewById(R.id.ratingBar);
                        alertRating();
                    }
                })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(ExploreLocation.this, "Error location not saved", Toast.LENGTH_LONG).show();

                            }
                        });


            }
        });


    }



    public void alertManualEnter(){

        final View v = LayoutInflater.from(ExploreLocation.this).inflate(R.layout.manualenteralert, null);
        final EditText manualName = (EditText) v.findViewById(R.id.manName);
        final EditText manualAddress = (EditText) v.findViewById(R.id.manAddress);
        final RatingBar mRatingBar = (RatingBar) v.findViewById(R.id.ratingBar);

        AlertDialog.Builder alertBuilder3 = new AlertDialog.Builder(this);
        alertBuilder3.setView(v).setCancelable(true)
                .setPositiveButton("Save Location", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        String name = manualName.getText().toString().trim();
                        String address = manualAddress.getText().toString().trim();
                        rating = mRatingBar.getRating();

                        FirebaseUser user = mAuth.getCurrentUser();
                        String userID = user.getUid();

                /*saveRating = FirebaseDatabase.getInstance().getReference().child("User Locations").child(userID).child(name);
                saveRating.child("rating").setValue(rateValue);
                saveRating.child("name").setValue(name);
                saveRating.child("address").setValue(address);*/

                        LocationInformation locInfo = new LocationInformation(name, latitude, longitude, rating, placeID, time, address, mUsername);

                        saveLocations.child("AllUsersLocations").child(name).setValue(locInfo);
                        saveLocations.child("User Locations").child(userID).child(name).setValue(locInfo).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(ExploreLocation.this, "Location Saved", Toast.LENGTH_LONG).show();
                            }
                        })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(ExploreLocation.this, "Error location not saved", Toast.LENGTH_LONG).show();

                                    }
                                });
                    }
                }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        AlertDialog alert = alertBuilder3.create();
        alert.setTitle("Manually Enter Location Details: ");
        alert.show();

    }

    public void alertWebsiteIntent(){

        final View v = LayoutInflater.from(ExploreLocation.this).inflate(R.layout.intent_alert, null);

        AlertDialog.Builder alertBuilder2 = new AlertDialog.Builder(this);
        alertBuilder2.setView(v).setCancelable(true).setPositiveButton("Yes, Continue", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                Intent tMasterIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.ticketmaster.ie/"));
                startActivity(tMasterIntent);

            }
        }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        AlertDialog alert = alertBuilder2.create();
        alert.setTitle("Hold on...");
        alert.show();

    }

    public void alertRating(){

        final View v = LayoutInflater.from(ExploreLocation.this).inflate(R.layout.rating_alert, null);
        final RatingBar mRatingBar = (RatingBar) v.findViewById(R.id.ratingBar);

        final TextView textName = (TextView) v.findViewById(R.id.placeName);
        textName.setText(name);

        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(this);
        alertBuilder.setView(v)
                .setCancelable(true)
                .setPositiveButton("Rate", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        rateValue = mRatingBar.getRating();
                        Toast.makeText(ExploreLocation.this, "rating test1:  " + rateValue, Toast.LENGTH_LONG).show();

                        FirebaseUser user = mAuth.getCurrentUser();
                        String userID = user.getUid();

                        saveRating = FirebaseDatabase.getInstance().getReference().child("User Locations").child(userID).child(name);
                        saveRating1 = FirebaseDatabase.getInstance().getReference().child("AllUsersLocations").child(name);
                        saveRating.child("rating").setValue(rateValue);
                        saveRating1.child("rating").setValue(rateValue);

                    }
                })
                .setNegativeButton("Later", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
        AlertDialog alert = alertBuilder.create();
        alert.setTitle("");
        alert.show();

    }


    public void myMethod(){
        Toast.makeText(this, "please write any location name...", Toast.LENGTH_SHORT).show();


        //LatLng location = new LatLng(53.271316, -6.16224);
        //name = locInfo.name;


        mMap.addMarker(new MarkerOptions()
                .position(new LatLng(53.271316,-6.16224))
                .title("Hello world"));

        //mMap.moveCamera(CameraUpdateFactory.newLatLng(location));


    }


    /*private void setupPlaceAutocomplete() {

        places_fragment = (AutocompleteSupportFragment)getSupportFragmentManager()
                .findFragmentById(R.id.places_autocomplete_fragment);
        places_fragment.setPlaceFields(placeFields);
        places_fragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(@NonNull Place place) {
                Toast.makeText(NearbyLocations.this, "" + place.getName() , Toast.LENGTH_SHORT).show();

                onMapSearch();

                mMap.addMarker(new MarkerOptions().position(place.getLatLng()).title(place.getName())).setIcon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ROSE));
                mMap.moveCamera(CameraUpdateFactory.newLatLng(place.getLatLng()));
                //mMap.animateCamera(CameraUpdateFactory.zoomBy(12));

            }

            @Override
            public void onError(@NonNull Status status) {
                Toast.makeText(NearbyLocations.this, "" + status.getStatusMessage() , Toast.LENGTH_SHORT).show();
            }
        });
    }*/

    public void onMapSearch(View view) {
        EditText locationSearch = (EditText) findViewById(R.id.editSearch);
        String location = locationSearch.getText().toString();
        List<Address> addressList = null;

        if (location != null || !location.equals("")) {
            Geocoder geocoder = new Geocoder(this);
            try {
                addressList = geocoder.getFromLocationName(location, 1);

            } catch (IOException e) {
                e.printStackTrace();
            }
            Address address = addressList.get(0);
            LatLng latLng = new LatLng(address.getLatitude(), address.getLongitude());
            mMap.addMarker(new MarkerOptions().position(latLng).title("Searched Location"));
            mMap.animateCamera(CameraUpdateFactory.newLatLng(latLng));
            locationSearch.setText(" ");
        }
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


                        mMap.addMarker(new MarkerOptions().position(location).title(userlocation.name)).setIcon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_VIOLET));

                        mMap.moveCamera(CameraUpdateFactory.newLatLng(location));
                        //mMap.animateCamera(CameraUpdateFactory.zoomBy(12));
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

        mMap.getUiSettings().setZoomControlsEnabled(true);

        mMap.addMarker(new MarkerOptions()
                .position(new LatLng(passLong,passLat))
                .title("Hello world"))
                .setIcon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_VIOLET));


        //mMap.addMarker(new MarkerOptions().position(location).title(userlocation.name)).setIcon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_VIOLET));




        //if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION))

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {


            buildGoogleApiClient();

            mMap.setMyLocationEnabled(true);
            mMap.getUiSettings().setZoomControlsEnabled(true);
        }

        mMap.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {
            @Override
            public void onMapLongClick(LatLng latLng) {

                if (marker != null) {
                    marker.remove();
                }
                marker = mMap.addMarker(new MarkerOptions()
                        .position(latLng)
                        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE))
                        .title("My Current Click"));


            }
        });



        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {

                mHeart.setVisibility(View.VISIBLE);
                manualEnter.setVisibility(View.VISIBLE);

                latitude = marker.getPosition().latitude;
                longitude = marker.getPosition().longitude;
                name = marker.getTitle();
                placeID = marker.getId();
                address = marker.getSnippet();

                time = DateFormat.getDateTimeInstance().format(new Date());
                //long miliTime = time.getTime



                //getMarkerInfo();

                Toast.makeText(ExploreLocation.this, name, Toast.LENGTH_SHORT).show();
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

                LinearLayout info = new LinearLayout(ExploreLocation.this);
                info.setOrientation(LinearLayout.VERTICAL);

                TextView title = new TextView(ExploreLocation.this);
                title.setTextColor(Color.BLACK);
                title.setGravity(Gravity.CENTER);
                title.setTypeface(null, Typeface.BOLD);
                title.setText(marker.getTitle());

                TextView snippet = new TextView(ExploreLocation.this);
                snippet.setTextColor(Color.GRAY);
                snippet.setText(marker.getSnippet());

                info.addView(title);
                info.addView(snippet);

                return info;
            }
        });





    }

    /*public void getMarkerInfo(){

        List<HashMap<String, String>> nearbyPlacesList = null;

            for (int i=0; i<nearbyPlacesList.size(); i++){
                MarkerOptions markerOptions = new MarkerOptions();

                HashMap<String, String> googleNearbyPlace = nearbyPlacesList.get(i);
                vicinity = googleNearbyPlace.get("vicinity");
                address = googleNearbyPlace.get("adr_address");

            }


    }*/



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
                Intent intent3 = new Intent(this, ProfileActivity.class);
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
        markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ROSE));

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

            /*case R.id.search_address:
                AutocompleteSupportFragment searchedPlace = (AutocompleteSupportFragment) findViewById(R.id.location_search);
                String address = searchedPlace.getText().toString();

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


            case R.id.fab_action_hotel:
                //mMap.clear();
                String url = getUrl(latitude, longitude, hotel);
                transferData[0] = mMap;
                transferData[1] = url;

                getNearbyPlaces.execute(transferData);
                Toast.makeText(this, "Showing Nearby hotels...", Toast.LENGTH_SHORT).show();


                break;


            case R.id.fab_action_nightclub:
                //mMap.clear();
                url = getUrl(latitude, longitude, night_club);
                transferData[0] = mMap;
                transferData[1] = url;

                getNearbyPlaces.execute(transferData);

                Toast.makeText(this, "Showing Nearby Night Clubs...", Toast.LENGTH_SHORT).show();
                break;


            case R.id.fab_action_restaurant:
                //mMap.clear();
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

    public void getData() {

        eventsList = new ArrayList<Event>();

        String url = "https://app.ticketmaster.com/discovery/v2/events.json?countryCode=IE&apikey=OBfaBREyutatiLUjh3XJ5oW51KAQwYCQ&size=50";
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

                //mMap.addMarker(new MarkerOptions().position(location).title(userlocation.name)).setIcon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ROSE));


                marker.position(venue).title(venueName);
                marker.position(venue).snippet(events);
                marker.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE));
                mMap.addMarker(marker);
                Marker locationMarker = mMap.addMarker(marker);
                locationMarker.showInfoWindow();

            } //end if

        }
    }


    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);


        return true;
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
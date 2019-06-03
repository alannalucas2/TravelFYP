package com.example.alannalucas.travelfyp;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class  MainActivity extends AppCompatActivity {

    private RecyclerView mRecyclerFeed;

    Button btn_get_photo;
    ImageView locationImage;
    TextView txt_detail;

    private FeedAdapter feedAdapter;
    public static ArrayList<LocationInformation> feedList = new ArrayList<>();
    private ArrayList feedResults = new ArrayList<LocationInformation>();

    ArrayList<String> userList = new ArrayList<String>();

    private BottomNavigationView mBottomNav;
    private Button btnTest;
    private RatingBar mRatingBar;
    private String name, time, mUsername;


    private FirebaseAuth mAuth;
    private FirebaseDatabase mFirebaseDatabase;
    private FirebaseDatabase locationReference;
    private DatabaseReference userReference, friendsLocationsDatabase, friendsDatabase;
    private DatabaseReference locationsDatabase;
    private FirebaseAuth.AuthStateListener mAuthListener;

    private PlacesClient placesClient;
    private static final String TAG = "LocationsList";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        mAuth = FirebaseAuth.getInstance();
        locationsDatabase = FirebaseDatabase.getInstance().getReference().child("User Locations");
        friendsDatabase = FirebaseDatabase.getInstance().getReference().child("User Locations");
        friendsLocationsDatabase = FirebaseDatabase.getInstance().getReference().child("User Locations");

        /*btnTest = (Button) findViewById(R.id.btnTestClass);
        btnTest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, TestActivity.class);
                startActivity(intent);
            }
        });*/

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




        LinearLayoutManager mLayoutManager = new LinearLayoutManager(MainActivity.this);
        mLayoutManager.setReverseLayout(true);
        mLayoutManager.setStackFromEnd(true);
        mRecyclerFeed = (RecyclerView) findViewById(R.id.recyclerFeed);
        mRecyclerFeed.setHasFixedSize(true);
        mRecyclerFeed.setLayoutManager(mLayoutManager);
        mRecyclerFeed.addItemDecoration(new DividerItemDecoration(mRecyclerFeed.getContext(), DividerItemDecoration.HORIZONTAL));


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

        mBottomNav = (BottomNavigationView) findViewById(R.id.navigation);
        mBottomNav.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                selectNavigation(item);
                return true;
            }
        });

        final View v = LayoutInflater.from(MainActivity.this).inflate(R.layout.feed_layout, null);

        //mRatingBar = (RatingBar) v.findViewById(R.id.displayRatingBar);

        //feedAdapter = new FeedAdapter(getDataSetHistory(), MainActivity.this);
        //mRecyclerFeed.setAdapter(feedAdapter);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //getTimeAgo();

        /*mRatingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                locationsDatabase.child("rating").setValue(rating);

                DatabaseReference mDriverRatingDb = FirebaseDatabase.getInstance().getReference().child("User Locations").child(userID).child(name);
                mDriverRatingDb.child("rating").setValue(rating);
            }
        });*/

        //placesClient = Places.createClient(this);


        /*btn_get_photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(TextUtils.isEmpty(placeId)){
                    Toast.makeText(MainActivity.this, "Place id must not be null", Toast.LENGTH_SHORT).show();
                    return;
                }else{
                    getPhotoAndDetail(placeId);
                }
            }
        });*/


    }



    @Override
    protected void onStart() {
        super.onStart();
        //startListening();
        getUserIds();

    }

    private void getUserIds(){
        //locationReference = FirebaseDatabase.getInstance().getReference().child("User Locations");
        locationsDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange( DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    for(DataSnapshot locations : dataSnapshot.getChildren()){
                        startListening(locations.getKey());
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void startListening(String key) {

        Query query = FirebaseDatabase.getInstance()
                .getReference()
                .child("User Locations")
                .child(key)
                .orderByChild("time")
                .limitToLast(20);



        FirebaseRecyclerOptions<LocationInformation> options =
                new FirebaseRecyclerOptions.Builder<LocationInformation>()
                        .setQuery(query, LocationInformation.class)
                        .build();
        FirebaseRecyclerAdapter adapter = new FirebaseRecyclerAdapter<LocationInformation, UserViewHolder>(options) {
            @Override
            public UserViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                // Create a new instance of the ViewHolder, in this case we are using a custom
                // layout called R.layout.message for each item
                View view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.feed_layout, parent, false);

                return new UserViewHolder(view);


            }

            @Override
            protected void onBindViewHolder(UserViewHolder holder, int position, LocationInformation model) {

                holder.setName(model.name);
                holder.setRating(model.rating);
                holder.setAddress(model.address);
                holder.setTime(model.time);
                //holder.setUsername(model.mUsername);

                //holder.setRating(model.rating);

                final String name = getRef(position).getKey();
                //final float rateValue = getRef(position).getKey();


            }
        };
        mRecyclerFeed.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        adapter.startListening();

    }


    /*private void getLocationInfo(String key){
        DatabaseReference postInfoRef = FirebaseDatabase.getInstance().getReference().child("User Locations").child(key);
        postInfoRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    String userID = dataSnapshot.getKey().toString();
                    String name = "";
                    String address = "";
                    double latitude= 0;
                    double longitude = 0;
                    float rating = 0;
                    String time = "";
                    String placeID = "";

                    if(dataSnapshot.child("name").getValue()!=null){
                        name = dataSnapshot.child("name").getValue().toString();
                    }
                    if(dataSnapshot.child("address").getValue()!=null){
                        address = dataSnapshot.child("address").getValue().toString();
                    }

                    if(dataSnapshot.child("latitude").getValue()!=null){
                        latitude = Double.parseDouble(dataSnapshot.child("latitude").getValue().toString());
                    }
                    if(dataSnapshot.child("longitude").getValue()!=null){
                        longitude = Double.parseDouble(dataSnapshot.child("longitude").getValue().toString());
                    }

                    if(dataSnapshot.child("time").getValue()!=null){
                        time = dataSnapshot.child("time").getValue().toString();
                    }

                    if(dataSnapshot.child("rating").getValue()!=null){
                        rating = Float.parseFloat(dataSnapshot.child("rating").getValue().toString());
                    }
                    LocationInformation location = new LocationInformation( name,  latitude, longitude,  rating,  placeID,  time,  address);
                    feedResults.add(location);
                    feedList.add(location);
                    feedAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private ArrayList<LocationInformation> getDataSetHistory(){
        return feedResults;
    }*/





    public static class UserViewHolder extends RecyclerView.ViewHolder {
        View mView;

        public UserViewHolder(View itemView) {
            super(itemView);
            mView = itemView;
        }

        public void setUsername(String username){
            TextView textUsername = (TextView) mView.findViewById(R.id.usernameStamp);
            textUsername.setText(username);
        }


        public void setName(String name) {
            TextView textName = (TextView) mView.findViewById(R.id.locationStamp);
            textName.setText(name);
        }

        public void setAddress(String address) {
            TextView textAddress = (TextView) mView.findViewById(R.id.addressStamp);
            textAddress.setText(address);
        }

        public void setRating(float rating) {
            TextView textRating = (TextView) mView.findViewById(R.id.ratingStamp);
            textRating.setText(Float.toString(rating));
        }

        public void setTime(String time) {

        }
    }

            /*public void setRating(float rating){
                RatingBar ratingBar = (RatingBar) mView.findViewById(R.id.displayRatingBar);
                //ratingBar.setRating(Integer.valueOf(child.getValue().toString()));

            }

    }



    /*private void getTimeAgo(final String time){
        FirebaseUser user = mAuth.getCurrentUser();
        final String userID = user.getUid();
        locationsDatabase = FirebaseDatabase.getInstance().getReference().child("User Locations").child(userID);

        locationsDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                String format = DateFormat.getDateTimeInstance().format(new Date());
                Date past = format.parse(time);
                Date now = new Date();



            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }*/


    public void getDateInMillis() {

        Date now = new Date();
        //String src = DateUtils.getRelativeDateTimeString(this, now.getTime(), DateUtils.MINUTE_IN_MILLIS, DateUtils.WEEK_IN_MILLIS, 0);
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

            case R.id.accountDetails:
                Intent intent3 = new Intent(this, UpdateProfile.class);
                this.startActivity(intent3);
                break;


        }


        return true;
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
                Intent intent3 = new Intent(this, ProfileActivity.class);
                this.startActivity(intent3);
                break;

        }
    }

}




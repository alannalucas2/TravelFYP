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
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ViewFriendsLocList extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private FirebaseDatabase mFirebaseDatabase;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private DatabaseReference databaseLocations;
    private static final String TAG = "ViewListedLocations";
    private BottomNavigationView mBottomNav;
    private RecyclerView mRecyclerLocations;

    private String user_id, username;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_listed_locations);

        mAuth = FirebaseAuth.getInstance();

        databaseLocations = FirebaseDatabase.getInstance().getReference("Locations");
        user_id = getIntent().getStringExtra("user_id");
        username = getIntent().getStringExtra("username");


        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Users's Locations");

        mBottomNav = (BottomNavigationView) findViewById(R.id.navigation);
        mBottomNav.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                selectNavigation(item);
                return true;
            }
        });


        mAuth = FirebaseAuth.getInstance();
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        databaseLocations = mFirebaseDatabase.getReference();

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

        mRecyclerLocations = (RecyclerView) findViewById(R.id.recyclerSavedLocations);
        mRecyclerLocations.setHasFixedSize(true);
        mRecyclerLocations.setLayoutManager(new LinearLayoutManager(this));


        FirebaseUser user = mAuth.getCurrentUser();
        final String userID = user.getUid();




        databaseLocations.addValueEventListener(new ValueEventListener() {
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




    }

    @Override
    protected void onStart() {
        super.onStart();
        startListening();

    }

    public void startListening() {
        FirebaseUser user = mAuth.getCurrentUser();
        String userID = user.getUid();
        Query query = FirebaseDatabase.getInstance()
                .getReference()
                .child("User Locations")
                .child(user_id)
                .limitToLast(20);

        FirebaseRecyclerOptions<LocationInformation> options =
                new FirebaseRecyclerOptions.Builder<LocationInformation>()
                        .setQuery(query, LocationInformation.class)
                        .build();
        FirebaseRecyclerAdapter adapter = new FirebaseRecyclerAdapter<LocationInformation, ViewFriendsLocList.UserViewHolder>(options) {
            @Override
            public ViewFriendsLocList.UserViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                // Create a new instance of the ViewHolder, in this case we are using a custom
                // layout called R.layout.message for each item
                View view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.locations_single_layout, parent, false);

                return new ViewFriendsLocList.UserViewHolder(view);


            }

            @Override
            protected void onBindViewHolder(ViewFriendsLocList.UserViewHolder holder, int position, LocationInformation model) {

                holder.setName(model.name);
                holder.setRating(model.rating);
                holder.setAddress(model.address);
                holder.setUsername(model.username);

                //holder.setRating(model.rating);

                final String name = getRef(position).getKey();
                //final float rateValue = getRef(position).getKey();


            }
        };
        mRecyclerLocations.setAdapter(adapter);
        adapter.startListening();

    }



    public static class UserViewHolder extends RecyclerView.ViewHolder {
        View mView;

        public UserViewHolder(View itemView) {
            super(itemView);
            mView = itemView;
        }

        public void setName(String name){
            TextView textName = (TextView) mView.findViewById(R.id.locationStamp);
            textName.setText(name);
        }

        public void setAddress(String address){
            TextView textAddress = (TextView) mView.findViewById(R.id.addressStamp);
            textAddress.setText(address);
        }

        public void setRating(float rating){
            TextView textRating = (TextView) mView.findViewById(R.id.ratingStamp);
            textRating.setText(Float.toString(rating));
        }

        public void setUsername(String username){
            TextView textRating = (TextView) mView.findViewById(R.id.usernameStamp);
            textRating.setText(username);
        }

            /*public void setRating(float rating){
                RatingBar ratingBar = (RatingBar) mView.findViewById(R.id.displayRatingBar);
                //ratingBar.setRating(Integer.valueOf(child.getValue().toString()));

            }*/

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

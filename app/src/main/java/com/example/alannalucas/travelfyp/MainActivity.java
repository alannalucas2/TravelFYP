package com.example.alannalucas.travelfyp;

import android.content.Intent;
import android.graphics.Bitmap;
import android.media.Image;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.libraries.places.api.model.PhotoMetadata;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.net.FetchPhotoRequest;
import com.google.android.libraries.places.api.net.FetchPhotoResponse;
import com.google.android.libraries.places.api.net.FetchPlaceRequest;
import com.google.android.libraries.places.api.net.FetchPlaceResponse;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import java.util.Arrays;

public class  MainActivity extends AppCompatActivity {

    private RecyclerView mRecyclerFeed;

    private BottomNavigationView mBottomNav;
    Button btn_get_photo;
    ImageView image_view;
    TextView txt_detail;
    private String placeId = "ChIJie_ddoEJZ0gRaPFQNx-VMiM";

    private FirebaseAuth mAuth;
    private FirebaseDatabase mFirebaseDatabase;
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


        mFirebaseDatabase = FirebaseDatabase.getInstance();
        locationsDatabase = mFirebaseDatabase.getReference();

        mRecyclerFeed = (RecyclerView) findViewById(R.id.recyclerFeed);
        mRecyclerFeed.setHasFixedSize(true);
        mRecyclerFeed.setLayoutManager(new LinearLayoutManager(this));


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

        btn_get_photo = (Button) findViewById(R.id.btn_get_photo);
        image_view = (ImageView) findViewById(R.id.image_view);
        txt_detail = (TextView) findViewById(R.id.txt_detail);

        btn_get_photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(TextUtils.isEmpty(placeId)){
                    Toast.makeText(MainActivity.this, "Place id must not be null", Toast.LENGTH_SHORT).show();
                    return;
                }else{
                    getPhotoAndDetail(placeId);
                }
            }
        });

        mBottomNav = (BottomNavigationView) findViewById(R.id.navigation);
        mBottomNav.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                selectNavigation(item);
                return true;
            }
        });


        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


    }

    private void getPhotoAndDetail(String placeId){
        placeId = "ChIJie_ddoEJZ0gRaPFQNx-VMiM";

        FetchPlaceRequest request = FetchPlaceRequest.builder(placeId, Arrays.asList(Place.Field.PHOTO_METADATAS)).build();
        placesClient.fetchPlace(request)
                .addOnSuccessListener(new OnSuccessListener<FetchPlaceResponse>() {
            @Override
            public void onSuccess(FetchPlaceResponse fetchPlaceResponse) {
                Place place = fetchPlaceResponse.getPlace();

                //get photo meta data
                PhotoMetadata photoMetadata = place.getPhotoMetadatas().get(0);

                //create fetch photo request
                FetchPhotoRequest photoRequest = FetchPhotoRequest.builder(photoMetadata).build();
                placesClient.fetchPhoto(photoRequest).addOnSuccessListener(new OnSuccessListener<FetchPhotoResponse>() {
                    @Override
                    public void onSuccess(FetchPhotoResponse fetchPhotoResponse) {
                        Bitmap bitmap = fetchPhotoResponse.getBitmap();
                        image_view.setImageBitmap(bitmap);
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(MainActivity.this, "" + e.getMessage(), Toast.LENGTH_SHORT).show();

                    }
                });
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(MainActivity.this, "" + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        //detail
        FetchPlaceRequest detailrequest = FetchPlaceRequest.builder(placeId, Arrays.asList(Place.Field.LAT_LNG)).build();
        placesClient.fetchPlace(detailrequest).addOnCompleteListener(new OnCompleteListener<FetchPlaceResponse>() {
            @Override
            public void onComplete(@NonNull Task<FetchPlaceResponse> task) {
                if(task.isSuccessful()){
                    Place place = task.getResult().getPlace();
                    txt_detail.setText(new StringBuilder(String.valueOf(place.getLatLng().latitude))
                            .append("/").append(place.getLatLng().longitude));
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(MainActivity.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();

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
                .child(userID)
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

                final String name = getRef(position).getKey();


            }
        };
        mRecyclerFeed.setAdapter(adapter);
        adapter.startListening();

    }


        public static class UserViewHolder extends RecyclerView.ViewHolder {
            View mView;

            public UserViewHolder(View itemView) {
                super(itemView);
                mView = itemView;
            }

            public void setName(String name){
                TextView textName = (TextView) mView.findViewById(R.id.locNameStamp);
                textName.setText(name);
            }

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

            case R.id.menuProfile:
                Intent intent = new Intent(this, ProfileActivity.class);
                this.startActivity(intent);
                break;

            case R.id.menuLocation:
                Intent intent2 = new Intent(this, MapsActivity.class);
                this.startActivity(intent2);
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

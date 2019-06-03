 package com.example.alannalucas.travelfyp;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

 public class SaveLocation extends AppCompatActivity implements View.OnClickListener {


     private Button btnLocSave, btnProceed;
     private EditText editTextName, editTextLatitude, editTextLongitude;

     private FirebaseAuth mAuth;
     private FirebaseDatabase mFirebaseDatabase;
     private FirebaseAuth.AuthStateListener mAuthListener;
     private DatabaseReference databaseLocations;

     private static final String TAG = "AddToDatabase";


     @Override
     protected void onCreate(Bundle savedInstanceState) {
         super.onCreate(savedInstanceState);
         setContentView(R.layout.activity_save_location);

         //how to get page to say hi with name
         //textViewUserEmail.setText("Welcome " + user.getEmail());


         btnProceed=(Button)findViewById(R.id.btnProceed);
         editTextName=(EditText)findViewById(R.id.editTextName);
         editTextLatitude=(EditText)findViewById(R.id.editTextLatitude);
         editTextLongitude=(EditText)findViewById(R.id.editTextLongitude);
         btnLocSave=(Button)findViewById(R.id.btnLocSave);

         mAuth = FirebaseAuth.getInstance();
         mFirebaseDatabase = FirebaseDatabase.getInstance();
         databaseLocations = mFirebaseDatabase.getReference();

         Toolbar toolbar = findViewById(R.id.toolbar);
         setSupportActionBar(toolbar);

         mAuthListener = new FirebaseAuth.AuthStateListener() {
             @Override
             public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                 FirebaseUser user = firebaseAuth.getCurrentUser();
                 if (user != null) {
                     // User is signed in
                     Log.d(TAG, "onAuthStateChanged:signed_in:" + user.getUid());
                     toastMessage("Successfully signed in with: " + user.getEmail());
                 } else {
                     // User is signed out
                     Log.d(TAG, "onAuthStateChanged:signed_out");
                     toastMessage("Successfully signed out.");
                 }
                 // ...
             }
         };

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





         btnLocSave.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 saveUserLocation();
             }
         });


         btnProceed.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 Intent i = new Intent(SaveLocation.this, MapsActivity.class);
                 startActivity(i);
             }
         });
     }



         @Override
     public void onClick(View view) {
         if(view==btnProceed){
             finish();
         }
         if (view==btnLocSave){
             editTextName.getText().clear();
             editTextLatitude.getText().clear();
             editTextLongitude.getText().clear();
             saveUserLocation();

         }
     }

     private void saveUserLocation() {
         String name = editTextName.getText().toString().trim();
         double latitude = Double.parseDouble(editTextLatitude.getText().toString().trim());
         double longitude = Double.parseDouble(editTextLongitude.getText().toString().trim());

         /*LocationInformation locInfo = new LocationInformation(name, latitude, longitude, rating);

         FirebaseUser user = mAuth.getCurrentUser();
         String userID = user.getUid();
         databaseLocations.child(userID).child("Locations").child(name).setValue(locInfo).addOnSuccessListener(new OnSuccessListener<Void>() {
             @Override
             public void onSuccess(Void aVoid) {
                 Toast.makeText(SaveLocation.this, "Location saved", Toast.LENGTH_LONG).show();
             }
         })
                 .addOnFailureListener(new OnFailureListener() {
                     @Override
                     public void onFailure(@NonNull Exception e) {
                         Toast.makeText(SaveLocation.this, "Error location not saved", Toast.LENGTH_LONG).show();

                     }
                 });*/

     }


     //add a toast to show when successfully signed in
     /**
      * customizable toast
      * @param message
      */
     private void toastMessage(String message){
         Toast.makeText(this,message,Toast.LENGTH_SHORT).show();
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


 }



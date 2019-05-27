package com.example.alannalucas.travelfyp;

import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.util.Random;

public class UpdateProfile extends AppCompatActivity {

    private DatabaseReference userDatabase;
    private FirebaseUser user;


    /*Uri uriProfileImage;
    String profileImageUrl;*/

    private StorageReference mImageStorage;
    private static final int CHOOSE_IMAGE = 101;
    private BottomNavigationView mBottomNav;


    private ImageView mDisplayImage;
    private EditText mUsername, mAddress;
    private Button mBtnUpdateAccount, mBtnUpdateImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_profile);



        mDisplayImage = (ImageView) findViewById(R.id.displayImage);
        mUsername = (EditText) findViewById(R.id.username);
        mAddress = (EditText) findViewById(R.id.address);
        mBtnUpdateAccount = (Button) findViewById(R.id.btnUpdateAccount);
        mBtnUpdateImage = (Button) findViewById(R.id.btnUpdateImage);

        mImageStorage = FirebaseStorage.getInstance().getReference();

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mBtnUpdateImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent galleryIntent = new Intent();
                galleryIntent.setType("image/*");
                galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(galleryIntent, "Select Profile Picture"), CHOOSE_IMAGE);

                /*CropImage.activity().setGuidelines(CropImageView.Guidelines.ON)
                        .start(UpdateProfile.this);*/

            }
        });

        mBtnUpdateAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //uploadImageToFirebaseStorage();

                String username = mUsername.getText().toString();
                String address = mAddress.getText().toString();


                userDatabase.child("name").setValue(username).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(!task.isSuccessful()){
                            Toast.makeText(UpdateProfile.this, "Error not saved", Toast.LENGTH_LONG).show();

                        }else{
                            Toast.makeText(UpdateProfile.this, "Profile Updated", Toast.LENGTH_LONG).show();

                        }
                    }
                });
                userDatabase.child("address").setValue(address).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(!task.isSuccessful()){
                            Toast.makeText(UpdateProfile.this, "Error not saved", Toast.LENGTH_LONG).show();

                        }else{
                            Toast.makeText(UpdateProfile.this, "Profile Updated", Toast.LENGTH_LONG).show();

                        }
                    }
                });

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

        user = FirebaseAuth.getInstance().getCurrentUser();
        String current_user = user.getUid();
        userDatabase = FirebaseDatabase.getInstance().getReference().child("Users").child(current_user);



        userDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                String name = dataSnapshot.child("name").getValue().toString();
                String image = dataSnapshot.child("image").getValue().toString();
                String address = dataSnapshot.child("address").getValue().toString();
                String thumb_image = dataSnapshot.child("thumb_image").getValue().toString();

                mUsername.setText(name);
                mAddress.setText(address);

                Picasso.get().load(image).into(mDisplayImage);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CHOOSE_IMAGE && resultCode == RESULT_OK) {

            Uri imageUri = data.getData();

            CropImage.activity(imageUri)
                    .setCropShape(CropImageView.CropShape.OVAL)
                    .start(this);

            //Toast.makeText()
        }

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                Uri resultUri = result.getUri();



                StorageReference filepath = mImageStorage.child("profile_images").child(random() + ".jpg");
                filepath.putFile(resultUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                        if (task.isSuccessful()){

                            String download_url = task.getResult().getStorage().getDownloadUrl().toString();
                            userDatabase.child("image").setValue(download_url).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if(task.isSuccessful()){
                                        Toast.makeText(UpdateProfile.this, "Successful upload", Toast.LENGTH_LONG).show();

                                    }
                                }
                            });

                            Toast.makeText(UpdateProfile.this, "Successful upload", Toast.LENGTH_LONG).show();

                        }else{
                            Toast.makeText(UpdateProfile.this, "Error uploading", Toast.LENGTH_LONG).show();

                        }
                    }
                });

            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
            }
        }
    }




    /*public void uploadImageToFirebaseStorage() {
        final StorageReference profileImageRef = FirebaseStorage.getInstance().getReference("profilepics/" + System.currentTimeMillis() + ".jpg");


        if (uriProfileImage != null) {
            profileImageRef.putFile(uriProfileImage)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            //progressBar.setVisibility(View.GONE);
                            profileImageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    profileImageUrl = uri.toString();
                                    Toast.makeText(getApplicationContext(), "Image Upload Successful", Toast.LENGTH_SHORT).show();
                                }
                            })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                                        }
                                    });
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            //progressBar.setVisibility(View.GONE);
                            Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    });
        }
    }*/


    public static String random(){
        Random generator = new Random();
        StringBuilder randomStringBuilder = new StringBuilder();
        int randomLength = generator.nextInt(1);
        char tempChar;
        for (int i = 0; i< randomLength; i++){
            tempChar = (char) (generator.nextInt(96)+32);
            randomStringBuilder.append(tempChar);
        }
        return randomStringBuilder.toString();
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
                Intent intent3 = new Intent(this, UpdateProfile.class);
                this.startActivity(intent3);
                break;

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

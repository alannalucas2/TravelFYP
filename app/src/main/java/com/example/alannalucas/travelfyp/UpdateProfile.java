package com.example.alannalucas.travelfyp;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
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

import com.bumptech.glide.Glide;
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

import java.io.IOException;
import java.text.DateFormat;
import java.util.Date;
import java.util.Random;

import de.hdodenhof.circleimageview.CircleImageView;

public class UpdateProfile extends AppCompatActivity {

    private DatabaseReference userDatabase;
    private FirebaseUser user;


    /*Uri uriProfileImage;
    String profileImageUrl;*/

    private StorageReference mImageStorage;
    private static final int CHOOSE_IMAGE = 101;
    private BottomNavigationView mBottomNav;
    Uri uriItemImage, resultUri;

    private FirebaseAuth mAuth;

    private CircleImageView mProfileImage;
    private EditText mUsername, mAddress, mName;
    String profileImageUrl;
    private Button mBtnUpdateAccount, mBtnUpdateImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_update);

        final String pUsername = getIntent().getStringExtra("username");


        mProfileImage = (CircleImageView) findViewById(R.id.updateProfileImage);
        mUsername = (EditText) findViewById(R.id.username);
        mAddress = (EditText) findViewById(R.id.address);
        mName = (EditText) findViewById(R.id.name);
        mBtnUpdateAccount = (Button) findViewById(R.id.btnUpdateAccount);
        mBtnUpdateImage = (Button) findViewById(R.id.btnUploadPic);

        mImageStorage = FirebaseStorage.getInstance().getReference();

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mBtnUpdateImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showImageChooser();
            }
        });

        mBtnUpdateAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //uploadImageToFirebaseStorage();

                String name = mName.getText().toString();
                String username = mUsername.getText().toString();
                String address = mAddress.getText().toString();

                userDatabase.child("name").setValue(name).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(!task.isSuccessful()){
                            Toast.makeText(UpdateProfile.this, "Error not saved", Toast.LENGTH_LONG).show();

                        }else{
                            Toast.makeText(UpdateProfile.this, "Profile Updated", Toast.LENGTH_LONG).show();

                        }
                    }
                });
                userDatabase.child("username").setValue(username).addOnCompleteListener(new OnCompleteListener<Void>() {
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

                String username = dataSnapshot.child("username").getValue().toString();
                String name = dataSnapshot.child("name").getValue().toString();
                //String image = dataSnapshot.child("image").getValue().toString();
                String image = String.valueOf(dataSnapshot.child("image").getValue());
                String address = dataSnapshot.child("address").getValue().toString();
                //String thumb_image = dataSnapshot.child("thumb_image").getValue().toString();

                if(name == ""){
                    mName.setText("Enter Name");
                }
                if(username == ""){
                    mName.setText("Enter Username");
                }
                if(address == ""){
                    mName.setText("Enter Address");
                }


                mName.setText(name);
                mUsername.setText(username);
                mAddress.setText(address);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }


    private void showImageChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Profile Picture"), CHOOSE_IMAGE);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        if (requestCode == CHOOSE_IMAGE && resultCode == RESULT_OK && data != null && data.getData() != null) {
            uriItemImage = data.getData();

            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uriItemImage);
                mProfileImage.setImageBitmap(bitmap);

                uploadImageToFirebaseStorage();


            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void uploadImageToFirebaseStorage(){
        final String currentTime = DateFormat.getDateTimeInstance().format(new Date());

        StorageReference filepath = mImageStorage.child("profile_images").child(currentTime + ".jpg");
        filepath.putFile(uriItemImage).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                if (task.isSuccessful()){

                    String download_url = task.getResult().getStorage().getDownloadUrl().toString();
                    userDatabase.child("image").setValue(currentTime + ".jpg").addOnCompleteListener(new OnCompleteListener<Void>() {
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

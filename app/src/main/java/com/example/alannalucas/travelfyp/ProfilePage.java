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

import java.text.DateFormat;
import java.util.Date;

public class ProfilePage extends AppCompatActivity {

    TextView mDisplayName, mDisplayAddress, mTotalFriends;
    Button mBtnAddFriend, mBtnDeclineFriend, mBtnFriendMap;


    private String mCurrent_state;

    private BottomNavigationView mBottomNav;

    private DatabaseReference mUsersDatabase;
    private DatabaseReference mFriendRequestDatabase;
    private DatabaseReference mFriendDatabase;
    private FirebaseUser mCurrent_user;
    FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_page);


        mAuth = FirebaseAuth.getInstance();


        final String user_id = getIntent().getStringExtra("user_id");


        mTotalFriends = (TextView) findViewById(R.id.totalFriends);
        mDisplayAddress = (TextView) findViewById(R.id.displayAddress);
        mDisplayName = (TextView) findViewById(R.id.displayName);
        mBtnAddFriend = (Button) findViewById(R.id.btnAddFriend);
        mBtnDeclineFriend = (Button) findViewById(R.id.btnDeclineFriend);
        mBtnFriendMap = (Button) findViewById(R.id.btnFriendMap);

        mBtnFriendMap.setVisibility(View.INVISIBLE);

        mBtnFriendMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ProfilePage.this, FriendMapsActivity.class);
                startActivity(intent);
                finish();
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

        final String username = String.valueOf(mDisplayName);
        //mBtnFriendMap.setVisibility(View.GONE);


        mCurrent_state = "not_friends";

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mBtnDeclineFriend.setVisibility(View.INVISIBLE);


        mUsersDatabase = FirebaseDatabase.getInstance().getReference().child("Users").child(user_id);
        mFriendRequestDatabase = FirebaseDatabase.getInstance().getReference().child("FriendRequest");
        mFriendDatabase = FirebaseDatabase.getInstance().getReference().child("Friends");
        mCurrent_user = FirebaseAuth.getInstance().getCurrentUser();

        mUsersDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                String display_name = dataSnapshot.child("username").getValue().toString();
                String address = dataSnapshot.child("address").getValue().toString();

                mDisplayName.setText(display_name);
                mDisplayAddress.setText(address);


                //FRIENDS LIST - REQUEST

                mFriendRequestDatabase.child(mCurrent_user.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.hasChild(user_id)) {
                            String req_type = dataSnapshot.child(user_id).child("request_type").getValue().toString();

                            if (req_type.equals("received")) {

                                mCurrent_state = "req_received";
                                mBtnAddFriend.setText("Accept Friend Request");
                                mBtnFriendMap.setText("Decline Friend Request");
                                mBtnFriendMap.setVisibility(View.VISIBLE);


                            } else if (req_type.equals("sent")) {
                                mCurrent_state = "req_sent";
                                mBtnAddFriend.setText("Cancel Friend Request");
                            }

                        } else {

                            mFriendDatabase.child(mCurrent_user.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    if (dataSnapshot.hasChild(user_id)) {

                                        mCurrent_state = "friends";
                                        mBtnAddFriend.setText("Remove Friend");


                                        mBtnDeclineFriend.setText("View User's Map");
                                        mBtnDeclineFriend.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {

                                                Intent intent = new Intent(ProfilePage.this, FriendMapsActivity.class);
                                                intent.putExtra("user_id", user_id);
                                                intent.putExtra("username", username);
                                                startActivity(intent);
                                                finish();

                                            }
                                        });

                                        mBtnFriendMap.setText("View User's Locations");
                                        mBtnFriendMap.setVisibility(View.VISIBLE);
                                        mBtnFriendMap.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {

                                                Intent intent = new Intent(ProfilePage.this, ViewFriendsLocList.class);
                                                intent.putExtra("user_id", user_id);
                                                intent.putExtra("username", username);
                                                startActivity(intent);
                                                finish();

                                            }
                                        });


                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                }
                            });

                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        /*if(mCurrent_state.equals("not_friends")){
            mBtnDeclineFriend.setVisibility(View.INVISIBLE);
        }*/

        mBtnFriendMap.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 if (mCurrent_state.equals("req_received")) {


                     mFriendRequestDatabase.child(mCurrent_user.getUid()).child(user_id).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                         @Override
                         public void onSuccess(Void aVoid) {

                             mFriendRequestDatabase.child(user_id).child(mCurrent_user.getUid()).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                                 @Override
                                 public void onSuccess(Void aVoid) {
                                     mBtnAddFriend.setEnabled(true);
                                     mCurrent_state = "not_friends";
                                     mBtnAddFriend.setText("Send Friend Request");
                                     mBtnFriendMap.setVisibility(View.INVISIBLE);


                                 }
                             });

                         }
                     });
                 }
             }
         });



        mBtnAddFriend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mBtnAddFriend.setEnabled(false);

                //NOT FRIENDS STATE

                if (mCurrent_state.equals("not_friends")) {

                    mFriendRequestDatabase.child(mCurrent_user.getUid()).child(user_id).child("request_type").setValue("sent").addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {

                            if (task.isSuccessful()) {
                                mFriendRequestDatabase.child(user_id).child(mCurrent_user.getUid()).child("request_type").setValue("received").addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        mBtnAddFriend.setEnabled(true);
                                        mCurrent_state = "req_sent";
                                        mBtnAddFriend.setText("Cancel Friend Request");

                                        //Toast.makeText(ProfilePage.this, "Friend Request Sent", Toast.LENGTH_LONG).show();

                                    }
                                });

                            } else {
                                Toast.makeText(ProfilePage.this, "Error", Toast.LENGTH_LONG).show();

                            }

                        }
                    });

                }

                // CANCEL REQUEST STATE

                if (mCurrent_state.equals("req_sent")) {
                    mFriendRequestDatabase.child(mCurrent_user.getUid()).child(user_id).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {

                            mFriendRequestDatabase.child(user_id).child(mCurrent_user.getUid()).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    mBtnAddFriend.setEnabled(true);
                                    mCurrent_state = "not_friends";
                                    mBtnAddFriend.setText("Send Friend Request");
                                }
                            });

                        }
                    });
                }

                // REQUEST RECEIVED STATE

                if (mCurrent_state.equals("req_received")) {



                    final String currentDate = DateFormat.getDateTimeInstance().format(new Date());

                    mFriendDatabase.child(mCurrent_user.getUid()).child(user_id).child("date").setValue(currentDate).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {

                            mFriendDatabase.child(user_id).child(mCurrent_user.getUid()).child("date").setValue(currentDate).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {

                                    mFriendRequestDatabase.child(mCurrent_user.getUid()).child(user_id).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {

                                            mFriendRequestDatabase.child(user_id).child(mCurrent_user.getUid()).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void aVoid) {
                                                    mBtnAddFriend.setEnabled(true);
                                                    mCurrent_state = "friends";
                                                    mBtnAddFriend.setText("Remove Friend");
                                                    mBtnFriendMap.setVisibility(View.VISIBLE);
                                                    mBtnFriendMap.setText("View User Map");


                                                }
                                            });

                                        }
                                    });


                                }
                            });

                        }
                    });
                }

                if (mCurrent_state.equals("friends")){

                    mFriendDatabase.child(mCurrent_user.getUid()).child(user_id).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {

                            mFriendDatabase.child(user_id).child(mCurrent_user.getUid()).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    mBtnAddFriend.setEnabled(true);
                                    mCurrent_state = "not_friends";
                                    mBtnAddFriend.setText("Send Friend Request");
                                    mBtnFriendMap.setVisibility(View.INVISIBLE);
                                    mBtnDeclineFriend.setVisibility(View.INVISIBLE);


                                }
                            });

                        }
                    });

                }

            }
        });
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

package com.example.alannalucas.travelfyp;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

public class AllUsers extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private RecyclerView recyclerUsers;
    private DatabaseReference usersDatabase;
    Button mBtnAllFriendsMap;
    private BottomNavigationView mBottomNav;
    View mView;
    private DrawerLayout drawer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_users);

        usersDatabase = FirebaseDatabase.getInstance().getReference().child("Users");


        mBtnAllFriendsMap = (Button) findViewById(R.id.btnAllFriendsMap);


        recyclerUsers = (RecyclerView) findViewById(R.id.recyclerUsers);
        recyclerUsers.setHasFixedSize(true);
        recyclerUsers.setLayoutManager(new LinearLayoutManager(this));


        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("All App Users");

        mBottomNav = (BottomNavigationView) findViewById(R.id.navigation);
        mBottomNav.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                selectNavigation(item);
                return true;
            }
        });

        mBtnAllFriendsMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AllUsers.this, FriendMapsActivity.class);
                startActivity(intent);
                finish();
            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();
        startListening();

    }

    public void startListening() {
        Query query = FirebaseDatabase.getInstance()
                .getReference()
                .child("Users")
                .limitToLast(50);

        FirebaseRecyclerOptions<Users> options =
                new FirebaseRecyclerOptions.Builder<Users>()
                        .setQuery(query, Users.class)
                        .build();
        FirebaseRecyclerAdapter adapter = new FirebaseRecyclerAdapter<Users, UserViewHolder>(options) {
            @Override
            public UserViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                // Create a new instance of the ViewHolder, in this case we are using a custom
                // layout called R.layout.message for each item
                View view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.users_single_layout, parent, false);

                System.out.println("made it to createviewholder");

                return new UserViewHolder(view);


            }

            @Override
            protected void onBindViewHolder(UserViewHolder holder, int position, Users model) {
                // Bind the Chat object to the ChatHolder
                holder.setName(model.name);
                holder.setAddress(model.address);

                final String user_id = getRef(position).getKey();


                System.out.println("made it to bindviewholder");


                holder.mView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        //for activity to activity
                        Intent profileIntent = new Intent(AllUsers.this, ProfilePage.class);
                        profileIntent.putExtra("user_id", user_id);
                        startActivity(profileIntent);




                    }
                });

            }

        };
        recyclerUsers.setAdapter(adapter);
        adapter.startListening();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

        switch (menuItem.getItemId()) {
            case R.id.nav_profile:
                Intent intent = new Intent(AllUsers.this, ProfileActivity.class);
                startActivity(intent);
                finish();
                break;
            case R.id.nav_maps:
                Intent intent2 = new Intent(AllUsers.this, NearbyLocations.class);
                startActivity(intent2);
                finish();
                break;
            case R.id.nav_users:
                Intent intent3 = new Intent(AllUsers.this, AllUsers.class);
                startActivity(intent3);
                finish();
                break;

            case R.id.nav_share:
                Toast.makeText(this, "Share", Toast.LENGTH_LONG).show();
                break;

            case R.id.nav_sendshare:
                Toast.makeText(this, "Send", Toast.LENGTH_LONG).show();
                break;

        }
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    /*public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }*/


    public static class UserViewHolder extends RecyclerView.ViewHolder {
        View mView;

        public UserViewHolder(View itemView) {
            super(itemView);
            mView = itemView;
        }


        public void setName(String name) {
            TextView userNameView = (TextView) mView.findViewById(R.id.userSingleName);
            userNameView.setText(name);
        }

        public void setAddress(String address) {
            TextView userAddressView = (TextView) mView.findViewById(R.id.userSingleAddress);
            userAddressView.setText(address);
        }
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

            case R.id.accountDetails:
                Intent intent3 = new Intent(this, UpdateProfile.class);
                this.startActivity(intent3);
                break;

            case R.id.menuProfile:
                Intent intent = new Intent(this, ProfilePage.class);
                this.startActivity(intent);
                break;

            case R.id.menuLocation:
                Intent intent2 = new Intent(this, MapsActivity.class);
                this.startActivity(intent2);
                break;

            case R.id.listOnline:
                Intent intent4 = new Intent(this, ListOnline.class);
                this.startActivity(intent4);
                break;

        }


        return true;
    }
}






        /*FirebaseRecyclerOptions<AllUsers> options = new FirebaseRecyclerOptions.Builder<AllUsers>()
                        .setQuery(usersDatabase, AllUsers.class)
                        .build();

        FirebaseRecyclerAdapter adapter = new FirebaseRecyclerAdapter<Users, UsersViewHolder>(options) {

            @Override
            protected void onBindViewHolder(@NonNull UsersViewHolder holder, int position, @NonNull Users model) {
                usersViewHolder.setName(user.getName());
            }

            @Override
            public UsersViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.users_single_layout, parent, false);

                return new UsersViewHolder(view);
            }


    public class UsersViewHolder extends RecyclerView.ViewHolder{

        View mView;

        public UsersViewHolder(View itemView){
            super(itemView);

            mView = itemView;
        }
    }


}*/

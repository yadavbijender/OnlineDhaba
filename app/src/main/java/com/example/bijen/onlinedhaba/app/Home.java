package com.example.bijen.onlinedhaba.app;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.bijen.onlinedhaba.R;
import com.firebase.client.Firebase;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class Home extends AppCompatActivity {

    Toolbar toolbar;
    DrawerLayout drawerLayout;
    ActionBarDrawerToggle actionBarDrawerToggle;
    NavigationView navigationView;
    RecyclerView recyclerView;
    DatabaseReference databaseReference,databaseProfile;
    FirebaseAuth firebaseAuth;
    ImageView profilePic;
    TextView managerName, managerEmail;

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)){
            drawerLayout.closeDrawer(GravityCompat.START);
        }
        else {
            super.onBackPressed();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        Firebase.setAndroidContext(this);
        toolbar = (Toolbar) findViewById(R.id.admin_home_toolbar);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout_admin_home);
        navigationView = (NavigationView) findViewById(R.id.navigation_view_admin_home);
        recyclerView = (RecyclerView) findViewById(R.id.admin_recycler_view);
        firebaseAuth = FirebaseAuth.getInstance();
        databaseProfile = FirebaseDatabase.getInstance().getReference().child("Admin").child(firebaseAuth.getCurrentUser().getUid());
        databaseReference = FirebaseDatabase.getInstance().getReference().child("Admin").child(firebaseAuth.getCurrentUser().getUid()).child("Food Item");
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        toolbar.setTitle("Home");
        toolbar.inflateMenu(R.menu.menu);
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()){
                    case R.id.about:
                        Intent intent = new Intent(Home.this,About.class);
                        startActivity(intent);
                        break;

                    case R.id.setting:
                        Intent intent1 = new Intent(Home.this,AdminSetting.class);
                        startActivity(intent1);
                        break;

                    case R.id.help:
                        Intent intent2 = new Intent(Home.this,AdminHelp.class);
                        startActivity(intent2);
                        break;

                    case R.id.logout:
                        FirebaseAuth.getInstance().signOut();
                        finish();
                        Intent intent3 = new Intent(Home.this,Admin.class);
                        intent3.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        intent3.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent3);
                        break;

                    case R.id.exit:
                        AlertDialog.Builder builder = new AlertDialog.Builder(Home.this);
                        builder.setTitle("Are You sure?");
                        builder.setMessage("You want to exit this application.");
                        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                moveTaskToBack(true);
                                android.os.Process.killProcess(android.os.Process.myPid());
                                System.exit(1);
                            }
                        });
                        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                        AlertDialog dialog = builder.create();
                        dialog.show();
                        break;
                }
                return true;
            }
        });

        actionBarDrawerToggle = new ActionBarDrawerToggle(Home.this,drawerLayout,toolbar,R.string.drawer_open,R.string.drawer_close);
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();
        View headerView = navigationView.getHeaderView(0);
        managerName = headerView.findViewById(R.id.manager_profile_name);
        managerEmail = headerView.findViewById(R.id.manager_profile_email);
        profilePic = headerView.findViewById(R.id.manager_profile_pic);
        databaseProfile.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String name, email, image;
                name = dataSnapshot.child("Manager's Name").getValue(String.class);
                email = dataSnapshot.child("Email").getValue(String.class);
                image = dataSnapshot.child("Profile Image").getValue(String.class);
                Picasso.with(Home.this)
                        .load(image)
                        .into(profilePic);
                managerName.setText(name);
                managerEmail.setText(email);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.admin_profile:
                        Intent intent5 = new Intent(Home.this,Admin_Profile.class);
                        startActivity(intent5);
                        break;

                    case R.id.register_hotel:
                        Intent intent6 = new Intent(Home.this,RegisterHotel.class);
                        startActivity(intent6);
                        break;

                    case R.id.add_item:
                        Intent intent7 = new Intent(Home.this,FoodItem.class);
                        startActivity(intent7);
                        break;
                }
                return true;
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseRecyclerAdapter<AdminItemList,AdminItemHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<AdminItemList, AdminItemHolder>(
                AdminItemList.class,
                R.layout.item_list,
                AdminItemHolder.class,
                databaseReference
        ) {
            @Override
            protected void populateViewHolder(AdminItemHolder viewHolder, AdminItemList model, int position) {
                viewHolder.setName(model.getName());
                viewHolder.setDescription(model.getDescription());
                viewHolder.setPrize(model.getPrize());
                viewHolder.setImage(getApplicationContext(),model.getImage());
            }
        };
        recyclerView.setAdapter(firebaseRecyclerAdapter);
    }

    public static class AdminItemHolder extends RecyclerView.ViewHolder{

        TextView itemname, itemdescription, itemprize;
        ImageView itemimage;

        public AdminItemHolder(View itemView) {
            super(itemView);
            itemname = (TextView) itemView.findViewById(R.id.admin_item_name);
            itemdescription = (TextView) itemView.findViewById(R.id.admin_item_description);
            itemprize = (TextView) itemView.findViewById(R.id.admin_item_prize);
            itemimage = (ImageView) itemView.findViewById(R.id.admin_item_image);
        }

        public void setName(String name){
            itemname.setText(name);
        }

        public void setDescription(String description){
            itemdescription.setText(description);
        }

        public void setPrize(String prize){
            itemprize.setText(prize);
        }

        public void setImage(Context context,String image){
            Picasso.with(context)
                    .load(image)
                    .into(itemimage);
        }
    }
}

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
import android.widget.Toast;

import com.example.bijen.onlinedhaba.Interface.ItemClickListener;
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

public class HomeUser extends AppCompatActivity {

    Toolbar toolbar;
    DrawerLayout drawerLayout;
    ActionBarDrawerToggle actionBarDrawerToggle;
    NavigationView navigationView;
    RecyclerView recyclerView;
    DatabaseReference databaseReference,databaseProfile;
    FirebaseAuth firebaseAuth;
    ImageView userimage;
    TextView uname,uemail;
    FirebaseRecyclerAdapter<UserItemList,UserItemHolder> firebaseRecyclerAdapter;

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
        setContentView(R.layout.activity_home_user);

        Firebase.setAndroidContext(this);
        firebaseAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference().child("Items");
        recyclerView = (RecyclerView) findViewById(R.id.user_recycler_view);
        navigationView = (NavigationView) findViewById(R.id.navigation_view_user_home);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        toolbar = (Toolbar) findViewById(R.id.user_home_toolbar);
        toolbar.setTitle("Home");
        toolbar.inflateMenu(R.menu.menu);
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()){
                    case R.id.about:
                        break;

                    case R.id.logout:
                        if (FirebaseAuth.getInstance().getCurrentUser().getUid() != null){
                            FirebaseAuth.getInstance().signOut();
                            finish();
                            Intent intent = new Intent(HomeUser.this,MainActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);
                        }
                        break;

                    case R.id.exit:
                        AlertDialog.Builder builder = new AlertDialog.Builder(HomeUser.this);
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

        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout_user_home);
        actionBarDrawerToggle = new ActionBarDrawerToggle(HomeUser.this,drawerLayout,toolbar,R.string.drawer_open,R.string.drawer_close);
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();
        if (FirebaseAuth.getInstance().getCurrentUser().getUid() != null) {
            databaseProfile = FirebaseDatabase.getInstance().getReference().child("User").child(FirebaseAuth.getInstance().getCurrentUser().getUid());
            View headerView = navigationView.getHeaderView(0);
            uname = (TextView) headerView.findViewById(R.id.user_profile_name);
            uemail = (TextView) headerView.findViewById(R.id.user_profile_email);
            userimage = (ImageView) headerView.findViewById(R.id.user_profile_pic);
            databaseProfile.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    String name, email, image;
                    name = dataSnapshot.child("User_name").getValue(String.class);
                    email = dataSnapshot.child("Email").getValue(String.class);
                    image = dataSnapshot.child("Profile_image").getValue(String.class);
                    Picasso.with(HomeUser.this)
                            .load(image)
                            .into(userimage);
                    uname.setText(name);
                    uemail.setText(email);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }


        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.user:
                        Intent intent = new Intent(HomeUser.this,User.class);
                        startActivity(intent);
                        break;

                    case R.id.category_user:
                        Toast.makeText(getApplicationContext(),"user",Toast.LENGTH_LONG).show();
                        break;
                }
                return true;
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();

        firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<UserItemList, UserItemHolder>(
                UserItemList.class,
                R.layout.item_list_user,
                UserItemHolder.class,
                databaseReference
        ) {
            @Override
            protected void populateViewHolder(UserItemHolder viewHolder, UserItemList model, final int position) {
                viewHolder.setName(model.getName());
                viewHolder.setDescription(model.getDescription());
                viewHolder.setPrize(model.getPrize());
                viewHolder.setHotel(model.getHotel());
                viewHolder.setLocation(model.getLocation());
                viewHolder.setImage(getApplicationContext(),model.getImage());
                viewHolder.setItemClickListener(new ItemClickListener() {
                    @Override
                    public void onclick(View view, int position, boolean isLongClick) {
                        Intent food = new Intent(HomeUser.this,Item_Deatail.class);
                        food.putExtra("Food_id",firebaseRecyclerAdapter.getRef(position).getKey());
                        startActivity(food);
                    }
                });
            }
        };
        recyclerView.setAdapter(firebaseRecyclerAdapter);
    }

    public static class UserItemHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView itemname, itemdescription, itemprize, hotelname, hotellocation;
        ImageView itemimage;

        private ItemClickListener itemClickListener;

        public UserItemHolder(View itemView) {
            super(itemView);
            itemimage = (ImageView) itemView.findViewById(R.id.user_item_image);
            itemname = (TextView) itemView.findViewById(R.id.user_item_name);
            itemdescription = (TextView) itemView.findViewById(R.id.user_item_description);
            itemprize = (TextView) itemView.findViewById(R.id.user_item_prize);
            hotelname = (TextView) itemView.findViewById(R.id.user_hotel_namer);
            hotellocation = (TextView) itemView.findViewById(R.id.user_hotel_location);
            itemView.setOnClickListener(this);
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

        public void setHotel(String hotel){
            hotelname.setText(hotel);
        }

        public void setLocation(String location){
            hotellocation.setText(location);
        }

        public void setImage(Context context, String image){
            Picasso.with(context)
                    .load(image)
                    .into(itemimage);
        }

        public void setItemClickListener(ItemClickListener itemClickListener){
            this.itemClickListener = itemClickListener;
        }

        @Override
        public void onClick(View v) {
            itemClickListener.onclick(v,getAdapterPosition(),false);
        }
    }
}

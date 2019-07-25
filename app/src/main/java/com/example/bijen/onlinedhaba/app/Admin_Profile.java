package com.example.bijen.onlinedhaba.app;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.bijen.onlinedhaba.R;
import com.firebase.client.Firebase;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class Admin_Profile extends AppCompatActivity {

    ImageView profileImage;
    Toolbar toolbar;
    TextView ownerName, ownerEmail, ownerPhone, hotelName, hotelLocation, landlineContact;
    FirebaseAuth firebaseAuth;
    DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin__profile);

        Firebase.setAndroidContext(Admin_Profile.this);
        toolbar = (Toolbar) findViewById(R.id.admin_profile_toolbar);
        profileImage = (ImageView) findViewById(R.id.profile_hotel_image);
        ownerName = (TextView) findViewById(R.id.owner_name);
        ownerEmail = (TextView) findViewById(R.id.owner_email);
        ownerPhone = (TextView) findViewById(R.id.owner_phone);
        hotelName = (TextView) findViewById(R.id.owner_hotel_name);
        hotelLocation = (TextView) findViewById(R.id.owner_hotel_location);
        landlineContact = (TextView) findViewById(R.id.owner_landline_contact);
        firebaseAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference().child("Admin");

        toolbar.setTitle("Admin Profile");
        toolbar.inflateMenu(R.menu.menu);
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()){
                    case R.id.about:
                        Intent intent = new Intent(Admin_Profile.this,About.class);
                        startActivity(intent);
                        break;

                    case R.id.setting:
                        Intent intent1 = new Intent(Admin_Profile.this,AdminSetting.class);
                        startActivity(intent1);
                        break;

                    case R.id.help:
                        Intent intent2 = new Intent(Admin_Profile.this,AdminHelp.class);
                        startActivity(intent2);
                        break;

                    case R.id.logout:
                        FirebaseAuth.getInstance().signOut();
                        finish();
                        Intent intent3 = new Intent(Admin_Profile.this,Admin.class);
                        intent3.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        intent3.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent3);
                        break;

                    case R.id.exit:
                        AlertDialog.Builder builder = new AlertDialog.Builder(Admin_Profile.this);
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
    }

    @Override
    protected void onStart() {
        super.onStart();

        String admin_id = firebaseAuth.getCurrentUser().getUid();
        final DatabaseReference current_id = databaseReference.child(admin_id);
        current_id.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String image, name, email, phone, hotel, location, landline;
                image = dataSnapshot.child("Hotel Image").getValue(String.class);
                name = dataSnapshot.child("Manager's Name").getValue(String.class);
                email = dataSnapshot.child("Email").getValue(String.class);
                phone = dataSnapshot.child("Phone Number").getValue(String.class);
                hotel = dataSnapshot.child("Hotel's Name").getValue(String.class);
                location = dataSnapshot.child("Location").getValue(String.class);
                landline = dataSnapshot.child("Landline Number").getValue(String.class);
                Picasso.with(Admin_Profile.this)
                        .load(image)
                        .into(profileImage);
                ownerName.setText(name);
                ownerEmail.setText(email);
                ownerPhone.setText(phone);
                hotelName.setText(hotel);
                hotelLocation.setText(location);
                landlineContact.setText(landline);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}

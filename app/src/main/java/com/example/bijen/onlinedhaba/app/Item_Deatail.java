package com.example.bijen.onlinedhaba.app;

import android.support.annotation.NonNull;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.cepheuen.elegantnumberbutton.view.ElegantNumberButton;
import com.example.bijen.onlinedhaba.R;
import com.firebase.client.Firebase;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class Item_Deatail extends AppCompatActivity {

    TextView foodname, foodprize;
    ImageView foodimage;
    CollapsingToolbarLayout collapsingToolbarLayout;
    FloatingActionButton floatingActionButton;
    ElegantNumberButton elegantNumberButton;
    DatabaseReference databaseReference;
    String foodid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item__deatail);

        Firebase.setAndroidContext(this);
        databaseReference = FirebaseDatabase.getInstance().getReference().child("Items");
        collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.collapsing_tollbar_layout);
      //  floatingActionButton = (FloatingActionButton) findViewById(R.id.btnCart);
        elegantNumberButton = (ElegantNumberButton) findViewById(R.id.number_btn);
        foodimage = (ImageView) findViewById(R.id.img_food);
        foodname = (TextView) findViewById(R.id.food_name);
        foodprize = (TextView) findViewById(R.id.food_prize);
        collapsingToolbarLayout.setCollapsedTitleTextAppearance(R.style.CollapseAppBar);
        collapsingToolbarLayout.setExpandedTitleTextAppearance(R.style.ExpandedAppBar);

        if (getIntent()!= null) {
            foodid = getIntent().getStringExtra("Food_id");
            if (!foodid.isEmpty()) {
                databaseReference.child(foodid).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        String image,name,prize;
                        image = dataSnapshot.child("image").getValue(String.class);
                        name = dataSnapshot.child("name").getValue(String.class);
                        prize = dataSnapshot.child("prize").getValue(String.class);
                        Picasso.with(Item_Deatail.this)
                                .load(image)
                                .into(foodimage);
                        collapsingToolbarLayout.setTitle(name);
                        foodname.setText(name);
                        foodprize.setText(prize);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }
        }
    }
}

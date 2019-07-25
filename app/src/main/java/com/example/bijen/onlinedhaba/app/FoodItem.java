package com.example.bijen.onlinedhaba.app;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.bijen.onlinedhaba.R;
import com.firebase.client.Firebase;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class FoodItem extends AppCompatActivity {

    Toolbar toolbar;
    private static final int RESULT_LOAD_IMAGE = 1;
    EditText itemName, itemCategory, itemDescription, itemPrize;
    ImageView itemImage;
    TextView dragItemImage;
    Button addItem;
    Uri setImage;
    FirebaseAuth firebaseAuth;
    DatabaseReference databaseReference, itemReference;
    StorageReference storageReference;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK && data !=null)
        {
            setImage = data.getData();
            itemImage.setImageURI(setImage);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_item);

        Firebase.setAndroidContext(FoodItem.this);
        toolbar = (Toolbar) findViewById(R.id.fooditem_toolbar);
        itemName = (EditText) findViewById(R.id.item_name);
        itemCategory = (EditText) findViewById(R.id.item_category);
        itemDescription = (EditText) findViewById(R.id.item_description);
        itemPrize = (EditText) findViewById(R.id.item_prize);
        itemImage = (ImageView) findViewById(R.id.item_image);
        dragItemImage = (TextView) findViewById(R.id.add_item_drag_image);
        addItem = (Button) findViewById(R.id.add_item);
        firebaseAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference().child("Admin");
        itemReference = FirebaseDatabase.getInstance().getReference().child("Items");
        storageReference = FirebaseStorage.getInstance().getReference();

        toolbar.setTitle("Food Item");
        toolbar.inflateMenu(R.menu.menu);
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()){
                    case R.id.about:
                        Intent intent = new Intent(FoodItem.this,About.class);
                        startActivity(intent);
                        break;

                    case R.id.setting:
                        Intent intent1 = new Intent(FoodItem.this,AdminSetting.class);
                        startActivity(intent1);
                        break;

                    case R.id.help:
                        Intent intent2 = new Intent(FoodItem.this,AdminHelp.class);
                        startActivity(intent2);
                        break;

                    case R.id.logout:
                        FirebaseAuth.getInstance().signOut();
                        finish();
                        Intent intent3 = new Intent(FoodItem.this,Admin.class);
                        intent3.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        intent3.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent3);
                        break;

                    case R.id.exit:
                        AlertDialog.Builder builder = new AlertDialog.Builder(FoodItem.this);
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

        addItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name,category,description,prize;
                name = itemName.getText().toString();
                category = itemCategory.getText().toString();
                description = itemDescription.getText().toString();
                prize = itemPrize.getText().toString();
                String admin_id = firebaseAuth.getCurrentUser().getUid();
                final DatabaseReference currentAdmin = databaseReference.child(admin_id).child("Food Item").push();
                final DatabaseReference currentitem = itemReference.push();
                final DatabaseReference current_id = databaseReference.child(admin_id);
                final StorageReference filePath = storageReference.child(setImage.getLastPathSegment());
                final UploadTask uploadTask = filePath.putFile(setImage);
                uploadTask.addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                    }
                }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        filePath.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                Uri downloadUri = uri;
                                currentAdmin.child("image").setValue(downloadUri.toString());
                                currentitem.child("image").setValue(downloadUri.toString());
                            }
                        });
                    }
                });
                currentAdmin.child("name").setValue(name);
                currentAdmin.child("category").setValue(category);
                currentAdmin.child("description").setValue(description);
                currentAdmin.child("prize").setValue(prize);
                currentitem.child("name").setValue(name);
                currentitem.child("category").setValue(category);
                currentitem.child("description").setValue(description);
                currentitem.child("prize").setValue(prize);
                current_id.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        String email, hotel, location;
                        email = dataSnapshot.child("Email").getValue(String.class);
                        hotel = dataSnapshot.child("Hotel's Name").getValue(String.class);
                        location = dataSnapshot.child("Location").getValue(String.class);
                        currentitem.child("email").setValue(email);
                        currentitem.child("hotel").setValue(hotel);
                        currentitem.child("location").setValue(location);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }
        });

        dragItemImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent,RESULT_LOAD_IMAGE);
            }
        });
    }
}

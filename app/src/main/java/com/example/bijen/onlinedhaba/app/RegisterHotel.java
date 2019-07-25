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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.bijen.onlinedhaba.R;
import com.firebase.client.Firebase;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class RegisterHotel extends AppCompatActivity {

    private static final int RESULT_LOAD_IMAGE = 1;
    Toolbar toolbar;
    ImageView selectImage;
    TextView dragImage;
    Button register;
    Spinner hotelType;
    EditText managerName, managerPhoneNumber, managerOptionalPhoneNumber, managerEmailId, hotelName, hotelLocation, helpLineNumber;
    String type[] = {"Vegetarian", "Non-Vegetarian", "Mix-Food"};
    String htype;
    Uri selectedImage;
    FirebaseAuth firebaseAuth;
    DatabaseReference databaseReference;
    StorageReference storageReference;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK && data != null){
            selectedImage = data.getData();
            selectImage.setImageURI(selectedImage);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_hotel);

        Firebase.setAndroidContext(RegisterHotel.this);
        selectImage = (ImageView) findViewById(R.id.register_hotel_hotel_image);
        managerName = (EditText) findViewById(R.id.register_hotel_manager_name);
        managerEmailId = (EditText) findViewById(R.id.register_hotel_manager_email_id);
        managerPhoneNumber = (EditText) findViewById(R.id.register_hotel_manager_phone_number);
        managerOptionalPhoneNumber = (EditText) findViewById(R.id.register_hotel_manager_optional_phone_number);
        hotelName = (EditText) findViewById(R.id.register_hotel_hotel_name);
        hotelLocation = (EditText) findViewById(R.id.register_hotel_hotel_location);
        helpLineNumber = (EditText) findViewById(R.id.register_hotel_helpline_number);
        hotelType = (Spinner) findViewById(R.id.register_hotel_hotel_category);
        register = (Button) findViewById(R.id.register_hotel_register);
        dragImage = (TextView) findViewById(R.id.register_hotel_drag_image);
        firebaseAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference().child("Admin");
        storageReference = FirebaseStorage.getInstance().getReference();

        ArrayAdapter arrayAdapter = new ArrayAdapter(RegisterHotel.this,android.R.layout.simple_spinner_item,type);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        hotelType.setAdapter(arrayAdapter);

        hotelType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                htype = type[position];
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        setUpToolbar();

        dragImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent,RESULT_LOAD_IMAGE);
            }
        });

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String mName,mail,pnumber,opnumber,hname,hlocation,landlinenumber;
                mName = managerName.getText().toString();
                mail = managerEmailId.getText().toString();
                pnumber = managerPhoneNumber.getText().toString();
                opnumber = managerOptionalPhoneNumber.getText().toString();
                hname = hotelName.getText().toString();
                hlocation = hotelLocation.getText().toString();
                landlinenumber = helpLineNumber.getText().toString();
                String admin_id = firebaseAuth.getCurrentUser().getUid();
                final DatabaseReference current_admin = databaseReference.child(admin_id);
                final StorageReference filePath = storageReference.child(selectedImage.getLastPathSegment());
                final UploadTask uploadTask = filePath.putFile(selectedImage);
                uploadTask.addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                    }
                }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(final UploadTask.TaskSnapshot taskSnapshot) {
                        filePath.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                Uri downloadUrl = uri;
                                current_admin.child("Hotel Image").setValue(downloadUrl.toString());
                            }
                        });
                    }
                });
                current_admin.child("Manager's Name").setValue(mName);
                current_admin.child("Email").setValue(mail);
                current_admin.child("Phone Number").setValue(pnumber);
                current_admin.child("Another Number").setValue(opnumber);
                current_admin.child("Hotel's Name").setValue(hname);
                current_admin.child("Category").setValue(htype);
                current_admin.child("Location").setValue(hlocation);
                current_admin.child("Landline Number").setValue(landlinenumber);
            }
        });
    }

    private void setUpToolbar() {
        toolbar = (Toolbar) findViewById(R.id.register_hotel_toolbar);
        toolbar.setTitle("Register Hotel");
        toolbar.inflateMenu(R.menu.menu);
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()){
                    case R.id.about:
                        Intent intent = new Intent(RegisterHotel.this,About.class);
                        startActivity(intent);
                        break;

                    case R.id.setting:
                        Intent intent1 = new Intent(RegisterHotel.this,AdminSetting.class);
                        startActivity(intent1);
                        break;

                    case R.id.help:
                        Intent intent2 = new Intent(RegisterHotel.this,AdminHelp.class);
                        startActivity(intent2);
                        break;

                    case R.id.logout:
                        FirebaseAuth.getInstance().signOut();
                        finish();
                        Intent intent3 = new Intent(RegisterHotel.this,Admin.class);
                        intent3.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        intent3.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent3);
                        break;

                    case R.id.exit:
                        AlertDialog.Builder builder = new AlertDialog.Builder(RegisterHotel.this);
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
}

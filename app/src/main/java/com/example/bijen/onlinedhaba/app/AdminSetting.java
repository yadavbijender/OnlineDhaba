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
import android.widget.Toast;

import com.example.bijen.onlinedhaba.R;
import com.firebase.client.Firebase;
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

public class AdminSetting extends AppCompatActivity {

    private static final int RESULT_LOAD_IMAGE =1;
    Toolbar toolbar;
    TextView registerHotel, changeEmail, changePhone, deleteAccount, signOut, emailDescription, phoneDescription, passwordDescription, resetPassword;
    ImageView profilePic;
    Button dragImage, uploadImage, changeEmailButton, changePhoneButton, resetPasswordButton;
    EditText changeEmailEdit, changePhoneEdit, resetPasswordEdit;
    Uri setImage;
    FirebaseAuth firebaseAuth;
    DatabaseReference databaseReference;
    StorageReference storageReference;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK && data!=null){
            setImage = data.getData();
            profilePic.setImageURI(setImage);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_setting);

        Firebase.setAndroidContext(this);
        toolbar = (Toolbar) findViewById(R.id.admin_setting_toolbar);
        registerHotel = (TextView) findViewById(R.id.admin_setting_register_hotel);
        changeEmail = (TextView) findViewById(R.id.admin_setting_change_email);
        changePhone = (TextView) findViewById(R.id.admin_setting_change_phone);
        deleteAccount = (TextView) findViewById(R.id.admin_setting_delete_account);
        signOut = (TextView) findViewById(R.id.admin_setting_sign_out);
        emailDescription = (TextView) findViewById(R.id.admin_setting_email_description);
        phoneDescription = (TextView) findViewById(R.id.admin_setting_phone_description);
        resetPassword = (TextView) findViewById(R.id.admin_setting_reset_password);
        passwordDescription = (TextView) findViewById(R.id.admin_setting_password_description);
        profilePic = (ImageView) findViewById(R.id.admin_setting_profile_image);
        dragImage = (Button) findViewById(R.id.admin_setting_drag_image);
        uploadImage = (Button) findViewById(R.id.admin_setting_upload_image);
        changeEmailButton = (Button) findViewById(R.id.admin_setting_change_email_button);
        changePhoneButton = (Button) findViewById(R.id.admin_setting_change_phone_button);
        resetPasswordButton = (Button) findViewById(R.id.admin_setting_change_password_button);
        resetPasswordEdit = (EditText) findViewById(R.id.admin_setting_password_Edit);
        changeEmailEdit = (EditText) findViewById(R.id.admin_setting_email);
        changePhoneEdit = (EditText) findViewById(R.id.admin_setting_phone);
        firebaseAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference().child("Admin");
        storageReference = FirebaseStorage.getInstance().getReference();

        toolbar.setTitle("Setting");
        toolbar.inflateMenu(R.menu.menu);
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()){
                    case R.id.about:
                        Intent intent = new Intent(AdminSetting.this,About.class);
                        startActivity(intent);
                        break;

                    case R.id.setting:
                        Intent intent1 = new Intent(AdminSetting.this,AdminSetting.class);
                        startActivity(intent1);
                        break;

                    case R.id.help:
                        Intent intent2 = new Intent(AdminSetting.this,AdminHelp.class);
                        startActivity(intent2);
                        break;

                    case R.id.logout:
                        FirebaseAuth.getInstance().signOut();
                        finish();
                        Intent intent3 = new Intent(AdminSetting.this,Admin.class);
                        intent3.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        intent3.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent3);
                        break;

                    case R.id.exit:
                        AlertDialog.Builder builder = new AlertDialog.Builder(AdminSetting.this);
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

        dragImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadImage.setVisibility(View.VISIBLE);
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent,RESULT_LOAD_IMAGE);
            }
        });

        uploadImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String admin_id = firebaseAuth.getCurrentUser().getUid();
                final DatabaseReference currentAdmin = databaseReference.child(admin_id);
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
                                currentAdmin.child("Profile Image").setValue(downloadUri.toString());
                                uploadImage.setVisibility(View.GONE);
                                Toast.makeText(getApplicationContext(),"Profile image is uploaded",Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                });
            }
        });

        registerHotel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(AdminSetting.this,Admin.class);
                startActivity(intent1);
            }
        });

        changeEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeEmail.setVisibility(View.GONE);
                emailDescription.setVisibility(View.GONE);
                changeEmailEdit.setVisibility(View.VISIBLE);
                changeEmailButton.setVisibility(View.VISIBLE);
            }
        });

        changeEmailButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = changeEmailEdit.getText().toString();
                String admin_id = FirebaseAuth.getInstance().getCurrentUser().getUid();
                DatabaseReference currentAdmin = databaseReference.child(admin_id);
                currentAdmin.child("Email").setValue(email);
                changeEmail.setVisibility(View.VISIBLE);
                emailDescription.setVisibility(View.VISIBLE);
                changeEmailEdit.setVisibility(View.GONE);
                changeEmailButton.setVisibility(View.GONE);
                Toast.makeText(getApplicationContext(),"Your email is changed",Toast.LENGTH_SHORT).show();
            }
        });

        changePhone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changePhone.setVisibility(View.GONE);
                phoneDescription.setVisibility(View.GONE);
                changePhoneEdit.setVisibility(View.VISIBLE);
                changePhoneButton.setVisibility(View.VISIBLE);
            }
        });

        changePhoneButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String phone = changePhoneEdit.getText().toString();
                String admin_id = FirebaseAuth.getInstance().getCurrentUser().getUid();
                DatabaseReference currentAdmin = databaseReference.child(admin_id);
                currentAdmin.child("Phone Number").setValue(phone);
                changePhone.setVisibility(View.VISIBLE);
                phoneDescription.setVisibility(View.VISIBLE);
                changePhoneEdit.setVisibility(View.GONE);
                changePhoneButton.setVisibility(View.GONE);
                Toast.makeText(getApplicationContext(),"Your phone number is changed",Toast.LENGTH_SHORT).show();
            }
        });

        resetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetPassword.setVisibility(View.GONE);
                passwordDescription.setVisibility(View.GONE);
                resetPasswordEdit.setVisibility(View.VISIBLE);
                resetPasswordButton.setVisibility(View.VISIBLE);
            }
        });

        resetPasswordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String password = resetPasswordEdit.getText().toString();
                final FirebaseUser user = firebaseAuth.getCurrentUser();
                user.updatePassword(password).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()){
                            resetPassword.setVisibility(View.VISIBLE);
                            passwordDescription.setVisibility(View.VISIBLE);
                            resetPasswordEdit.setVisibility(View.GONE);
                            resetPasswordButton.setVisibility(View.GONE);
                            Toast.makeText(getApplicationContext(),"Your password is changed",Toast.LENGTH_SHORT).show();
                        }
                        else {
                            Toast.makeText(getApplicationContext(),"Your password is not changed",Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });

        deleteAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder dialog = new AlertDialog.Builder(AdminSetting.this);
                dialog.setTitle("Are you sure?");
                dialog.setMessage("Deleting this account will result in completely removing your" +
                        "account from the system and you won't be able to access the app.");
                dialog.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
                        firebaseUser.delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()){
                                    Toast.makeText(getApplicationContext(),"Deleted your account",Toast.LENGTH_SHORT).show();
                                    Intent intent3 = new Intent(AdminSetting.this,Admin.class);
                                    intent3.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                    intent3.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                    startActivity(intent3);
                                }
                                else {
                                    Toast.makeText(getApplicationContext(),task.getException().getMessage(),Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    }
                });
                dialog.setNegativeButton("Dismiss", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                AlertDialog alertDialog = dialog.create();
                alertDialog.show();
            }
        });

        signOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                finish();
                Intent intent2 = new Intent(AdminSetting.this,Admin.class);
                intent2.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent2.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent2);
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
                String image = dataSnapshot.child("Profile Image").getValue(String.class);
                Picasso.with(AdminSetting.this)
                        .load(image)
                        .into(profilePic);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}

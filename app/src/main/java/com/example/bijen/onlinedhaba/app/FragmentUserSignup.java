package com.example.bijen.onlinedhaba.app;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.bijen.onlinedhaba.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class FragmentUserSignup extends Fragment {

    View view;
    EditText usemail, uspassword, usname;
    Button button;
    FirebaseAuth firebaseAuth;
    DatabaseReference databaseReference;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.user_signup,container,false);
        usemail = (EditText) view.findViewById(R.id.user_signup_email);
        usname = (EditText) view.findViewById(R.id.user_signup_username);
        uspassword = (EditText) view.findViewById(R.id.user_signup_password);
        button = (Button) view.findViewById(R.id.user_signup);
        firebaseAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference().child("User");

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UserSignUp();
            }
        });
        return view;
    }

    private void UserSignUp() {
        final String uemail,upassword,uname;
        uemail = usemail.getText().toString();
        uname = usname.getText().toString();
        upassword = uspassword.getText().toString();
        if (TextUtils.isEmpty(uemail) || TextUtils.isEmpty(upassword) || TextUtils.isEmpty(uname)){
            Toast.makeText(getContext(),"Some field is blank",Toast.LENGTH_SHORT).show();
        }
        else if (upassword.length()<=6){
            Toast.makeText(getContext(),"Password is too small",Toast.LENGTH_SHORT).show();
        }
        else {
            firebaseAuth.createUserWithEmailAndPassword(uemail,upassword).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()){
                        String user_id = firebaseAuth.getCurrentUser().getUid();
                        final DatabaseReference current_user = databaseReference.child(user_id);
                        current_user.child("User_name").setValue(uname);
                        current_user.child("Email").setValue(uemail);
                        Intent intent = new Intent(getContext(),HomeUser.class);
                        startActivity(intent);
                        Toast.makeText(getContext(),"register",Toast.LENGTH_SHORT).show();
                    }
                    else {
                        Toast.makeText(getContext(),"You are not a valid authentication",Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }

    public FragmentUserSignup() {
    }
}

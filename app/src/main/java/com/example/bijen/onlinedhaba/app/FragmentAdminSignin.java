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
import com.firebase.client.Firebase;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class FragmentAdminSignin extends Fragment {

    View view;
    EditText saemail, sapassword;
    Button button;
    FirebaseAuth firebaseAuth;

    public FragmentAdminSignin() {
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.admin_sign_in,container,false);
        saemail = (EditText) view.findViewById(R.id.admin_signup_email);
        sapassword = (EditText) view.findViewById(R.id.admin_signup_password);
        button = (Button) view.findViewById(R.id.admin_signup);
        firebaseAuth = FirebaseAuth.getInstance();
        Firebase.setAndroidContext(getContext());

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Sign_up();
            }
        });
        return view;
    }

    private void Sign_up() {

        String email,password;
        email = saemail.getText().toString();
        password = sapassword.getText().toString();
        if (TextUtils.isEmpty(email) || TextUtils.isEmpty(password)){
            Toast.makeText(getContext(),"Some Field is blank",Toast.LENGTH_SHORT).show();
        }
        else if (password.length()<=6){
            Toast.makeText(getContext(),"Password is too small",Toast.LENGTH_SHORT).show();
        }
        else {
            firebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        Intent intent = new Intent(getContext(), RegisterHotel.class);
                        startActivity(intent);
                    } else {
                        Toast.makeText(getContext(), "Your are not a valid authentication", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }
}

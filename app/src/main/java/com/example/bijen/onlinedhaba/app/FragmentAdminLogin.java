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
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class FragmentAdminLogin extends Fragment {

    View view;
    EditText laemail, lapassword;
    Button button;
    FirebaseAuth firebaseAuth;

    public FragmentAdminLogin() {
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.admin_login,container,false);
        laemail = (EditText) view.findViewById(R.id.admin_login_email);
        lapassword = (EditText) view.findViewById(R.id.admin_login_passward);
        button = (Button) view.findViewById(R.id.admin_login);
        firebaseAuth = FirebaseAuth.getInstance();
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                log_in();
            }
        });
        return view;
    }

    private void log_in() {
        String email,password;
        email = laemail.getText().toString();
        password = lapassword.getText().toString();
        if (TextUtils.isEmpty(email)){
            Toast.makeText(getContext(),"Email field is empty",Toast.LENGTH_SHORT).show();
        }
        else if (TextUtils.isEmpty(password)){
            Toast.makeText(getContext(),"Password field is empty",Toast.LENGTH_SHORT).show();
        }
        else {
            firebaseAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()){
                        Intent intent = new Intent(getContext(),Home.class);
                        startActivity(intent);
                    }
                    else {
                        Toast.makeText(getContext(),"Your email id or password is worng",Toast.LENGTH_SHORT).show();
                    }
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(getContext(),"First You Should create an account",Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
}

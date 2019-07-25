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

public class FragmentUserLogin extends Fragment{

    View view;
    EditText luemail, lupassword;
    Button button;
    FirebaseAuth firebaseAuth;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.user_login,container,false);
        luemail = (EditText) view.findViewById(R.id.user_login_email);
        lupassword = (EditText) view.findViewById(R.id.user_login_passward);
        button = (Button) view.findViewById(R.id.user_login);
        firebaseAuth = FirebaseAuth.getInstance();

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userLogin();
            }
        });
        return view;
    }

    private void userLogin() {
        String uemail, upassword;
        uemail = luemail.getText().toString();
        upassword = lupassword.getText().toString();
        if (TextUtils.isEmpty(uemail)){
            Toast.makeText(getContext(),"Email field is empty",Toast.LENGTH_SHORT).show();
        }
        else if (TextUtils.isEmpty(upassword)){
            Toast.makeText(getContext(),"Password field is empty",Toast.LENGTH_SHORT).show();
        }
        else {
            firebaseAuth.signInWithEmailAndPassword(uemail,upassword).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()){
                        Toast.makeText(getContext(),"success",Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(getContext(),HomeUser.class);
                        startActivity(intent);
                    }
                    else {
                        Toast.makeText(getContext(),"Your email or password is wrong",Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }

    public FragmentUserLogin() {
    }
}

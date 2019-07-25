package com.example.bijen.onlinedhaba.app;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.bijen.onlinedhaba.R;

public class MainActivity extends AppCompatActivity {

    Button buttonAdmin, buttonUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        buttonAdmin = (Button) findViewById(R.id.admin);
        buttonUser = (Button) findViewById(R.id.user);

        buttonAdmin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this,Admin.class);
                startActivity(intent);
            }
        });

        buttonUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent1 = new Intent(MainActivity.this,User.class);
                startActivity(intent1);
            }
        });
    }
}

package com.example.smartweather;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class register extends AppCompatActivity {
    Database dbHelper = new Database(register.this);
    EditText txtUsername, txtPassword;
    Button button3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        button3 = findViewById(R.id.button4);
        button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                txtUsername = findViewById(R.id.emailE2);
                txtPassword = findViewById(R.id.pass2);
                String username = txtUsername.getText().toString();
                String password = txtPassword.getText().toString();
                Toast.makeText(register.this,"Registered Try Login",Toast.LENGTH_SHORT).show();
                dbHelper.register(username, password);
             Intent intent = new Intent(register.this, login.class);
             startActivity(intent);
            }
        });

    }
}
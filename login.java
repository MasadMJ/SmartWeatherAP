package com.example.smartweather;


import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Build;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

public class login extends AppCompatActivity {
    EditText txtUsername, txtPassword;
    Button btnLogin,button3;
    Database dbHelper = new Database(login.this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        txtUsername = findViewById(R.id.emailE);
        txtPassword = findViewById(R.id.pasteE);
        btnLogin = findViewById(R.id.button2);
        button3 = findViewById(R.id.button3);

        button3.setOnClickListener(new View.OnClickListener() {
            @Override
        public void onClick(View view) {
                Intent intent = new Intent(login.this, register.class);
                startActivity(intent);
            }
        });


        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String username = txtUsername.getText().toString();
                String password = txtPassword.getText().toString();

                // Check if username, password is filled
                try {

                        if ( dbHelper.searchData(username,password)) {

                            Intent intent = new Intent(login.this, MainActivity.class);
                            startActivity(intent);


                        } else {
                            Toast.makeText(login.this,"Wrong User",Toast.LENGTH_SHORT).show();


                        }
                } catch (Exception e) {
                    Toast.makeText(login.this, e.getMessage(), Toast.LENGTH_LONG).show();
                }
            }
        });



    }
}
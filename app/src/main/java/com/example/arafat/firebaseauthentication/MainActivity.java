package com.example.arafat.firebaseauthentication;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private EditText userEamil, userPassword;
    private Button loginButton, logoutButton, addItemButton, viewDatabaseContentButton;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthStateListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        userEamil = findViewById(R.id.email_edit_text);
        userPassword = findViewById(R.id.password_edit_text);
        loginButton = findViewById(R.id.login_button);
        logoutButton = findViewById(R.id.logout_button);
        addItemButton = findViewById(R.id.add_item);
        viewDatabaseContentButton = findViewById(R.id.button_view_database_content);

        mAuth = FirebaseAuth.getInstance();



        mAuthStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if(user != null) {
                    //user is signed in
                    Toast.makeText(MainActivity.this, "Signin Successful", Toast.LENGTH_SHORT).show();
                } else {
                    // user is signed out
                    Toast.makeText(MainActivity.this, "signout successfull", Toast.LENGTH_SHORT).show();
                }
            }
        };

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final String email = userEamil.getText().toString().trim();
                final String password = userPassword.getText().toString().trim();

                if(email.equals("") && password.equals("")) {
                    Toast.makeText(MainActivity.this, "Enter Information", Toast.LENGTH_SHORT).show();
                } else  {
                    mAuth.signInWithEmailAndPassword(email, password);
                }
            }
        });

        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mAuth.signOut();
            }
        });

        addItemButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, AddItem.class));
            }
        });

        viewDatabaseContentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //////
                startActivity(new Intent(MainActivity.this, ViewDatabase.class));

            }
        });

        
        

    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        mAuth.addAuthStateListener(mAuthStateListener);

    }

    @Override
    protected void onStop() {
        super.onStop();
        if(mAuthStateListener != null) {
            mAuth.removeAuthStateListener(mAuthStateListener);
        }
    }
}

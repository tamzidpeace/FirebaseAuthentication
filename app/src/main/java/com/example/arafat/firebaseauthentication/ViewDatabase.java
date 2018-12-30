package com.example.arafat.firebaseauthentication;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ViewDatabase extends AppCompatActivity {

    private static final String TAG = "ViewDatabase";
    private TextView viewDatabaseContent, showItem;
    private ListView listView;

    private FirebaseDatabase mFirebaseDatabase;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthStateListener;
    private DatabaseReference myRef;

    private String userID;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_database);

        viewDatabaseContent = findViewById(R.id.text_view_view_database_content);
        showItem = findViewById(R.id.show_item);
        listView = findViewById(R.id.list_view_for_database_content);

        mAuth = FirebaseAuth.getInstance();
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        myRef = mFirebaseDatabase.getReference();

        FirebaseUser user = mAuth.getCurrentUser();
        userID = user.getUid();

        mAuthStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    //user is signed in
                    Toast.makeText(ViewDatabase.this, "Signin Successful", Toast.LENGTH_SHORT).show();
                } else {
                    // user is signed out
                    Toast.makeText(ViewDatabase.this, "signout successfull", Toast.LENGTH_SHORT).show();
                }
            }
        };

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                showData(dataSnapshot);
            }


            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void showData(DataSnapshot dataSnapshot) {
        for (DataSnapshot ds : dataSnapshot.getChildren()) {

            UserInformation userInformation = new UserInformation();
            userInformation.setName(ds.child(userID).getValue(UserInformation.class).getName());
            userInformation.setEmail(ds.child(userID).getValue(UserInformation.class).getEmail());
            userInformation.setPhone_num(ds.child(userID).getValue(UserInformation.class).getPhone_num());

            ArrayList<String> array = new ArrayList<>();
            array.add(userInformation.getName());
            array.add(userInformation.getEmail());
            array.add(userInformation.getPhone_num());
            ArrayAdapter adapter = new ArrayAdapter(this, R.layout.simple_list_item_1, array);
            listView.setAdapter(adapter);
            /*String info = userInformation.getName() + "\n" +
                    userInformation.getEmail() + "\n" +
                    userInformation.getEmail();

            showItem.setText(info);*/
        }
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
        if (mAuthStateListener != null) {
            mAuth.removeAuthStateListener(mAuthStateListener);
        }
    }
}

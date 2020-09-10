package com.example.snapchatclone;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Objects;

public class MainActivity extends AppCompatActivity {
    EditText userName;
    EditText userPassword;
    private FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_main);
        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();
        userName= findViewById(R.id.userName);
        userPassword=findViewById(R.id.userPassword);

        if(mAuth.getCurrentUser()!=null){
            logIn();
        }
    }
    public void gotClicked(View view){
        //SignIn
        mAuth.signInWithEmailAndPassword(userName.getText().toString(), userPassword.getText().toString())
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            //login
                            logIn();
                        } else {
                            //signup
                            mAuth.createUserWithEmailAndPassword(userName.getText().toString(), userPassword.getText().toString())
                                    .addOnCompleteListener(MainActivity.this, new OnCompleteListener<AuthResult>() {
                                        @Override
                                        public void onComplete(@NonNull Task<AuthResult> task) {
                                            if (task.isSuccessful()) {
                                                // Sign in success, update UI with the signed-in user's information
                                                FirebaseDatabase.getInstance().getReference().child("user").child(task.getResult().getUser().getUid()).child("email").setValue(userName.getText().toString());
                                            } else {
                                                // If sign in fails, display a message to the user.
                                                Toast.makeText(MainActivity.this, "Login Failed", Toast.LENGTH_SHORT).show();
                                            }

                                            // ...
                                        }
                                    });

                        }

                        // ...
                    }
                });


    }

    public void logIn(){
        //Load Info
        Intent intent=new Intent(getApplicationContext(),SnapsActivity.class);
        startActivity(intent);
    }

}
package com.example.snapchatclone;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class UserActivity extends AppCompatActivity {

    ListView userListView;
    ArrayList<String> emails;
    ArrayList<String> keys;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);
        userListView=findViewById(R.id.chooseUserListView);
        emails=new ArrayList<>();
        keys=new ArrayList<>();
        final ArrayAdapter<String> arrayAdapter=new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,emails);
        userListView.setAdapter(arrayAdapter);



        Object childEventListener;
        //to fill emails from database
        FirebaseDatabase.getInstance().getReference().child("user").addChildEventListener((ChildEventListener) (childEventListener=new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
               String email=snapshot.child("email").getValue().toString();
               emails.add(email);
               keys.add(snapshot.getKey());
               arrayAdapter.notifyDataSetChanged();
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {}

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {}

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {}

            @Override
            public void onCancelled(@NonNull DatabaseError error) {}
        }));

             userListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                 @Override
                 public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                     Map<String,String> map=new HashMap<>();
                     Bundle bundle = getIntent().getExtras();

                     map.put("from",bundle.getString("from"));
                     map.put("imageName",bundle.getString("imageName"));
                     map.put("imageUrl",bundle.getString("imageUrl"));
                     map.put("message",bundle.getString("message"));
                     FirebaseDatabase.getInstance().getReference().child("user").child(keys.get(i)).child("snaps").push().setValue(map);
                     Intent k=new Intent(getApplicationContext(),SnapsActivity.class);
                    /* k.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);//wipe everything out of back button
                     startActivity(k);
                     //finish();*/
                 }
             });

    }


}
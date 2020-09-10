package com.example.snapchatclone;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class SnapsActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    ListView snapsListView;
    List<String> snapsList;
    List<DataSnapshot> snapShot;
    String from;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater=getMenuInflater();
        menuInflater.inflate(R.menu.snap_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_snaps);
         mAuth = FirebaseAuth.getInstance();
        from=mAuth.getCurrentUser().getEmail();
        snapsListView=findViewById(R.id.snapsListView);
        snapsList=new ArrayList<>();
        snapShot=new ArrayList<>();
        final ArrayAdapter<String> arrayAdapter=new ArrayAdapter<>(this,android.R.layout.simple_list_item_1,snapsList);
        snapsListView.setAdapter(arrayAdapter);


        Object childEventListener;
        //to fill snapsList from database
        FirebaseDatabase.getInstance().getReference().child("user").child(mAuth.getCurrentUser().getUid()).child("snaps").addChildEventListener((ChildEventListener) (childEventListener=new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                    String snaps=snapshot.child("from").getValue().toString();
                    snapsList.add(snaps);
                    snapShot.add(snapshot);
                    arrayAdapter.notifyDataSetChanged();
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {}

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {
                int j=0;
                for(DataSnapshot snap:snapShot){
                    if(snap.getKey().equals(snapshot.getKey())){
                       snapShot.remove(j);
                       snapsList.remove(j);

                    }
                    j++;

                }
                arrayAdapter.notifyDataSetChanged();
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {}

            @Override
            public void onCancelled(@NonNull DatabaseError error) {}
        }));
        snapsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                DataSnapshot snapshot=snapShot.get(i);
                Intent intent=new Intent(getApplicationContext(),viewSnapActivity.class);
                intent.putExtra("imageName",snapshot.child("imageName").getValue().toString());
                intent.putExtra("imageUrl",snapshot.child("imageUrl").getValue().toString());
                intent.putExtra("message",snapshot.child("message").getValue().toString());
                intent.putExtra("snapKey",snapshot.getKey());
                startActivity(intent);

            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.creatsnap:
                //open camera
                Intent intent=new Intent(getApplicationContext(),cameraActivity.class);
                intent.putExtra("from",from);
                startActivity(intent);
            case R.id.logout:
                //logout
                mAuth.signOut();
                finish();
            default:return false;
        }

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        mAuth.signOut();
    }
}
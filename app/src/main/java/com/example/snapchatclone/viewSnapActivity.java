package com.example.snapchatclone;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

public class viewSnapActivity extends AppCompatActivity {
    TextView messageView;
    ImageView snapView;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_snap);
         messageView=findViewById(R.id.messageView);
         snapView=findViewById(R.id.snapView);
         mAuth = FirebaseAuth.getInstance();
         /*WebView webView=findViewById(R.id.snapView);*/
         messageView.setText(getIntent().getExtras().getString("message"));
        /* webView.getSettings().setJavaScriptEnabled(true);
         webView.setWebViewClient(new WebViewClient());
         webView.loadUrl(getIntent().getExtras().getString("imageUrl"));*/

        ImageDownloader task=new ImageDownloader();
        Bitmap myImage;
        try{
            myImage = task.execute(getIntent().getExtras().getString("imageUrl")).get();
            snapView.setImageBitmap(myImage);}
        catch(Exception e)
        {e.printStackTrace();}


    }



    public class ImageDownloader extends AsyncTask<String,Void, Bitmap>{

        @Override
        protected Bitmap doInBackground(String... urls) {
            try {
                URL url=new URL(urls[0]);
                HttpURLConnection connection =(HttpURLConnection) url.openConnection();
                connection.connect();
                InputStream in=connection.getInputStream();
                Bitmap myBitmap= BitmapFactory.decodeStream(in);
                return myBitmap;

            } catch (Exception e) {
                e.printStackTrace();
                return  null;
            }
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        FirebaseDatabase.getInstance().getReference().child("user").child(mAuth.getCurrentUser().getUid()).child("snaps").child(getIntent().getExtras().getString("snapKey")).removeValue();
        FirebaseStorage.getInstance().getReference().child("images").child(getIntent().getExtras().getString("imageName")).delete();
    }
}
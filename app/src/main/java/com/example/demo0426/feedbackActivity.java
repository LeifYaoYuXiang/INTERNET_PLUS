package com.example.demo0426;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.FileNotFoundException;

public class feedbackActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);

        Intent intent=getIntent();
        Bundle bundle=intent.getExtras();

        String imageUri=bundle.getString("imageUri");

        try {
            Bitmap bitmap= BitmapFactory.decodeStream(getContentResolver().openInputStream(Uri.parse(imageUri)));

            ImageView imageView=findViewById(R.id.photoTaken);
            imageView.setImageBitmap(bitmap);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        String[] feedback=HttpClientHelper.sendWithOKHttpArea(2,Uri.parse(imageUri));
        String id=feedback[0];
        String image=feedback[1];

        Log.i("TAG","In the feedback I have received id:"+id+" and image:"+image);

    }
}

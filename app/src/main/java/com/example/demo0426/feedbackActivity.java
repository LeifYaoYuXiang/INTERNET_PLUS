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
        String possibility=bundle.getString("Possibility");
        String area=bundle.getString("AreaInformation");

        try {
            Bitmap bitmap= BitmapFactory.decodeStream(getContentResolver().openInputStream(Uri.parse(imageUri)));

            ImageView imageView=findViewById(R.id.photoTaken);
            imageView.setImageBitmap(bitmap);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        TextView textView=findViewById(R.id.areaPossibility);
        textView.setText("有"+possibility+"%的概率在"+area);


    }
}

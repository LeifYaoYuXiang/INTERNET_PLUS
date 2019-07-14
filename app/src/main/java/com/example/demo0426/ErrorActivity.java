package com.example.demo0426;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class ErrorActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_error);

        Intent intent=getIntent();
        Bundle bundle=intent.getExtras();

        int errorCode=bundle.getInt("ErrorCode");
        String errorType=bundle.getString("ErrorType");
        String errorMessage=bundle.getString("ErrorMessage");

        TextView errorHint=findViewById(R.id.errorHint);
        errorHint.setText("ErrorCode:"+errorCode+"\nErrorType:"+errorType+"\nErrorMessage"+errorMessage);
    }
}

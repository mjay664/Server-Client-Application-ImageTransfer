package com.example.mjay.myapplication;

import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

public class Main3Activity extends AppCompatActivity {
    ImageView img;
    Button b1;
    Button b2;
    Bitmap set_image;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setTitle("Preview Image");
        setContentView(R.layout.activity_main3);
        getConnected();
    }

    public void getConnected()
    {

        img = (ImageView) findViewById(R.id.imageView);
        b1 = (Button) findViewById(R.id.button);
        b2 = (Button) findViewById(R.id.button2);

        img.setImageBitmap(ImageContainer.getBitmapImage());

        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ImageContainer.setBoolValue(true);
                finish();
            }
        });

        b2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ImageContainer.setBoolValue(false);
                finish();
            }
        });
    }
}

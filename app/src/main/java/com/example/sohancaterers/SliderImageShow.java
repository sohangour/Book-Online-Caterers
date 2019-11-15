package com.example.sohancaterers;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.widget.ImageView;

public class SliderImageShow extends AppCompatActivity {
private ImageView imageView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_slider_image_show);
        imageView=findViewById(R.id.image_show);
        Intent intent=getIntent();
        Bundle bundle=this.getIntent().getExtras();
        int pic=bundle.getInt("image");
        imageView.setImageResource(pic);
    }
}

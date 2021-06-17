package com.sorrybro.app;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.widget.ImageView;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ImageView imgView = findViewById(R.id.imageView);
        Drawable drawable = LoadImageFromWebOperations("https://picsum.photos/200/300");
        imgView.setImageDrawable(drawable);
    }
    private Drawable LoadImageFromWebOperations(String url)
    {
            try {
                InputStream is = (InputStream) new URL(url).getContent();
                return Drawable.createFromStream(is, "src name");
            } catch (Exception e) {
                System.out.println("Exc="+e);
                return null;
            }
    }
}
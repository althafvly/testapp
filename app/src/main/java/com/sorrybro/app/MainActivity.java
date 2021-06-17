package com.sorrybro.app;

import android.content.Context;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.io.File;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final File cacheDir = new File(android.os.Environment.getExternalStorageDirectory(),"TestAppImages");

        int loader = R.drawable.dummy;
        ImageView image = findViewById(R.id.image);
        String image_url = "https://picsum.photos/720";
        ImageLoader imgLoader = new ImageLoader(getApplicationContext());

        if(cacheDir.exists()) {
            String[] files = cacheDir.list();
            if (files.length != 0) {
                imgLoader.DisplayImage(image_url, loader, image);
            }
        }

        ShowWarning();

        Button button = findViewById(R.id.button);
        button.setOnClickListener(v -> {
            ShowWarning();
            imgLoader.clearCache();
            imgLoader.DisplayImage(image_url, loader, image);
        });
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    private void ShowWarning() {
        TextView textimage = findViewById(R.id.textView);
        if(!isNetworkAvailable()) {
            Toast toast = Toast.makeText(getApplicationContext(),
                    R.string.no_network_error,
                    Toast.LENGTH_SHORT);
            toast.show();
            textimage.setText(R.string.old_image_warning);
        } else {
            textimage.setText("");
        }
    }
}
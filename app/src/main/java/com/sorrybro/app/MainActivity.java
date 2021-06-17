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

        int loader = R.drawable.dummy;
        ImageView image = findViewById(R.id.image);
        String image_url = "https://picsum.photos/720";
        ImageLoader imgLoader = new ImageLoader(getApplicationContext());

        if(!isDirectoryEmpty()) {
            imgLoader.DisplayImage(image_url, loader, image);
            ShowImageWarning();
        }

        ShowNetworkWarning();

        Button button = findViewById(R.id.button);
        button.setOnClickListener(v -> {
            ShowNetworkWarning();
            ShowImageWarning();
            if(isNetworkAvailable()) {
                imgLoader.clearCache();
            }
            imgLoader.DisplayImage(image_url, loader, image);
        });
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    private void ShowImageWarning() {
        TextView textimage = findViewById(R.id.textView);
        if(!isNetworkAvailable()) {
            textimage.setText(R.string.old_image_warning);
        } else {
            textimage.setText("");
        }
    }

    private void ShowNetworkWarning() {
        if(!isNetworkAvailable()) {
            Toast toast = Toast.makeText(getApplicationContext(),
                    R.string.no_network_error,
                    Toast.LENGTH_SHORT);
            toast.show();
        }
    }

    private boolean isDirectoryEmpty() {
        final File cacheDir = new File(android.os.Environment.getExternalStorageDirectory(),"TestAppImages");
        if(cacheDir.exists()) {
            String[] files = cacheDir.list();
            return files.length == 0;
        }
        return false;
    }
}
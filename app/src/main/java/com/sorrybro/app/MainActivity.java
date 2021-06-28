package com.sorrybro.app;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Build.VERSION;
import android.provider.Settings;
import android.widget.Button;
import android.widget.ImageView;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.material.snackbar.Snackbar;

import java.io.File;

public class MainActivity extends AppCompatActivity {

    private static final int STORAGE_PERMISSION_CODE = 200;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        int loader = R.drawable.dummy;
        ImageView image = findViewById(R.id.image);
        String image_url = "https://picsum.photos/720";
        ImageLoader imgLoader = new ImageLoader(getApplicationContext());

        checkPermission(Manifest.permission.READ_EXTERNAL_STORAGE, STORAGE_PERMISSION_CODE);

        if(ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) ==
                PackageManager.PERMISSION_GRANTED) {
            if (!isDirectoryEmpty()) {
                imgLoader.DisplayImage(image_url, loader, image);
                showImageWarning();
            }
        }

        showNetworkWarning();

        Button button = findViewById(R.id.button);
        button.setOnClickListener(v -> {
            showNetworkWarning();
            showImageWarning();
            if(isNetworkAvailable()) {
                if (VERSION.SDK_INT >= 30) {
                    checkforPermission();
                } else {
                    checkPermission(Manifest.permission.READ_EXTERNAL_STORAGE, STORAGE_PERMISSION_CODE);
                }
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

    private void showImageWarning() {
        TextView textimage = findViewById(R.id.textView);
        if(!isNetworkAvailable()) {
            textimage.setText(R.string.old_image_warning);
        } else {
            textimage.setText("");
        }
    }

    private void showNetworkWarning() {
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
            assert files != null;
            return files.length == 0;
        }
        return false;
    }

    public void checkPermission(String permission, int requestCode)
    {
        // Checking if permission is not granted
        if (ContextCompat.checkSelfPermission(MainActivity.this, permission) == PackageManager.PERMISSION_DENIED) {
            ActivityCompat.requestPermissions(MainActivity.this, new String[] { permission }, requestCode);
        }
    }

    public void checkforPermission() {
        if (VERSION.SDK_INT >= 30) {
            if (!Environment.isExternalStorageManager()) {
                Snackbar.make(findViewById(android.R.id.content), "Permission needed!", Snackbar.LENGTH_INDEFINITE)
                        .setAction("Settings", v -> {
                            try {
                                Uri uri = Uri.parse("package:" + BuildConfig.APPLICATION_ID);
                                Intent intent = new Intent(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION, uri);
                                startActivity(intent);
                            } catch (Exception ex) {
                                Intent intent = new Intent();
                                intent.setAction(Settings.ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION);
                                startActivity(intent);
                            }
                        })
                        .show();
            }
        }
    }
}
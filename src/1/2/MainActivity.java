package com.example.whatsapp_saver;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final int REQUEST_PERMISSIONS = 1;
    private RecyclerView recyclerView;
    private StatusAdapter statusAdapter;
    private List<File> statusFiles;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = findViewById(R.id.recyclerView);
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);

        if (checkPermissions()) {
            loadStatuses();
        } else {
            requestPermissions();
        }

        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.nav_all:
                    statusAdapter.filter(StatusAdapter.FilterType.ALL);
                    return true;
                case R.id.nav_images:
                    statusAdapter.filter(StatusAdapter.FilterType.IMAGES);
                    return true;
                case R.id.nav_videos:
                    statusAdapter.filter(StatusAdapter.FilterType.VIDEOS);
                    return true;
                default:
                    return false;
            }
        });
    }

    private boolean checkPermissions() {
        int readPermission = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE);
        int writePermission = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        return readPermission == PackageManager.PERMISSION_GRANTED && writePermission == PackageManager.PERMISSION_GRANTED;
    }

    private void requestPermissions() {
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_PERMISSIONS);
    }

    private void loadStatuses() {
        File statusDir = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/WhatsApp/Media/.Statuses");
        if (statusDir.exists() && statusDir.isDirectory()) {
            statusFiles = new ArrayList<>();
            File[] files = statusDir.listFiles();
            if (files != null) {
                for (File file : files) {
                    if (file.getName().endsWith(".jpg") || file.getName().endsWith(".mp4")) {
                        statusFiles.add(file);
                    }
                }
            }
            statusAdapter = new StatusAdapter(statusFiles);
            recyclerView.setAdapter(statusAdapter);
        } else {
            Toast.makeText(this, "No statuses found", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == REQUEST_PERMISSIONS) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                loadStatuses();
            } else {
                Toast.makeText(this, "Permissions Denied", Toast.LENGTH_SHORT).show();
            }
        }
    }
  }

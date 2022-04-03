package com.example.birthdayer;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.google.gson.Gson;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    private final ActivityResultLauncher<String> requestPermissionLauncher =
            registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted -> {
                if (isGranted) {
                    Log.i("Permission", "Granted");
                } else {
                    Log.i("Permission", "Denied");
                }
            });

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        File file = new File(this.getFilesDir(), "BirthdayData.txt");

        String text = "1600 Pennsylvania Ave NW";
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(file);
            fos.write(text.getBytes());
            fos.close();
        } catch (FileNotFoundException e) {
            Log.e("FileNotfound", "File in main not found." + file.getAbsolutePath());
        } catch (IOException e) {
            e.printStackTrace();
        }

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void toMapScreen(View view) {
        Intent intent = new Intent(this, BirthdayMapActivity.class);

        if(requestLocationPermission()){
            startActivity(intent);
        }
    }

    private boolean requestLocationPermission(){
        if (ContextCompat.checkSelfPermission(
                this, Manifest.permission.ACCESS_FINE_LOCATION) ==
                PackageManager.PERMISSION_GRANTED) {
            return true;
        } else if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION)) {
            ActivityCompat.requestPermissions(this, new String[]{
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION,},1);
            return false;
        } else {
            requestPermissionLauncher.launch(
                    Manifest.permission.ACCESS_FINE_LOCATION);
            return false;
        }
    }

    public void toListScreen(View view){
        Intent intent = new Intent(this, BirthdayListActivity.class);
        startActivity(intent);
    }

}
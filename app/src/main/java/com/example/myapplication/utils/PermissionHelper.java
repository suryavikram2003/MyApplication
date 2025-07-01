package com.example.myapplication.utils;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

public class PermissionHelper {
    
    public static final int LOCATION_PERMISSION_REQUEST = 100;
    public static final int CAMERA_PERMISSION_REQUEST = 101;
    public static final int CALL_PERMISSION_REQUEST = 102;
    public static final int STORAGE_PERMISSION_REQUEST = 103;
    
    // Location Permissions
    public static boolean hasLocationPermission(Context context) {
        return ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED ||
               ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED;
    }
    
    public static void requestLocationPermission(Activity activity) {
        ActivityCompat.requestPermissions(activity, 
            new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 
            LOCATION_PERMISSION_REQUEST);
    }
    
    // Camera Permission
    public static boolean hasCameraPermission(Context context) {
        return ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED;
    }
    
    public static void requestCameraPermission(Activity activity) {
        ActivityCompat.requestPermissions(activity, 
            new String[]{Manifest.permission.CAMERA}, 
            CAMERA_PERMISSION_REQUEST);
    }
    
    // Call Permission
    public static boolean hasCallPermission(Context context) {
        return ContextCompat.checkSelfPermission(context, Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED;
    }
    
    public static void requestCallPermission(Activity activity) {
        ActivityCompat.requestPermissions(activity, 
            new String[]{Manifest.permission.CALL_PHONE}, 
            CALL_PERMISSION_REQUEST);
    }
    
    // Storage Permission
    public static boolean hasStoragePermission(Context context) {
        return ContextCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED;
    }
    
    public static void requestStoragePermission(Activity activity) {
        ActivityCompat.requestPermissions(activity, 
            new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE}, 
            STORAGE_PERMISSION_REQUEST);
    }
}
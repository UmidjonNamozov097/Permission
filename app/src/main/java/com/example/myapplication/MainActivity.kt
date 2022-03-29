package com.example.myapplication

import android.Manifest
import android.app.AlertDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.myapplication.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.feature1Button.setOnClickListener {
            if (ContextCompat.checkSelfPermission(this,Manifest.permission.CAMERA) ==
                    PackageManager.PERMISSION_GRANTED){
                onCameraPermissionGranted()
            } else{
                ActivityCompat.requestPermissions(
                    this, arrayOf(Manifest.permission.CAMERA),
                    RQ_PERMISSIONS_FOR_FEATURE_1_CODE
                )
            }
        }
        binding.feature2Button.setOnClickListener {

        }
    }

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when(requestCode){
            RQ_PERMISSIONS_FOR_FEATURE_1_CODE ->{
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    onCameraPermissionGranted()
                } else {
                    if (shouldShowRequestPermissionRationale(Manifest.permission.CAMERA)){
                        askUserForOpeningAppSettings()
                    }else{

                    }
                }
            }
        }
    }

    private fun askUserForOpeningAppSettings() {
        val appSettingsIntent = Intent(
            Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
            Uri.fromParts("package",packageName,null)
        )
        if (packageManager.resolveActivity(appSettingsIntent,PackageManager.MATCH_DEFAULT_ONLY) == null){
            Toast.makeText(this, "Permissions are denied forever", Toast.LENGTH_SHORT).show()
        }else{
            AlertDialog.Builder(this)
                .setTitle("Permissions denied")
                .setMessage("You have denied permissions forever. "+
                "You can change your decision in app settings. \n\n"+
                "Would you like to open app settingd ?")
                .setPositiveButton("Open"){_, _ ->
                    startActivity(appSettingsIntent)
                }
                .create()
                .show()
        }
    }

    private fun onCameraPermissionGranted(){
        Toast.makeText(this, "Camera permission is grated", Toast.LENGTH_SHORT).show()
    }
    private companion object{
        const val RQ_PERMISSIONS_FOR_FEATURE_1_CODE = 1
        const val RQ_PERMISSIONS_FOR_FEATURE_2_CODE = 2
    }
}
package com.example.shopscrapping

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.app.AlertDialog
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.content.ContextCompat
import com.example.shopscrapping.ui.theme.ShopScrappingTheme
import com.example.shopscrapping.ui.ShopScrappingApp

class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ShopScrappingTheme() {
                // A surface container using the 'background' color from the theme

//                requestPermissions()
                Surface{
                    val windowSize = calculateWindowSizeClass(this)
                    val activity: Activity = this
                    requestNotificationPermission()
                    ShopScrappingApp(
                        context = applicationContext,
                        windowSize = windowSize.widthSizeClass,
                        activity = activity
                    )
                }
            }
        }
    }

    /**
     * An ActivityResultLauncher for requesting notification permission.
     * It logs whether the user granted or denied permission.
     */
    @SuppressLint("all")
    private var requestPermissionLauncher: ActivityResultLauncher<String> =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
            if (!isGranted) {
                Log.d("POST_NOTIFICATION_PERMISSION", "USER DENIED PERMISSION")
            } else {
                Log.d("POST_NOTIFICATION_PERMISSION", "USER GRANTED PERMISSION")
            }
        }
    /**
     * Requests notification permission if it's not granted.
     * Shows a toast message indicating the permission status.
     */
    private fun requestNotificationPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            val permission = Manifest.permission.POST_NOTIFICATIONS
            when {
                ContextCompat.checkSelfPermission(
                    this, permission
                ) == PackageManager.PERMISSION_GRANTED -> {
                    // Action to take when permission is already granted
                    Toast.makeText(this, "Permission granted", Toast.LENGTH_LONG).show()
                }

                shouldShowRequestPermissionRationale(permission) -> {
                    // Action to take when permission was denied
                    Toast.makeText(this, "Permission denied", Toast.LENGTH_LONG).show()
                }

                else -> {
                    // Request permission
                    requestPermissionLauncher.launch(permission)
                }
            }
        } else {
            // Device does not support required permission
            Toast.makeText(this, "No required permission", Toast.LENGTH_LONG).show()
        }
    }



//    private val permission = Manifest.permission.POST_NOTIFICATIONS
//    private val readExternalPermission=registerForActivityResult(ActivityResultContracts.RequestPermission()){isGranted->
//        if (isGranted){
//            Toast.makeText(this, "1 Read external storage permission granted", Toast.LENGTH_SHORT).show()
//        }else{
//            Toast.makeText(this, "2 Read external storage permission denied!", Toast.LENGTH_SHORT).show()
//        }
//    }
//    private fun requestPermissions(){
//        //check the API level
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU){
//            //filter permissions array in order to get permissions that have not been granted
//            val notGrantedPermissions = ContextCompat.checkSelfPermission(this,permission) == PackageManager.PERMISSION_GRANTED
//
//            if (notGrantedPermissions){
//                //check if permission was previously denied and return a boolean value
//                val showRationale=shouldShowRequestPermissionRationale(permission)
//
//                //if true, explain to user why granting this permission is important
//                if (showRationale){
//                    AlertDialog.Builder(this)
//                        .setTitle("Storage Permission")
//                        .setMessage("Storage permission is needed in order to show images and videos")
//                        .setNegativeButton("Cancel"){dialog,_->
//                            Toast.makeText(this, "Read media storage permission denied!", Toast.LENGTH_SHORT).show()
//                            dialog.dismiss()
//                        }
//                        .setPositiveButton("OK"){_,_->
//                            readExternalPermission.launch(permission)
//                        }
//                        .show()
//                }else{
//                    //launch the videoPermission ActivityResultContract
//                    readExternalPermission.launch(permission)
//                }
//            }else{
//                Toast.makeText(this, "5 Read media storage permission granted", Toast.LENGTH_SHORT).show()
//            }
//        }else{
//            //check if permission is granted
//            if (ContextCompat.checkSelfPermission(this,permission) == PackageManager.PERMISSION_GRANTED){
//                Toast.makeText(this, "3 Read external storage permission granted", Toast.LENGTH_SHORT).show()
//            }else{
//                if (shouldShowRequestPermissionRationale(permission)){
//                    AlertDialog.Builder(this)
//                        .setTitle("Storage Permission")
//                        .setMessage("Storage permission is needed in order to show images and video")
//                        .setNegativeButton("Cancel"){dialog,_->
//                            Toast.makeText(this, "4 Read external storage permission denied!", Toast.LENGTH_SHORT).show()
//                            dialog.dismiss()
//                        }
//                        .setPositiveButton("OK"){_,_->
//                            readExternalPermission.launch(permission)
//                        }
//                        .show()
//                }else{
//                    readExternalPermission.launch(permission)
//                }
//            }
//        }
//    }
}

package promag.groupe.proapp

import android.app.Activity
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import promag.groupe.proapp.permissions.Location

open abstract class BaseActivity : AppCompatActivity() {

    lateinit var mApplication: BaseApplication

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mApplication = applicationContext as BaseApplication
    }


    override fun onResume() {
        super.onResume()

        //check location permissions
        val granted = Location.checkPermissionLocation(this)
        if (!granted){
            Location.requestPermissionLocation(this)
            return
        }

    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            LOCATION_PERMISSION_REQUEST_CODE -> {
                val fineLocationPermission = grantResults[0] === PackageManager.PERMISSION_GRANTED
                val coarseLocationPermission = grantResults[1] === PackageManager.PERMISSION_GRANTED

                onLocationPermissionGranted(fineLocationPermission, coarseLocationPermission)
            }
        }


    }

    val getResult =
        registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()) {
            if(it.resultCode == Activity.RESULT_OK){
                val value = it.data?.getStringExtra("input")
            }
        }

    abstract fun onLocationPermissionGranted(fineLocationPermissionGranted: Boolean, coarseLocationPermissionGranted: Boolean)



}
package promag.groupe.proapp

import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import promag.groupe.proapp.permissions.*

open abstract class BaseActivity : AppCompatActivity() {

    lateinit var mApplication: BaseApplication

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mApplication = applicationContext as BaseApplication
    }


    override fun onResume() {
        super.onResume()

        val overlayGranted = Overlay.checkOverlayPermission(this)
        if (!overlayGranted) {
            Overlay.startOverlaySettings(this, getResultOfOverlaySettings)
            return
        }


        val gpsActivated = Gps.checkGpsState(mApplication)
        if (!gpsActivated) {
            Gps.requestGpsPermission(mApplication, this)
            return
        }


        //check location permissions
        val locationGranted = Location.checkPermissionLocation(this)
        if (!locationGranted) {
            Location.requestPermissionLocation(this)
            return
        }

        onRequirementsChecked()

    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            LOCATION_PERMISSION_REQUEST_CODE -> {
                val fineLocationPermission = grantResults[0] == PackageManager.PERMISSION_GRANTED
                val coarseLocationPermission = grantResults[1] == PackageManager.PERMISSION_GRANTED

                onLocationPermissionGranted(fineLocationPermission, coarseLocationPermission)
            }
        }


    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            GPS_SETTINGS_REQUEST_CODE -> when (resultCode) {
                RESULT_OK -> {
                    onGpsActivated()
                }
                RESULT_CANCELED -> {
                    onGpsDeactivated()
                }
            }
        }
    }


    private val getResultOfOverlaySettings =
        registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) {
            if (it.resultCode == Activity.RESULT_OK) {
                onOverlaySettingGranted()
            }
        }


    abstract fun onLocationPermissionGranted(
        fineLocationPermissionGranted: Boolean,
        coarseLocationPermissionGranted: Boolean
    )

    abstract fun onOverlaySettingGranted()
    abstract fun onGpsActivated()
    abstract fun onGpsDeactivated()
    abstract fun onRequirementsChecked()


}
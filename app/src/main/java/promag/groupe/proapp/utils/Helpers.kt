package promag.groupe.proapp.utils

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.IntentSender
import android.content.pm.PackageManager
import android.location.LocationManager
import android.os.Build
import android.provider.Settings
import android.util.Log
import androidx.core.app.ActivityCompat
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.common.api.PendingResult
import com.google.android.gms.common.api.Result
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.LocationSettingsRequest
import com.google.android.gms.location.LocationSettingsStatusCodes
import promag.groupe.proapp.BaseApplication
import promag.groupe.proapp.LOCATION_SETTINGS_REQUEST_CODE

class Helpers {
    companion object {
        fun checkGpsState(app: BaseApplication, activity: Activity): Boolean {
            try {
                val locationManager =
                    (app.getSystemService(Context.LOCATION_SERVICE) as LocationManager?)
                        ?: return false

                val gpsStatue = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
                if (gpsStatue) {

                    return true
                }
                val googleApiClient =
                    GoogleApiClient.Builder(app.applicationContext).addApi(LocationServices.API)
                        .build()
                googleApiClient.connect()
                val locationRequest = LocationRequest.create()
                locationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
                locationRequest.interval = (10).toLong()
                locationRequest.fastestInterval = (10).toLong()
                val builder = LocationSettingsRequest.Builder().addLocationRequest(locationRequest)
                val result: PendingResult<*> =
                    LocationServices.SettingsApi.checkLocationSettings(
                        googleApiClient,
                        builder.build()
                    )
                result.setResultCallback { results: Result ->
                    val status = results.status
                    when (status.statusCode) {
                        LocationSettingsStatusCodes.SUCCESS, LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE -> {
                        }
                        LocationSettingsStatusCodes.RESOLUTION_REQUIRED -> try {
                            status.startResolutionForResult(
                                activity,
                                LOCATION_SETTINGS_REQUEST_CODE
                            )
                        } catch (e: IntentSender.SendIntentException) {
                        }
                    }
                }
            } catch (e: Exception) {
                return false
            }

            return false
        }

        fun checkOverlayPermission(context: Context): Boolean {
            return try {

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    Settings.canDrawOverlays(context)

                } else {
                    true
                }

            } catch (e: Exception) {
                false
            }
        }

    }

}
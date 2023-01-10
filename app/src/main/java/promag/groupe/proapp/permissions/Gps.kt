package promag.groupe.proapp.permissions

import android.app.Activity
import android.content.Context
import android.content.IntentSender
import android.location.LocationManager

import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.common.api.PendingResult
import com.google.android.gms.common.api.Result
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.LocationSettingsRequest
import com.google.android.gms.location.LocationSettingsStatusCodes


import promag.groupe.proapp.BaseApplication


const val LOCATION_SETTINGS_REQUEST_CODE = 3000

class Gps {

    companion object {
        fun checkGpsState(application: BaseApplication): Boolean {
            try {
                val locationManager =
                    (application.getSystemService(Context.LOCATION_SERVICE) as LocationManager?)
                        ?: return false
                val active = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
                if (active) return true
                return false

            } catch (e: Exception) {
                return false
            }

        }

        fun requestGpsPermission(application: BaseApplication, activity: Activity) {
            val googleApiClient =
                GoogleApiClient.Builder(application.applicationContext).addApi(LocationServices.API)
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
                    } catch (_: IntentSender.SendIntentException) {
                    }
                }
            }

        }


    }
}
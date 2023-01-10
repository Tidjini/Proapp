package promag.groupe.proapp.permissions

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat


//const val ACCESS_FINE_LOCATON = "android.permission.ACCESS_FINE_LOCATION"
//const val ACCESS_COARSE_LOCATON = "android.permission.ACCESS_COARSE_LOCATION"
const val LOCATION_PERMISSION_REQUEST_CODE = 2000
class Location {

    companion object{
        fun checkPermissionLocation(context: Context): Boolean {
            if (ActivityCompat.checkSelfPermission(
                    context,
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED || ActivityCompat.checkSelfPermission(
                    context,
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                return false

            }
            return true
        }
    }


}
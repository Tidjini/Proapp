package promag.groupe.proapp

import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.provider.Settings
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import promag.groupe.proapp.models.User
import promag.groupe.proapp.utils.CacheHelper.userToken
import promag.groupe.proapp.utils.Helpers
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class LauncherActivity : AppCompatActivity(), ActivityCompat.OnRequestPermissionsResultCallback {
    private lateinit var app: BaseApplication

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        app = applicationContext as BaseApplication
        setContentView(R.layout.activity_launcher)
    }
    var perms = arrayOf("android.permission.ACCESS_FINE_LOCATION", "android.permission.ACCESS_COARSE_LOCATION")

    override fun onResume() {
        super.onResume()
        val overlayPermission = Helpers.checkOverlayPermission(this)
        if(!overlayPermission){
            startSetOverlaySettings()
            return
        }

        val locationPermission = Helpers.checkFineLocationPermission(this)

        if(!locationPermission){
            val permsRequestCode = 200
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(perms, permsRequestCode)
            }
            return
        }

        if (Helpers.checkOverlayPermission(this)) {
            verifyGpsState()
        } else {
        }


    }



    private fun verifyGpsState() {
//        gpsNotReadyYet()

        if (Helpers.checkGpsState(application as BaseApplication, this)) {
//            afterGpsIsOn()
            main()

        }
    }

    private fun startSetOverlaySettings() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val intent = Intent(
                Settings.ACTION_MANAGE_OVERLAY_PERMISSION, Uri.parse(
                    "package:$packageName"
                )
            )
            startActivityForResult(intent, OVERLAY_PERMISSION)
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            200 -> {
                val fineLocation = grantResults[0] === PackageManager.PERMISSION_GRANTED
                val coarseLocation = grantResults[1] === PackageManager.PERMISSION_GRANTED

                Log.d("location_service", "ACCESS_FINE_LOCATION $fineLocation ACCESS_COARSE_LOCATION $coarseLocation")
            }
        }
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            LOCATION_SETTINGS_REQUEST_CODE -> when (resultCode) {
                RESULT_OK -> {
                    Log.d("location_service", "LOCATION_SETTINGS_REQUEST_CODE RESULT_OK $RESULT_OK")
                }
                RESULT_CANCELED -> {
                    Log.d("location_service", "LOCATION_SETTINGS_REQUEST_CODE RESULT_CANCELED $RESULT_CANCELED")
                }
            }
        }
    }



    private fun main() {

        Handler(Looper.getMainLooper()).postDelayed(
            {
                authToken()
            },
            3_000 // value in milliseconds
        )
    }


    //authenticate with User Token
    private fun authToken() {

        if (app.userPreferences.userToken.isNullOrEmpty()) {
            gotoConnexion()
            return
        }

        val result = app.quotesApi.authToken("token ${app.userPreferences.userToken}")
        if (result == null) {
            gotoConnexion()
            return
        }

        result.enqueue(object : Callback<User?> {
            override fun onResponse(call: Call<User?>, response: Response<User?>) {
                if (response.errorBody() != null || response.body()!!.id == 0) {
                    return
                }
                val user: User = response.body() ?: return

                app.user = user
                app.userPreferences.userToken = user.token
//                app.socketConnection()
                gotoMain()
            }

            override fun onFailure(call: Call<User?>, t: Throwable) {
                gotoConnexion()
            }

        })
    }

    fun gotoMain() {

        val intent = Intent(this, MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or
                Intent.FLAG_ACTIVITY_CLEAR_TOP
        startActivity(intent)
        this.finish()

    }

    fun gotoConnexion() {
        val intent = Intent(this, Connexion::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or
                Intent.FLAG_ACTIVITY_CLEAR_TOP
        startActivity(intent)
        this.finish()
    }
}
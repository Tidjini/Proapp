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

const val TAG = "Launcher Activity"

class LauncherActivity : BaseActivity(){
    private lateinit var app: BaseApplication

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_launcher)
    }

    override fun onResume() {
        super.onResume()
        val overlayPermission = Helpers.checkOverlayPermission(this)
        if(!overlayPermission){
            startSetOverlaySettings()
            return
        }




        if (Helpers.checkOverlayPermission(this)) {
            verifyGpsState()
        } else {
        }


    }

    override fun onLocationPermissionGranted(fineLocationGranted: Boolean, coarseLocationGranted: Boolean) {
        Log.d(TAG, "onLocationPermissionGranted: fine Location : $fineLocationGranted | coarse location : $coarseLocationGranted" )
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
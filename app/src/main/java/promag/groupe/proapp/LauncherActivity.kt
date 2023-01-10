package promag.groupe.proapp

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import promag.groupe.proapp.models.User
import promag.groupe.proapp.utils.CacheHelper.userToken
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

const val TAG = "Launcher Activity"

class LauncherActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_launcher)
    }


    override fun onLocationPermissionGranted(
        fineLocationPermissionGranted: Boolean,
        coarseLocationPermissionGranted: Boolean
    ) {
        Log.d(
            TAG,
            "onLocationPermissionGranted: fine Location : $fineLocationPermissionGranted | coarse location : $coarseLocationPermissionGranted"
        )
    }

    override fun onOverlaySettingGranted() {
        Log.d(TAG, "Overlay Settings are Granted")
    }

    override fun onGpsActivated() {
        Log.d(TAG, "GPS is Activated")
    }

    override fun onGpsDeactivated() {
        Log.d(TAG, "GPS is Deactivated")
    }

    override fun onRequirementsChecked() {
        main()
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

        if (mApplication.userPreferences.userToken.isNullOrEmpty()) {
            gotoConnexion()
            return
        }

        val result =
            mApplication.quotesApi.authToken("token ${mApplication.userPreferences.userToken}")
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

                mApplication.user = user
                mApplication.userPreferences.userToken = user.token
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
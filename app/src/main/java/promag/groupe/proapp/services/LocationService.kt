package promag.groupe.proapp.services

import android.Manifest
import android.app.*
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.os.Build
import android.os.IBinder
import android.os.Looper
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import com.google.android.gms.location.*
import promag.groupe.proapp.BaseApplication
import promag.groupe.proapp.NOTIFICATION_LOCATION_CHANNEL_ID
import promag.groupe.proapp.R
import promag.groupe.proapp.models.UserLocalisation
import promag.groupe.proapp.utils.CacheHelper.userId
import promag.groupe.proapp.utils.CacheHelper.userToken
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

const val TAG = "Location Service"

class LocationService : Service() {
    lateinit var mApplication: BaseApplication

    override fun onCreate() {

        super.onCreate()
        mApplication = application as BaseApplication

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            startMyForegroundService()
        } else {
            startForeground(1, Notification())
        }


    }

    private fun startMyForegroundService() {

        val builder: NotificationCompat.Builder =
            NotificationCompat.Builder(this, NOTIFICATION_LOCATION_CHANNEL_ID)
                .setOngoing(false)
                .setSmallIcon(R.drawable.logo)


        val notification = builder.setOngoing(true)
            .setContentTitle("Groupe Amry Location Service")
            .setPriority(NotificationManager.IMPORTANCE_MIN)
            .setCategory(Notification.CATEGORY_SERVICE)


        startForeground(2, notification.build())
    }


    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {

        super.onStartCommand(intent, flags, startId)


        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            Log.d(TAG, "ACCESS_COARSE_LOCATION")

        }
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            Log.d(TAG, "ACCESS_FINE_LOCATION")


        }



        Log.d(TAG, "onStartCommand .......")
        onLocationChanged()
        return START_STICKY
    }

    var mLocationCallback: LocationCallback? = null
    var fusedLocationClient: FusedLocationProviderClient? = null

    private fun onLocationChanged() {


        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        val locationRequest = LocationRequest.create().apply {
            interval = (10).toLong()
            fastestInterval = (10).toLong()
            priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        }

        mLocationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult) {
                if (locationResult.locations.isNotEmpty()) {
                    val newLocation = locationResult.locations[0] ?: return
                    updateTransporterLocation(location = newLocation)
                    Log.d(TAG, "my position is null ${newLocation.longitude}")

                } else {
                    Log.d(TAG, "my position is null")
                }
            }
        }

        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            Log.d(TAG, "ACCESS_COARSE_LOCATION")
            return
        }
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            Log.d(TAG, "ACCESS_FINE_LOCATION")
            return
        }


        fusedLocationClient!!.requestLocationUpdates(
            locationRequest,
            mLocationCallback!!,
            Looper.myLooper()
        )
    }

    private var oldLocation: Location? = null

    private fun updateTransporterLocation(location: Location) {
        val userId = mApplication.userPreferences.userId
        val userToken = mApplication.userPreferences.userToken ?: return
        if (userId == 0) return
        val distance = oldLocation?.distanceTo(location)

        if (distance == null || distance > 20.0) {
            updatePosition(userToken, userId, location)
            Log.d(
                TAG,
                "location updated at position : lat: ${location.latitude}, long:${location.longitude}"
            )

        }


    }

    private fun updatePosition(userToken: String, userId: Int, location: Location) {
        val position = UserLocalisation(
            user = userId,
            longitude = location.longitude,
            latitude = location.latitude
        )
        val result = mApplication.quotesApi.updatePosition(
            token = userToken,
            id = userId,
            localisation = position
        ) ?: return
        result.enqueue(object : Callback<UserLocalisation?> {
            override fun onResponse(
                call: Call<UserLocalisation?>,
                response: Response<UserLocalisation?>
            ) {
                if (response.errorBody() != null || response.body()!!.user == 0) {
                    return
                }
                val position: UserLocalisation = response.body() ?: return
            }

            override fun onFailure(call: Call<UserLocalisation?>, t: Throwable) {
            }

        })

        oldLocation = location
    }


    override fun onBind(intent: Intent): IBinder? {
        return null
    }


    override fun onUnbind(intent: Intent?): Boolean {
        Log.d(TAG, "onUnbind .......")
        return super.onUnbind(intent)
    }

    override fun onTrimMemory(level: Int) {
        Log.d(TAG, "onTrimMemory .......")
        super.onTrimMemory(level)
    }

    override fun onLowMemory() {
        Log.d(TAG, "onLowMemory .......")
        super.onLowMemory()
    }

    override fun onTaskRemoved(rootIntent: Intent?) {

        Log.d(TAG, "onTaskRemoved .......")
        super.onTaskRemoved(rootIntent)
//        if(!isMyServiceRunning(LocationService::class.java)){
//            onRestart()
//        }


    }

    private fun isMyServiceRunning(serviceClass: Class<*>): Boolean {
        val manager = getSystemService(ACTIVITY_SERVICE) as ActivityManager
        for (service in manager.getRunningServices(Int.MAX_VALUE)) {
            if (serviceClass.name == service.service.className) {
                Log.d(TAG, "Running")
                return true
            }
        }
        Log.d(TAG, "Not running")
        return false
    }

    override fun onDestroy() {
        Log.d(TAG, "onDestroy .......")

//        onRestart()
        super.onDestroy()

    }

    private fun onRestart() {
        Log.d(TAG, "onRestart .......")
        val restartService = Intent(applicationContext, this.javaClass)
        val pendingIntent = PendingIntent.getService(
            applicationContext,
            1,
            restartService,
            PendingIntent.FLAG_ONE_SHOT
        )
        val alarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager
        alarmManager.setExact(
            AlarmManager.RTC_WAKEUP,
            System.currentTimeMillis(),
            pendingIntent
        )
    }


}

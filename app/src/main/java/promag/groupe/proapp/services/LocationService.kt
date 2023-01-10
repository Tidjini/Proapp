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
import promag.groupe.proapp.utils.CacheHelper.userId
import promag.groupe.proapp.utils.CacheHelper.userToken


class LocationService : Service() {
    lateinit var mApplication : BaseApplication

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
            Log.d("location_service", "ACCESS_COARSE_LOCATION")

        }
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            Log.d("location_service", "ACCESS_FINE_LOCATION")


        }



        Log.d("location_service", "onStartCommand .......")
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
                    Log.d("location_service", "my position is null ${newLocation.longitude}")

                } else {
                    Log.d("location_service", "my position is null")
                }
            }
        }

        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            Log.d("location_service", "ACCESS_COARSE_LOCATION")
            return
        }
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            Log.d("location_service", "ACCESS_FINE_LOCATION")
            return
        }


        fusedLocationClient!!.requestLocationUpdates(
            locationRequest,
            mLocationCallback!!,
            Looper.myLooper()
        )
    }

    private var oldLocation : Location? = null

    private fun updateTransporterLocation(location: Location) {

        if(mApplication.userPreferences.userId == 0) return
        if(oldLocation == null) {



            oldLocation = location
        }

        Log.d(
            "location_service",
            "updateTransporterLocation ${location.longitude} ${location.latitude}"
        )
    }


    override fun onBind(intent: Intent): IBinder? {
        return null
    }


    override fun onUnbind(intent: Intent?): Boolean {
        Log.d("location_service", "onUnbind .......")
        return super.onUnbind(intent)
    }

    override fun onTrimMemory(level: Int) {
        Log.d("location_service", "onTrimMemory .......")
        super.onTrimMemory(level)
    }

    override fun onLowMemory() {
        Log.d("location_service", "onLowMemory .......")
        super.onLowMemory()
    }

    override fun onTaskRemoved(rootIntent: Intent?) {

        Log.d("location_service", "onTaskRemoved .......")
        super.onTaskRemoved(rootIntent)
//        if(!isMyServiceRunning(LocationService::class.java)){
//            onRestart()
//        }


    }

    private fun isMyServiceRunning(serviceClass: Class<*>): Boolean {
        val manager = getSystemService(ACTIVITY_SERVICE) as ActivityManager
        for (service in manager.getRunningServices(Int.MAX_VALUE)) {
            if (serviceClass.name == service.service.className) {
                Log.d("location_service", "Running")
                return true
            }
        }
        Log.d("location_service", "Not running")
        return false
    }

    override fun onDestroy() {
        Log.d("location_service", "onDestroy .......")

//        onRestart()
        super.onDestroy()

    }

    private fun onRestart() {
        Log.d("location_service", "onRestart .......")
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

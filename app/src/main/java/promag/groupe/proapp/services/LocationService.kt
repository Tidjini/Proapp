package promag.groupe.proapp.services

import android.Manifest
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
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
import promag.groupe.proapp.R


class LocationService : Service() {

    private val NOTIFICATION_CHANNEL_ID = "my_notification_location"


    override fun onCreate() {

        super.onCreate()

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            startMyForegroundService()
        } else {
            startForeground(1, Notification())
        }


    }

    fun startMyForegroundService() {

        val builder: NotificationCompat.Builder =
            NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID)
                .setOngoing(false)
                .setSmallIcon(R.drawable.logo)
        val notificationManager: NotificationManager =
            getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationChannel = NotificationChannel(
                NOTIFICATION_CHANNEL_ID,
                NOTIFICATION_CHANNEL_ID, NotificationManager.IMPORTANCE_LOW
            )
            notificationChannel.description = NOTIFICATION_CHANNEL_ID
            notificationChannel.setSound(null, null)
            notificationManager.createNotificationChannel(notificationChannel)
        }

        val notification = builder.setOngoing(true)
            .setContentTitle("Groupe Amry Location Service")
            .setPriority(NotificationManager.IMPORTANCE_MIN)
            .setCategory(Notification.CATEGORY_SERVICE)
            .build()
        startForeground(2, notification)
    }


    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {

        super.onStartCommand(intent, flags, startId)
        onLocationChanged()
        return START_STICKY
    }

    var mLocationCallback: LocationCallback? = null
    var fusedLocationClient: FusedLocationProviderClient? = null

    fun onLocationChanged() {
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        val locationRequest = LocationRequest.create().apply {
            interval = (1000 * 60).toLong()
            fastestInterval = (1000 * 60).toLong()
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
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return
        }

        fusedLocationClient!!.requestLocationUpdates(
            locationRequest,
            mLocationCallback!!,
            Looper.myLooper()
        )
    }

    private fun updateTransporterLocation(location: Location) {

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

    }

    override fun onDestroy() {

        Log.d("location_service", "onDestroy .......")
        super.onDestroy()

    }


}

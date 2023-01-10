package promag.groupe.proapp

import android.app.*
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Color
import android.media.RingtoneManager
import android.net.Uri
import android.os.Build
import android.provider.Settings
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import com.google.gson.Gson
import io.socket.client.Socket
import io.socket.emitter.Emitter
import promag.groupe.proapp.comercial.views.PaymentCollectionView
import promag.groupe.proapp.global.messenger.messages.MessagesActivity
import promag.groupe.proapp.models.Message
import promag.groupe.proapp.models.User
import promag.groupe.proapp.services.AlarmReceiver
import promag.groupe.proapp.services.LocationService
import promag.groupe.proapp.services.procom.CommercialAPI
import promag.groupe.proapp.services.procom.ProcomAPI
import promag.groupe.proapp.services.procom.ProcomService
import promag.groupe.proapp.services.procom.TasksAPI
import promag.groupe.proapp.tasks.views.TaskCollectionView
import promag.groupe.proapp.utils.CacheHelper
import java.net.URISyntaxException
import kotlin.properties.Delegates

class BaseApplication : Application() {

    //globals
    lateinit var user: User
    lateinit var userPreferences: SharedPreferences
    lateinit var quotesApi: ProcomAPI
    lateinit var commercialApi: CommercialAPI
    lateinit var tasksAPI: TasksAPI

    var mSocket: Socket? = null
    var observed = false


    public var max: Int by Delegates.observable(0) { _, _, _ ->
        observed = true
    }

    val newestArticleObservers = mutableListOf<(String) -> Unit>()

    var newestArticleUrl: String by Delegates.observable("") { _, _, newValue ->
        newestArticleObservers.forEach { it(newValue) }
    }

    override fun onCreate() {
        super.onCreate()
        //globals
        userPreferences = CacheHelper.customPreference(applicationContext, USER_PREFERENCE)
        quotesApi = ProcomService.getInstance().create(ProcomAPI::class.java)
        commercialApi = ProcomService.getInstance().create(CommercialAPI::class.java)
        tasksAPI = ProcomService.getInstance().create(TasksAPI::class.java)

        setLocationNotificationChannel()
//        alarmPermissions()
//        launchLocationService()
    }


    private fun setLocationNotificationChannel() {
        val notificationManager: NotificationManager =
            getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                NOTIFICATION_LOCATION_CHANNEL_ID,
                NOTIFICATION_LOCATION_CHANNEL_ID, NotificationManager.IMPORTANCE_LOW
            )

            channel.description = NOTIFICATION_LOCATION_CHANNEL_ID
            channel.setSound(null, null)

            notificationManager.createNotificationChannel(channel)
        }
    }


    var mServiceLocationIntent: Intent? = null
    fun launchLocationService() {

        try {
            mServiceLocationIntent = Intent(this, LocationService::class.java)
            if (!isMyServiceRunning(LocationService::class.java)) {
                stopService(mServiceLocationIntent)
            }
            //val mServiceIntent = Intent(this, LocationService::class.java) ?: return
            startService(mServiceLocationIntent)

        } catch (e: Exception) {
        }
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

    override fun onTerminate() {
        super.onTerminate()
        mSocket!!.disconnect();
        //clear all events
        mSocket!!.off(user.token, onNewMessage)
        mSocket!!.off("${user.token}_tasks", onTaskChange)

        if (user.isAdmin)
            mSocket!!.off("payment_added", onPayementAdded)

    }

    fun clearSocketListening(event: String?) {
        if (event.isNullOrEmpty()) return
        mSocket!!.off(event, onNewMessage)

    }

    fun listenNotifications(event: String?) {
        mSocket!!.on(event, onNewMessage)

    }

    fun socketConnection() {

        if (user.token.isNullOrEmpty()) return

        try {
//            mSocket = IO.socket(BASE_URL)
//            mSocket!!.on(user.token, onNewMessage)
//            mSocket!!.on("${user.token}_tasks", onTaskChange)
//
//            if (user.isAdmin)
//                mSocket!!.on("payment_added", onPayementAdded)
//            mSocket!!.connect()

        } catch (e: URISyntaxException) {

            Log.d("app_socket", e.toString())
        }
    }

    private val onNewMessage =
        Emitter.Listener { args ->
            val data = args[0]
            val g = Gson()
            val da = g.fromJson<Message>(data.toString(), Message::class.java)
            playNotif()
            displayNotification(da)
            Log.d("app_socket", data.toString())


        }

    private val onTaskChange =
        Emitter.Listener { args ->
            val data = args[0]
            val message = data.toString()
            playNotif()
            displayTaskChangeNotif(message)


        }

    fun displayTaskChangeNotif(message: String) {
        val notificationManager = this.getSystemService(NOTIFICATION_SERVICE) as NotificationManager

        val notificationId: Int = 0
        val channelId = "channel-id"
        val channelName = "Channel Name"
        val importance = NotificationManager.IMPORTANCE_HIGH

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val mChannel = NotificationChannel(
                channelId, channelName, importance
            )
            notificationManager.createNotificationChannel(mChannel)
        }
        val mBuilder: NotificationCompat.Builder = NotificationCompat.Builder(this, channelId)
            .setSmallIcon(R.drawable.logo) //R.mipmap.ic_launcher
            .setContentTitle("Gestion Tâches")
            .setContentText(message)
            .setVibrate(longArrayOf(100, 250))
            .setLights(Color.YELLOW, 500, 5000)
            .setAutoCancel(true)
            .setColor(ContextCompat.getColor(this, R.color.colorPrimary))

        val resultIntent = Intent(applicationContext, TaskCollectionView::class.java)
        val resultPendingIntent: PendingIntent? = TaskStackBuilder.create(this).run {
            // Add the intent, which inflates the back stack
            addNextIntentWithParentStack(resultIntent)
            // Get the PendingIntent containing the entire back stack
            getPendingIntent(
                0,
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )
        }

        mBuilder.setContentIntent(resultPendingIntent)


        notificationManager.notify(notificationId, mBuilder.build())
    }

    private val onPayementAdded =
        Emitter.Listener { args ->
            val data = args[0]
            val message = data.toString()
            playNotif()
            displayNotificationForPayment(message)
            Log.d("app_socket", data.toString())


        }

    fun displayNotificationForPayment(message: String) {
        val notificationManager = this.getSystemService(NOTIFICATION_SERVICE) as NotificationManager

        val notificationId: Int = 0
        val channelId = "channel-id"
        val channelName = "Channel Name"
        val importance = NotificationManager.IMPORTANCE_HIGH

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val mChannel = NotificationChannel(
                channelId, channelName, importance
            )
            notificationManager.createNotificationChannel(mChannel)
        }
        val mBuilder: NotificationCompat.Builder = NotificationCompat.Builder(this, channelId)
            .setSmallIcon(R.drawable.logo) //R.mipmap.ic_launcher
            .setContentTitle("Paiements")
            .setContentText(message)
            .setVibrate(longArrayOf(100, 250))
            .setLights(Color.YELLOW, 500, 5000)
            .setAutoCancel(true)
            .setColor(ContextCompat.getColor(this, R.color.colorPrimary))

        val resultIntent = Intent(applicationContext, PaymentCollectionView::class.java)
        val resultPendingIntent: PendingIntent? = TaskStackBuilder.create(this).run {
            // Add the intent, which inflates the back stack
            addNextIntentWithParentStack(resultIntent)
            // Get the PendingIntent containing the entire back stack
            getPendingIntent(
                0,
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )
        }

        mBuilder.setContentIntent(resultPendingIntent)


        notificationManager.notify(notificationId, mBuilder.build())
    }

    fun displayNotification(data: Message) {
        val notificationManager = this.getSystemService(NOTIFICATION_SERVICE) as NotificationManager

        val notificationId: Int = 0
        val channelId = "channel-id"
        val channelName = "Channel Name"
        val importance = NotificationManager.IMPORTANCE_HIGH

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val mChannel = NotificationChannel(
                channelId, channelName, importance
            )
            notificationManager.createNotificationChannel(mChannel)
        }
        val sender = data.sendTo?.name ?: "Message Anonymous"
        val mBuilder: NotificationCompat.Builder = NotificationCompat.Builder(this, channelId)
            .setSmallIcon(R.drawable.logo) //R.mipmap.ic_launcher
            .setContentTitle(sender)
            .setContentText(data.message)
            .setVibrate(longArrayOf(100, 250))
            .setLights(Color.YELLOW, 500, 5000)
            .setAutoCancel(true)
            .setColor(ContextCompat.getColor(this, R.color.colorPrimary))

        val resultIntent = Intent(applicationContext, MessagesActivity::class.java)
        resultIntent.putExtra(DISCUSSION_EXTRA, data.notifDiscussion)
        val resultPendingIntent: PendingIntent? = TaskStackBuilder.create(this).run {
            // Add the intent, which inflates the back stack
            addNextIntentWithParentStack(resultIntent)
            // Get the PendingIntent containing the entire back stack
            getPendingIntent(
                0,
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )
        }

        mBuilder.setContentIntent(resultPendingIntent)


        notificationManager.notify(notificationId, mBuilder.build())
    }

    private fun playNotif() {
        try {
            val n = Uri.parse("android.resource://promag.groupe.proapp/" + R.raw.notification_01)
            val r = RingtoneManager.getRingtone(
                applicationContext,
                n
            )

            r.play()
        } catch (e: Exception) {

        }

    }

    //alarm
    var alarmManager: AlarmManager? = null
    var pendingIntent: PendingIntent? = null

    private fun alarmPermissions() {
        alarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val hasPermission: Boolean = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            alarmManager!!.canScheduleExactAlarms()
        } else {
            true
        }

        if (!hasPermission) {
            val intent = Intent().apply {
                action = Settings.ACTION_REQUEST_SCHEDULE_EXACT_ALARM
            }
            startActivity(intent)
        }

        Log.d("Location Alarm", "alarmPermissions: $hasPermission")

        if (hasPermission) {
            setAlarm()

//            Thread {
//            }.start()
        }
    }


    private fun setAlarm() {
        val intent = Intent(applicationContext, AlarmReceiver::class.java)


        pendingIntent = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            PendingIntent.getBroadcast(applicationContext, 0, intent, PendingIntent.FLAG_IMMUTABLE)
        } else {
            PendingIntent.getBroadcast(applicationContext, 0, intent, 0)
        }

        alarmManager!!.setRepeating(
            AlarmManager.RTC_WAKEUP,
            System.currentTimeMillis(),
            10000,
            pendingIntent
        )

    }


}



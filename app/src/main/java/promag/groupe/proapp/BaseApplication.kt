package promag.groupe.proapp

import android.app.*
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Color
import android.media.RingtoneManager
import android.net.Uri
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import com.google.gson.Gson
import io.socket.client.IO
import io.socket.client.Socket
import io.socket.emitter.Emitter
import promag.groupe.proapp.global.messenger.messages.MessagesActivity
import promag.groupe.proapp.models.Message
import promag.groupe.proapp.models.User
import promag.groupe.proapp.services.procom.ProcomAPI
import promag.groupe.proapp.services.procom.ProcomService
import promag.groupe.proapp.utils.CacheHelper
import java.net.URISyntaxException
import kotlin.properties.Delegates

class BaseApplication : Application() {

    //globals
    lateinit var user: User
    lateinit var userPreferences: SharedPreferences
    lateinit var quotesApi: ProcomAPI

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

    }


    override fun onTerminate() {
        super.onTerminate()
        mSocket!!.disconnect();
        //clear all events
        mSocket!!.off(user.token, onNewMessage);
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
            mSocket = IO.socket(BASE_URL)
            mSocket!!.on(user.token, onNewMessage)
            mSocket!!.connect()

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


}



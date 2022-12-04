package promag.groupe.proapp

import android.annotation.SuppressLint
import android.app.*
import android.content.BroadcastReceiver
import android.content.Intent
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
import promag.groupe.proapp.infrabitume.Encaissement
import promag.groupe.proapp.infrabitume.FactureActivity
import promag.groupe.proapp.infrabitume.LivraisonActivity
import promag.groupe.proapp.models.Message
import promag.groupe.proapp.models.User
import promag.groupe.proapp.services.NotificationApi
import java.net.URISyntaxException
import kotlin.properties.Delegates

class BaseApplication : Application() {

    lateinit var user : User


    private var mSocket: Socket? = null
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
        NotificationApi.build(listener = { args ->
            val data = args[0]
            newestArticleUrl = data.toString()
        })

        newestArticleObservers.add { newOne ->
            Log.d("socket _observer", newOne)
        }


        try {
            mSocket = IO.socket("https://eassalnotif.herokuapp.com/")
            mSocket!!.on("PRODUCT", onNewMessage)
            mSocket!!.on("ENCAISSEMENT", onNewMessageN)
            mSocket!!.on("FACTURE", onNewMessageNn)
            mSocket!!.connect()
            Log.d("app_socket", "Connected to server")


        } catch (e: URISyntaxException) {

            Log.d("app_socket", e.toString())
        }
//        val br: BroadcastReceiver = promag.groupe.proapp.recievers.NotificationApi()
    }

    override fun onTerminate() {
        super.onTerminate()
        mSocket!!.disconnect();
        mSocket!!.off("PRODUCT", onNewMessage);
        mSocket!!.off("ENCAISSEMENT", onNewMessageN);
        mSocket!!.off("FACTURE", onNewMessageNn);
    }

    private val onNewMessage =
        Emitter.Listener { args ->
//            runOnUiThread(Runnable {
            val data = args[0]
            val g = Gson()
            val da = g.fromJson<Message>(data.toString(), Message::class.java)
            playNotif()

            displayNotification(da)


            Log.d("app_socket", data.toString())


//            })
        }

    private val onNewMessageN =
        Emitter.Listener { args ->
//            runOnUiThread(Runnable {
            val data = args[0]
            val g = Gson()
            val da = g.fromJson<Message>(data.toString(), Message::class.java)
            playNotif()

            displayNotification(da)


            Log.d("app_socket", data.toString())


//            })
        }

    private val onNewMessageNn =
        Emitter.Listener { args ->
//            runOnUiThread(Runnable {
            val data = args[0]
            val g = Gson()
            val da = g.fromJson<Message>(data.toString(), Message::class.java)
            playNotif()

            displayNotification(da)


            Log.d("app_socket", data.toString())


//            })
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

        val mBuilder: NotificationCompat.Builder = NotificationCompat.Builder(this, channelId)
            .setSmallIcon(R.drawable.logo) //R.mipmap.ic_launcher
            .setContentTitle("Groupe Amry")
            .setContentText(data.message)
            .setVibrate(longArrayOf(100, 250))
            .setLights(Color.YELLOW, 500, 5000)
            .setAutoCancel(true)
            .setColor(ContextCompat.getColor(this, R.color.colorPrimary))
        val stackBuilder: TaskStackBuilder = TaskStackBuilder.create(this)
        if (data.context == "PRODUCT") {
            stackBuilder.addNextIntent(Intent(this, LivraisonActivity::class.java))

        } else if (data.context == "ENCAISSEMENT") {
            stackBuilder.addNextIntent(Intent(this, Encaissement::class.java))

        } else {
            stackBuilder.addNextIntent(Intent(this, FactureActivity::class.java))
        }


        val resultPendingIntent: PendingIntent =
            stackBuilder.getPendingIntent(
                0,
                PendingIntent.FLAG_MUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
            )
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
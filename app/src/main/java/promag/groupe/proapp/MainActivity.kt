package promag.groupe.proapp

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.TaskStackBuilder
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.media.RingtoneManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import android.widget.GridLayout
import androidx.appcompat.app.AppCompatActivity
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
import promag.groupe.proapp.views.AppAlertDialog
import promag.groupe.proapp.views.AppToast
import java.net.URISyntaxException


class MainActivity : AppCompatActivity() {


    private lateinit var container: GridLayout
    private lateinit var mNotificationManager: NotificationManager



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        mNotificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

//        (application as BaseApplication).newestArticleObservers.add { newOne ->
//            Log.d("socket _observer", newOne)
//        }




//        mSendSocket!!.connect()

    }




    override fun onResume() {
        super.onResume()

        val handler = Handler(Looper.getMainLooper())

//        handler.postDelayed({
//            attemptSend()
//        }, 1000 * 5)
    }

    private fun attemptSend() {
        val message = "HI there from this terminal"

//        mSendSocket!!.emit("context", message)
    }





    fun onInfraStockClicked(view: View) {

        val intent = Intent(this, LivraisonActivity::class.java)
        startActivity(intent)

    }

    fun onInfraEncaissementClicked(view: View) {
        val intent = Intent(this, Encaissement::class.java)
        startActivity(intent)
    }
    fun onFactureClicked(view: View) {
        val intent = Intent(this, FactureActivity::class.java)
        startActivity(intent)
    }

    fun onOthersClicked(view: View) {

        AppToast(this, "This Application is not available now. retry later ;)", true)

    }


    fun onLogout(view: View) {
        AppAlertDialog.showYesNoDialog(
            this,
            "Information",
            "Voulez vous vraiment quitter l'application ?",
            { finish() },
            null
        )
    }

}
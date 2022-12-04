package promag.groupe.proapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import kotlinx.coroutines.delay

class LauncherActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_launcher)
    }


    override fun onResume() {
        super.onResume()
        main()
    }

    private fun main() {

        Handler(Looper.getMainLooper()).postDelayed(
            {
                val intent = Intent(this, Connexion::class.java)
                startActivity(intent)
                finish()
            },
            3_000 // value in milliseconds
        )
    }
}
package promag.groupe.proapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import kotlinx.coroutines.delay
import promag.groupe.proapp.models.User
import promag.groupe.proapp.utils.CacheHelper.userToken
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LauncherActivity : AppCompatActivity() {
    private lateinit var app: BaseApplication

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        app = applicationContext as BaseApplication
        setContentView(R.layout.activity_launcher)
    }


    override fun onResume() {
        super.onResume()
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
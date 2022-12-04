package promag.groupe.proapp

import android.app.Application
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import android.view.WindowManager
import android.view.animation.AnimationUtils
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import promag.groupe.proapp.infrabitume.FactureActivity
import promag.groupe.proapp.models.Auth
import promag.groupe.proapp.models.User
import promag.groupe.proapp.services.procom.ProcomAPI
import promag.groupe.proapp.services.procom.ProcomService
import promag.groupe.proapp.utils.CacheHelper
import promag.groupe.proapp.utils.CacheHelper.userToken
import promag.groupe.proapp.views.AppAlertDialog
import promag.groupe.proapp.views.AppToast
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class Connexion : AppCompatActivity() {


    private lateinit var loginPlaceholder: LinearLayout
    private lateinit var login: LinearLayout
    private lateinit var phonePlacehoder: EditText
    private lateinit var username: EditText
    private lateinit var password: EditText
    private lateinit var connexionButton: Button

    private lateinit var application: BaseApplication


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_connexion)

        application = applicationContext as BaseApplication
        intialize()
    }

    private fun intialize() {

        quotesApi = ProcomService.getInstance().create(ProcomAPI::class.java)


        loginPlaceholder = findViewById(R.id.login_layout)
        login = findViewById(R.id.inscription_layout)
        phonePlacehoder = findViewById(R.id.phone_edit_text)
        username = findViewById(R.id.username)
        password = findViewById(R.id.password)
        connexionButton = findViewById(R.id.sign_continue_btn)

        handleEvents()
    }


    override fun onResume() {
        super.onResume()
        loginPlaceholderAnimateIn()
    }


    val handler = Handler(Looper.getMainLooper())
    private fun loginPlaceholderAnimateIn() {
        handler.postDelayed({
            loginPlaceholder.visibility = View.VISIBLE
            val animation = AnimationUtils.loadAnimation(
                applicationContext, R.anim.from_bottom_enter
            )
            loginPlaceholder.animation = animation
        }, 0)
    }

    private fun loginPlaceholderAnimateOut() {
        val animation = AnimationUtils.loadAnimation(
            applicationContext, R.anim.from_top_exit
        )
        animation.duration = 300
        loginPlaceholder.startAnimation(animation)
        loginPlaceholder.visibility = View.GONE
    }


    private fun loginAnimateIn() {
        handler.postDelayed({

            login.visibility = View.VISIBLE
            val enter = AnimationUtils.loadAnimation(
                applicationContext, R.anim.from_bottom_enter
            )
            enter.duration = 500
            login.startAnimation(enter)
        }, 250)
    }


    private fun displayKeyboard() {
        handler.postDelayed({
            window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN)
            username.requestFocus()
            val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.showSoftInput(
                username, InputMethodManager.SHOW_IMPLICIT
            )
        }, 950)
    }

    private fun handleEvents() {
        phonePlacehoder.isFocusable = false
        phonePlacehoder.setOnClickListener {
            loginPlaceholderAnimateOut()
            loginAnimateIn()
            displayKeyboard()
        }

        connexionButton.setOnClickListener {
            //todo if there is some validations, for inputs, chars limits ..
            val username = username.text.toString().trim()
            val password = password.text.toString().trim()
            authentication(username = username, password = password)
        }
    }

    private lateinit var quotesApi: ProcomAPI

    private fun authentication(username: String, password: String) {
        //create the Auth object to send as body
        val auth = Auth(username = username, password = password)
        Log.e("MyAuth: ", "Username: ${auth.username}, Password: ${auth.password}")
        val result = quotesApi.authentication(auth) ?: return




        result.enqueue(object : Callback<User?> {
            override fun onResponse(call: Call<User?>, response: Response<User?>) {
                if (response.errorBody() != null || response.body()!!.id == 0) {
                    displayConnexionFailure("BODY ERROR| " + response.errorBody())
                    return
                }
                val user: User? = response.body()

                if (user == null) {
                    displayConnexionFailure("BODY ERROR| " + response.errorBody())
                    return
                }

                application.user = user
                application.userPreferences.userToken = user.token
                gotoMain()
                //todo set with shared preferences
                //todo continue to main activity

            }

            override fun onFailure(call: Call<User?>, t: Throwable) {
                displayConnexionFailure("ON FAILURE| " + t.toString())
            }

        })

    }

    fun displayConnexionFailure(message: String) {
        Log.e("MyAuth Exception: ", message)

        AppAlertDialog.showAlertDialog(
            this@Connexion,
            title = "Problem",
            message = "Problem de connexion, verifier votre username and password"
        )

    }

    fun gotoMain() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        this.finish()
    }
//
//
//    private fun confirmation() {
//        AppAlertDialog.showYesNoDialog(
//            this,
//            "Vérification",
//            "Voulez vous confirmer la connection avec le numéro: ${phone.text} ?",
//            {
//
//                val intent = Intent(this, MainActivity::class.java)
//                startActivity(intent)
//                finish()
//
//            },
//            null
//        )
//    }
}
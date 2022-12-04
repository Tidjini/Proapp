package promag.groupe.proapp

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.view.WindowManager
import android.view.animation.AnimationUtils
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import promag.groupe.proapp.views.AppAlertDialog

class Connexion : AppCompatActivity() {


    private lateinit var loginPlaceholder: LinearLayout
    private lateinit var login: LinearLayout
    private lateinit var phonePlacehoder: EditText
    private lateinit var username: EditText
    private lateinit var password: EditText
    private lateinit var connexionButton: Button
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_connexion)
        intialize()
    }

    private fun intialize() {
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
            phone.requestFocus()
            val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.showSoftInput(
                phone, InputMethodManager.SHOW_IMPLICIT
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
            val username = username.text.toString()
            val password = password.text.toString()
            confirmation()
        }
    }

    private fun authentication(username : String, password: String): Uti{

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
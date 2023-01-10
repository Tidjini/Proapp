package promag.groupe.proapp

import android.os.Bundle
import android.view.WindowManager
import androidx.activity.ComponentActivity
import promag.groupe.proapp.models.User


open class BaseComponentActivity : ComponentActivity() {
    lateinit var mApplication: BaseApplication
    lateinit var user: User
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE)

        mApplication = applicationContext as BaseApplication
        user = mApplication.user
    }
}
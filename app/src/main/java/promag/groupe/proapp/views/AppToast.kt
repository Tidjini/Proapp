package promag.groupe.proapp.views

import android.content.Context
import android.widget.Toast

class AppToast(context: Context?, message: String?, laLong: Boolean) {
    init {
        Toast.makeText(context, message, if (laLong) Toast.LENGTH_LONG else Toast.LENGTH_SHORT)
            .show()
    }
}
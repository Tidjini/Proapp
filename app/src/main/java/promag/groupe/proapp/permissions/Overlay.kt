package promag.groupe.proapp.permissions

import android.content.Context
import android.os.Build
import android.provider.Settings

class Overlay {
    companion object {
        fun checkOverlayPermission(context: Context): Boolean {
            return try {

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    Settings.canDrawOverlays(context)
                } else {
                    true
                }

            } catch (e: Exception) {
                false
            }
        }
    }
}
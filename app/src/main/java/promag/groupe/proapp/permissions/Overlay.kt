package promag.groupe.proapp.permissions

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.provider.Settings
import androidx.activity.result.ActivityResultLauncher


const val OVERLAY_PERMISSION_REQUEST_CODE = 2000

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

        fun startOverlaySettings(
            activity: Activity,
            getResult: ActivityResultLauncher<Intent>? = null
        ) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                val intent = Intent(
                    Settings.ACTION_MANAGE_OVERLAY_PERMISSION, Uri.parse(
                        "package:${activity.packageName}"
                    )
                )
                getResult?.launch(intent)

            }
        }
    }
}
package promag.groupe.proapp.utils

import android.content.Context
import android.content.SharedPreferences
import android.preference.PreferenceManager
import promag.groupe.proapp.USER_TOKEN


object CacheHelper {


    fun defaultPreference(context: Context): SharedPreferences =
        PreferenceManager.getDefaultSharedPreferences(context)

    fun customPreference(context: Context, name: String): SharedPreferences =
        context.getSharedPreferences(name, Context.MODE_PRIVATE)

    private inline fun SharedPreferences.editMe(operation: (SharedPreferences.Editor) -> Unit) {
        val editMe = edit()
        operation(editMe)
        editMe.apply()
    }

    var SharedPreferences.userToken
        get() = getString(USER_TOKEN, "")
        set(value) {
            editMe {
                it.putString(USER_TOKEN, value)
            }
        }

    
    var SharedPreferences.clearValues
        get() = { }
        set(value) {
            editMe {
                it.clear()
            }
        }
}
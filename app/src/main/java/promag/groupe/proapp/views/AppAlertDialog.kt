package promag.groupe.proapp.views

import android.R
import android.app.Activity
import android.app.AlertDialog
import android.content.DialogInterface
import android.view.View
import promag.groupe.proapp.IFunc


object AppAlertDialog {
    fun showAlertDialog(activity: Activity?, title: String?, message: String?) {
        val alertDialog: AlertDialog = AlertDialog.Builder(activity).create()
        alertDialog.setTitle(title)
        alertDialog.setMessage(message)
        alertDialog.setButton(
            AlertDialog.BUTTON_NEUTRAL, "Ok"
        ) { dialog, _ -> dialog.dismiss() }
        alertDialog.show()
    }

    fun showAlertDialog(
        activity: Activity?,
        title: String?,
        message: String?,
        okFunction: (dialog: AlertDialog.Builder) -> Unit
    ) {
        val alertDialog: AlertDialog.Builder = AlertDialog.Builder(activity)
        alertDialog.setTitle(title)
        alertDialog.setMessage(message)
        alertDialog.setPositiveButton(R.string.yes) { _, _ -> okFunction(alertDialog) }
        alertDialog.show()

    }

    fun showYesNoDialog(
        activity: Activity?, title: String?, message: String?,
        okFunction: IFunc<Any?>, cancelFunction: IFunc<Any?>?
    ) {
        val alert: AlertDialog.Builder = AlertDialog.Builder(activity)
        alert.setTitle(title)
        alert.setMessage(message)
        alert.setPositiveButton(R.string.yes) { _, _ -> okFunction.execute(null) }
        alert.setNegativeButton(R.string.no) { dialog, _ ->
            // close dialog
            cancelFunction?.execute(null)
            dialog.cancel()
        }
        alert.show()
    }

    fun showViewDialog(
        activity: Activity?, view: View?, title: String?, message: String?,
        okFunction: IFunc<Any?>?, cancelFunction: IFunc<Any?>?
    ) {
        val alert: AlertDialog.Builder = AlertDialog.Builder(activity)
        if (view != null) {
            alert.setView(view)
        }
        alert.setTitle(title)
        alert.setMessage(message)
        alert.setPositiveButton(R.string.yes) { dialog, _ ->
            okFunction?.execute(null)
            dialog.cancel()
        }
        alert.setNegativeButton(R.string.no) { dialog, _ ->
            // close dialog
            cancelFunction?.execute(null)
            dialog.cancel()
        }
        alert.show()
    }
}

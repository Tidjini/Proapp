package promag.groupe.proapp.models

import android.graphics.Color
import androidx.annotation.DrawableRes
import promag.groupe.proapp.R
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.text.NumberFormat
import java.util.*

data class Encaissement(
    var reference: String = "",
    var growth_loss: Int = 0,
    var label: String = "",
    var date_range: String = "",
    var value: Float = 0.0f,
    var previous_value: Float = 0.0f,
    var update_at: String = ""
) {

    val valuePrice: String
        get(){
            var format: NumberFormat = NumberFormat.getCurrencyInstance(Locale.FRANCE)
            val decimalFormatSymbols: DecimalFormatSymbols =
                (format as DecimalFormat).decimalFormatSymbols
            decimalFormatSymbols.currencySymbol = ""
            format.decimalFormatSymbols = decimalFormatSymbols
            val currency: String = format.format(value).trim()
            return "$currency DA"
        }

    val growth: String
        get(){
            var sign = "+"
            var suiffix = "au dernier jour"
            if (growth_loss < 0)
                sign = "-"
            if (reference == "month")
                suiffix = "au dernier mois"
            if (reference == "week")
                suiffix = "au derniere semaine"


            return "${sign}${growth_loss}% par rapport $suiffix"
        }
    val color: Int
        get(){
            if (growth_loss < 0)
                return Color.parseColor("#d62828")
            return  Color.parseColor("#06d6a0")
        }

    val icon: Int
        get(){
            if (growth_loss <= 0)
                return R.drawable.loss
            return  R.drawable.growth
        }
}
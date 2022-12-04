package promag.groupe.proapp.models

import promag.groupe.proapp.R
import promag.groupe.proapp.databinding.ActivityFactureBinding
import java.io.Serializable
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.text.NumberFormat
import java.util.*


data class Facture(
    var id: Int=0,
    var number: String = " ",
    var designation: Int = 0,
    var entity: String = "INF",
    var remarque: String = " ",
    var sender: String = "sender",
    var update_at: String = "",
    var confirm: Boolean = false,
    var picture: String? = null,
    var montant: Float = 0.0f
): Serializable {


    val fNumber:String
        get() {
            if(confirm)
                return "Facture N°: $number"
            return "Instance N°: $number"
        }

    val fRemaraque: String
        get(){
            if(remarque.length > 86)
                return remarque.substring(0, 83) + "..."
            return remarque
        }

    val fMontant: String
        get(){
            var format: NumberFormat = NumberFormat.getCurrencyInstance(Locale.FRANCE)
            val decimalFormatSymbols: DecimalFormatSymbols =
                (format as DecimalFormat).decimalFormatSymbols
            decimalFormatSymbols.currencySymbol = ""
            format.decimalFormatSymbols = decimalFormatSymbols
            val currency: String = format.format(montant).trim()
            return "$currency DA"
        }


    val icon: Int
        get(){
            if (confirm)
                return R.drawable.confirm
            return  R.drawable.docs
        }
}
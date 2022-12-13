package promag.groupe.proapp.comercial.models

import com.google.gson.annotations.SerializedName
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.text.NumberFormat
import java.util.*

fun toPrice(value: Double, currency: String = "DA"): String {
    var format: NumberFormat = NumberFormat.getCurrencyInstance(Locale.FRANCE)
    val decimalFormatSymbols: DecimalFormatSymbols =
        (format as DecimalFormat).decimalFormatSymbols
    decimalFormatSymbols.currencySymbol = ""
    format.decimalFormatSymbols = decimalFormatSymbols
    val price: String = format.format(value).trim()
    return "$price $currency"
}

class Tier(
    var id: Int? = null,
    var label: String = "",
    var debit: Double = 0.0,
    var credit: Double = 0.0,
    var balance: Double = 0.0,
    var type: String = "t",
) : java.io.Serializable {
    val caption: String
        get() {
            if (label.isNullOrEmpty()) return "An"
            try {
                return "${label[0].uppercase()}${label[1].lowercase()}"

            } catch (exeception: Exception) {
                /*TODO index exception catch*/
            }
            return "${label[0].uppercase()}"

        }


}

class Payment(
    var id: Int? = null,
    var label: String = "",
    var tier: Int = 0,
    var amount: Double = 0.0,
    @SerializedName("tier_item")
    var tierItem: Tier? = null,
    var out: Boolean = false
) : java.io.Serializable {
    val caption: String
        get() {
            if (label.isNullOrEmpty()) return "An"
            try {
                return "${label[0].uppercase()}${label[1].lowercase()}"

            } catch (exeception: Exception) {
                /*TODO index exception catch*/
            }
            return "${label[0].uppercase()}"

        }

}
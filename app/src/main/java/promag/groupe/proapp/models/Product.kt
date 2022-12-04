package promag.groupe.proapp.models

import android.os.Build
import androidx.annotation.RequiresApi
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.text.NumberFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*

data class Product(
    var reference: String = "",
    var tonne: String = "",
    var designation: String = "",
    var qte_stock: Float = 0.0f,
    var value: Float = 0.0f,
    var picture: String?,
    var update_at: String = ""
) {

    val qte: String
        get() {
            return tonne.replace("and", ", ")
        }
    val ref: String
        get()  {
            val formated = reference.replace("_", " / ")
            return "$formated"
        }
//    var ref = this.reference.replace("_", "/")
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

    val date: String
        @RequiresApi(Build.VERSION_CODES.O)
        get()
        {
            val dtf: DateTimeFormatter = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss")
            val now: LocalDateTime = LocalDateTime.now()
            return ""
        }



}

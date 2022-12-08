package promag.groupe.proapp.comercial.models

import com.google.gson.annotations.SerializedName


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
    var montant: Double = 0.0,
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
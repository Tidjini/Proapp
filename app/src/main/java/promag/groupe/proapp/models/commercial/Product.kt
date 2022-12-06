package promag.groupe.proapp.models.commercial

import com.google.gson.annotations.SerializedName

class Product(
    var id: Int? = null,
    var name: String = "",
    @SerializedName("qte_stock")
    var qteStock: Double = 0.0,
    var value: Double = 0.0,
    var unite: String = "unite"
) {
}
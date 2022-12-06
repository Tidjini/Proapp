package promag.groupe.proapp.models.commercial

import com.google.gson.annotations.SerializedName


class StockMovement(
    var id: Int? = null,
    var item: Product = Product(),
    var product: Int? = null,
    var qte: Double = 0.0,
    @SerializedName("prix_unite")
    var prixUnite: Double = 0.0,
    var document: String = "Reception N° 0001",
    var out: Boolean = false

) : java.io.Serializable {
    val caption: String
        get() {
            if (document.isNullOrEmpty()) return "An"
            try {
                return "${document[0].uppercase()}${document[1].lowercase()}"

            } catch (exeception: Exception) {

            }
            return "${document[0].uppercase()}"

        }

}
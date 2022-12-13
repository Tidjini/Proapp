package promag.groupe.proapp.models.commercial

import com.google.gson.annotations.SerializedName

class Product(
    var id: Int? = null,
    var name: String = "",
    @SerializedName("qte_stock")
    var qteStock: Double = 0.0,
    var value: Double = 0.0,
    var unite: String = "unite",
    val compositions : List<ProductComposition> = listOf()
) : java.io.Serializable {
    val caption: String
        get() {
            if (name.isNullOrEmpty()) return "An"
            try {
                return "${name.substring(0, 4).uppercase()}"

            } catch (exeception: Exception) {

            }
            return "${name[0].uppercase()}"

        }

}

class ProductComposition(
    var id: Int? = null,
    @SerializedName("compose_name")
    var composeName: String = "",
    var qte: Double = 0.0,
    var product: Int? = 0,
    var composer: Int? = 0
) : java.io.Serializable {
    val caption: String
        get() {
            if (composeName.isNullOrEmpty()) return "An"
            try {
                return "${composeName.substring(0, 4).uppercase()}"

            } catch (exeception: Exception) {

            }
            return "${composeName[0].uppercase()}"

        }

}
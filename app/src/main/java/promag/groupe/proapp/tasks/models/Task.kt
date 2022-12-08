package promag.groupe.proapp.tasks.models

import com.google.gson.annotations.SerializedName
import promag.groupe.proapp.models.User

class Task(
    var id: Int? = null,
    var label: String = "",
    var description: String = "",
    var creator: Int = 0,
    var receiver: Int = 0,
    @SerializedName("receiver_item")
    var receiverItem: User? = null,
    var statue: String = "t",
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

    val shortDescription: String
        get() {
            if (description.isEmpty()) return "No Description to display"
            if (description.length > 70) return "${description.substring(0, 70)} ..."
            return description

        }

    fun statVerbose(statue: String): String {
        return when (statue) {
            "i" -> "Instance"
            "a" -> "Accepted"
            "c" -> "Canceled"
            "t" -> "Terminated"
            else -> "Instance"
        }

    }

    val statueVerbose: String
        get() {
            return when (statue) {
                "i" -> "Instance"
                "a" -> "Accepted"
                "c" -> "Canceled"
                "t" -> "Terminated"
                else -> "Instance"
            }

        }

    fun changeState(): String {
        return when (statue) {
            "i" -> {
                return "a"
            }
            "a" -> {
                return "t"
            }
            else -> return "a"
        }
    }

    fun cancel(): String {
        return "c"
    }


}
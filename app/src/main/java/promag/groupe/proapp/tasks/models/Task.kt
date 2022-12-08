package promag.groupe.proapp.tasks.models

class Task(
    var id: Int? = null,
    var label: String = "",
    var description: String = "",
    var creator: Int = 0,
    var receiver: Int = 0,
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

}
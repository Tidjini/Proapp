package promag.groupe.proapp.models


//todo remember to switch all to English
class User(
    var id: Int = 0,
    var username: String = "",
    var nom: String = "",
    var prenom: String = "",
    var photo: String? = null,
    var name: String = "",
    var token: String? = null
){

    val caption: String
        get() {
            if (username.isNullOrEmpty()) return "An"
            return "${username[0].uppercase()}${username[1].lowercase()}"
        }
}

data class Auth(
    var username: String = "",
    var password: String = ""
)
package promag.groupe.proapp.models


//todo remember to switch all to English
class User(
    var id: Int = 0,
    var username: String = "",
    var nom: String = "",
    var prenom: String = "",
    var photo: String?,
    var token: String?
)

class Auth(
    var username: String = "",
    var password: String = ""
)
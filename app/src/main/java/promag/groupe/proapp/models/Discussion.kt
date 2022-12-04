package promag.groupe.proapp.models

import android.os.Build
import androidx.annotation.RequiresApi
import com.google.gson.annotations.SerializedName
import promag.groupe.proapp.utils.difference
import java.time.LocalDateTime
import java.time.ZoneId
import java.util.*

class Discussion(
    var id: Int = 0,
    @SerializedName("participants_count")
    var participantsCount: Int = 0,
    var other: Int?,

    @SerializedName("other_user")
    var otherUser: User?,
    var name: String = "",
    var type: String = "s",
    @SerializedName("last_message")
    var lastMessage: Message?
) {


    val otherCaption: String
        get() {
            if (otherUser == null || otherUser!!.username.isNullOrEmpty()) return "An"
            val username = otherUser?.username
            if (username.isNullOrEmpty()) return "An"
            return "${username[0].uppercase()} ${username[1].lowercase()}"
        }

    val otherUsername: String
        get() {
            if (otherUser == null) return "Anonymous"
            val user = otherUser!!
            if (user.prenom.isNullOrEmpty()) return "Anonymous"
            if (user.nom.isNullOrEmpty()) return "Anonymous"
            val nom = user.nom[0].uppercase() + user.nom.substring(1, user.nom.length)
            val prenom = user.prenom[0].uppercase() + user.prenom.substring(1, user.prenom.length)
            return "$nom $prenom"
        }


    val shortMessage: String
        get() {
            if (lastMessage == null || lastMessage!!.message.isEmpty()) return "Discussion Vide"
            val message = lastMessage?.message ?: return "Discussion Vide"
            if (message.length > 70) return "${message.substring(0, 70)} ..."
            return message

        }

    val fromTime: String
        @RequiresApi(Build.VERSION_CODES.O)
        get() {
//            if (lastMessage == null || lastMessage!!.message.isEmpty()) return ""
//            val dateCreation = lastMessage!!.dateCreation
//            if (dateCreation.isNullOrEmpty()) return ""
////            val df: DateFormat = SimpleDateFormat(ISO_DATE_TIME_FORMAT)
//            val ldt: LocalDateTime = LocalDateTime.parse(dateCreation)
//
//            val date = Date.from(ldt.atZone(ZoneId.systemDefault()).toInstant());
//            val now = Date()
//            return now.difference(date)
            return "24 minute(s)"

        }
}

class Message(
    var id: Int = 0,
    var receiver: Int = 0,
    @SerializedName("date_creation")
    var dateCreation: String?,
    var message: String = "",
    var discussion: Int = 0,
    var sender: Int = 0
)
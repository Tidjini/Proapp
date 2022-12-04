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
    var other_user: User?,
    var name: String = "",
    var type: String = "s",
    @SerializedName("last_message")
    var lastMessage: Message?
) {


    val otherCaption: String
        get() {
            if (other_user == null || other_user!!.name.isEmpty()) return "A / N"
            val nom = other_user?.nom
            val prenom = other_user?.prenom
            if (nom.isNullOrEmpty() || prenom.isNullOrEmpty()) return "A / N"
            return "${nom[0].uppercase()} / ${prenom[0].uppercase()}"
        }


    val shortMessage: String
        get() {
            if (lastMessage == null || lastMessage!!.message.isEmpty()) return "Discussion Vide"
            val message = lastMessage?.message ?: return "Discussion Vide"
            if (message.length > 100) return "${message.substring(0, 100)} ..."
            return message
        }

    val fromTime: String
        @RequiresApi(Build.VERSION_CODES.O)
        get() {
            if (lastMessage == null || lastMessage!!.message.isEmpty()) return ""
            val dateCreation = lastMessage!!.dateCreation
            if (dateCreation.isNullOrEmpty()) return ""
//            val df: DateFormat = SimpleDateFormat(ISO_DATE_TIME_FORMAT)
            val ldt: LocalDateTime = LocalDateTime.parse(dateCreation)

            val date = Date.from(ldt.atZone(ZoneId.systemDefault()).toInstant());
            val now = Date()
            return now.difference(date)


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
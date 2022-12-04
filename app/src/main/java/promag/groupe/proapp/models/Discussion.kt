package promag.groupe.proapp.models

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import com.google.gson.annotations.SerializedName
import promag.groupe.proapp.ISO_DATE_TIME_FORMAT
import promag.groupe.proapp.utils.difference
import java.text.DateFormat
import java.text.ParseException
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
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
            if (lastMessage == null || lastMessage!!.message.isEmpty()) return "Nouvelle Discussion"
            val message = lastMessage?.message ?: return "Nouvelle Discussion"
            if (message.length > 70) return "${message.substring(0, 70)} ..."
            return message

        }

    val fromTime: String
        @RequiresApi(Build.VERSION_CODES.O)
        get() {
            if (lastMessage == null || lastMessage!!.message.isEmpty()) return ""
            var dateCreation = lastMessage!!.dateCreation
            if (dateCreation.isNullOrEmpty()) return ""
//            dateCreation = dateCreation.replace("T", " ")

            Log.d("DATE DIFF", "$this - $dateCreation: ")



            try {
//                val format = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.ssssss'Z'", Locale.FRANCE)
                val format = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSSSS'Z'")
                val date = LocalDateTime.parse(dateCreation, format);
//                val date = format.parse(dateCreation)
                val now = LocalDateTime.now()

                Log.d("DATE DIFF", "$now - $date: ")

                return now.difference(date)

            } catch (e: ParseException) {
                // TODO Auto-generated catch block
                e.printStackTrace()
                return "dateTime"

            }
//            val df: DateFormat = SimpleDateFormat(ISO_DATE_TIME_FORMAT)
//            val ldt: LocalDateTime = LocalDateTime.parse(dateCreation)
//
//            val date = Date.from(ldt.atZone(ZoneId.systemDefault()).toInstant());
//            val now = Date()
//            return now.difference(date)
//            return "24 minute(s)"

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
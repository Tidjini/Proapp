package promag.groupe.proapp.models

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import com.google.gson.annotations.SerializedName
import promag.groupe.proapp.utils.difference
import java.io.Serializable
import java.text.ParseException
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class Discussion(
    var id: Int = 0,
    @SerializedName("participants_count")
    var participantsCount: Int = 0,
    var other: User? = null,
    var name: String = "",
    var type: String = "s",
    @SerializedName("last_message")
    var lastMessage: Message? = null
) : Serializable {


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

            return try {
                //todo time zone issue GMT+1
                val format = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSSSS'Z'")
                val date = LocalDateTime.parse(dateCreation, format);
                val now = LocalDateTime.now()

                Log.d("DATE DIFF", "$now - $date: ")

                now.difference(date)

            } catch (e: ParseException) {
                e.printStackTrace()
                "dateTime"

            }


        }
}

class Message(
    var id: Int? = null,
    var receiver: User? = null,
    @SerializedName("date_creation")
    var dateCreation: String? = null,
    var message: String = "",
    var discussion: Int = 0,
    @SerializedName("notif_discussion")
    var notifDiscussion: Discussion?= null,
    @SerializedName("send_to")
    var sendTo: User? = null,
    var sender: Int = 0,
) : Serializable


object MessageProvider {
    val messges = ArrayList<Message>()

    val longMessage =
        """"Some Content in Here from, Some Content in Here from, 
    Some Content in Here from, Some Content in Here from, 
    Some Content in Here from
    Some Content in Here from
    Some Content in Here from
    Some Content in Here from""".trimMargin()

    init {
//        for (i in 1..100) {
//            val r = (1..50).random()
//            val ctn = if (r.mod(2) == 0) longMessage else "Some Content in Here from 1 to $i"
//            val sender = if (r.mod(2) == 0) 2 else 1
//            val msg = Message(
//                id = i, receiver = i, message = ctn,
//                discussion = 1, sender = sender.id, dateCreation = ""
//            )
//            messges.add(msg)
//        }
    }
}
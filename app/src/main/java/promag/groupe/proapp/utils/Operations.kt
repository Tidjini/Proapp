package promag.groupe.proapp.utils

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZoneOffset
import java.util.*
import kotlin.math.abs

//https://www.behance.net/gallery/86147197/Whatsapp-Redesign-Concept?tracking_source=search_projects%7Cwhatsapp

fun Date.difference(date: Date): String {

    Log.d("DATE DIFF", "$this - $date: ")
    val timeDiff: Long = abs(this.time - date.time)
    val seconds = timeDiff / 1000

    if (seconds < 60)
        return "$seconds second(s)"
    val minutes = seconds / 60
    if (minutes < 60)
        return "$minutes minute(s)"
    val hours = minutes / 60

    if (hours < 24)
        return "$hours heure(s)"

    val days = hours / 24

    return "$days jour(s)"
}
@RequiresApi(Build.VERSION_CODES.O)
fun LocalDateTime.difference(date: LocalDateTime): String {

    Log.d("DATE DIFF", "$this - $date: ")
    val seconds: Long = abs(this.toEpochSecond(ZoneOffset.UTC) - date.toEpochSecond(ZoneOffset.UTC))
//    val seconds = timeDiff / 1000

    if (seconds < 60)
        return "$seconds second(s)"
    val minutes = seconds / 60
    if (minutes < 60)
        return "$minutes minute(s)"
    val hours = minutes / 60

    if (hours < 24)
        return "$hours heure(s)"

    val days = hours / 24

    return "$days jour(s)"
}

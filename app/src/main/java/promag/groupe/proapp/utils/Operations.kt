package promag.groupe.proapp.utils

import java.util.Date

fun Date.difference(date: Date) : String{
    val diff: Long = this.time - date.time
    val seconds = diff / 1000
    val minutes = seconds / 60
    val hours = minutes / 60
    val days = hours / 24

    return "$minutes minutes, "
}
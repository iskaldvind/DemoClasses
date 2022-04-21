package studio.iskaldvind.democlasses.utils

fun formatTime(hours: Int, minutes: Int): String {
    val twoDigitMinutes = when {
        minutes == 0 -> "00"
        minutes < 10 -> "0$minutes"
        else -> "$minutes"
    }
    return "$hours:$twoDigitMinutes"
}
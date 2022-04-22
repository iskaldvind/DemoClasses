package studio.iskaldvind.democlasses.utils

fun timeLeft(diff: Long): Triple<Int, Int, Int> {
    val diffMinutes = diff/60000
    val days = diffMinutes / 1440
    val withoutDays = diffMinutes - days * 1440
    val hours = withoutDays / 60
    val minutes = withoutDays - hours * 60
    return Triple(days.toInt(), hours.toInt(), minutes.toInt())
}
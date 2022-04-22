package studio.iskaldvind.democlasses.data

import studio.iskaldvind.democlasses.R
import studio.iskaldvind.democlasses.model.AppClass
import studio.iskaldvind.democlasses.model.Homework
import java.util.*

val name = "Mike"
val examTime = computeExamTime()
private fun computeExamTime(): Long {
    val cal = Calendar.getInstance()
    cal.add(Calendar.DATE, 9)
    cal.add(Calendar.HOUR_OF_DAY, 11)
    cal.add(Calendar.MINUTE, 7)
    return cal.timeInMillis
}

fun computeTime(days: Int = 0, hours: Int, minutes: Int): Long {
    val cal = Calendar.getInstance()
    if (days > 0) cal.add(Calendar.DATE, days)
    cal.set(Calendar.HOUR_OF_DAY, hours)
    cal.set(Calendar.MINUTE, minutes)
    return cal.timeInMillis
}

val classes: List<AppClass> = listOf(
    AppClass(
        id = 0,
        title = "History",
        description = "Medieval history of China",
        icon = R.drawable.ic_history,
        start = computeTime(hours = 8, minutes = 0),
        end = computeTime(hours = 8, minutes = 45),
        teacher = "Mrs Thomas",
        isExtra = false,
        isOnline = true
    ),
    AppClass(
        id = 1,
        title = "Literature",
        description = "Shakespeare",
        icon = R.drawable.ic_literature,
        start = computeTime(hours = 9, minutes = 0),
        end = computeTime(hours = 9, minutes = 45),
        teacher = "Mrs Barros",
        isExtra = false,
        isOnline = false
    ),
    AppClass(
        id = 2,
        title = "Physical Education",
        description = "Intensive preparation for The Junior World Championship in Los Angeles",
        icon = R.drawable.ic_literature,
        start = computeTime(hours = 10, minutes = 0),
        end = computeTime(hours = 11, minutes = 35),
        teacher = "Mr Barros",
        isExtra = true,
        isOnline = false
    ),
    AppClass(
        id = 3,
        title = "English Lessons",
        description = "Future Continuous",
        icon = R.drawable.ic_english,
        start = computeTime(hours = 18, minutes = 0),
        end = computeTime(hours = 18, minutes = 45),
        teacher = "Mr Johnson",
        isExtra = false,
        isOnline = true
    ),
    AppClass(
        id = 4,
        title = "Programming",
        description = "Kotlin Coroutines",
        icon = R.drawable.ic_programming,
        start = computeTime(hours = 20, minutes = 0),
        end = computeTime(hours = 20, minutes = 45),
        teacher = "Mr Johnson",
        isExtra = false,
        isOnline = false
    ),
    AppClass(
        id = 5,
        title = "Biology",
        description = "Modern vaccines - pros & cons",
        icon = R.drawable.ic_biology,
        start = computeTime(hours = 22, minutes = 0),
        end = computeTime(hours = 22, minutes = 45),
        teacher = "Mrs Stones",
        isExtra = true,
        isOnline = false
    )
)

val homeworks: List<Homework> = listOf(
    Homework(
        id = 0,
        title = "Literature",
        icon = R.drawable.ic_literature,
        description = "Read scenes 1.1 - 1.12 of the Master and Margarita",
        deadline = computeTime(days = 2, hours = 10, minutes = 0)
    ),
    Homework(
        id = 1,
        title = "Physics",
        icon = R.drawable.ic_physics,
        description = "Learn Newtons Law of motion",
        deadline = computeTime(days = 5, hours = 10, minutes = 0)
    ),
    Homework(
        id = 2,
        title = "Programming",
        icon = R.drawable.ic_programming,
        description = "Learn Compose library",
        deadline = computeTime(days = 6, hours = 10, minutes = 0)
    )
)
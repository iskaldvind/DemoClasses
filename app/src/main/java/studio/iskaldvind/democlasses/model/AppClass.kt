package studio.iskaldvind.democlasses.model

data class AppClass(
    val id: Int,
    val title: String,
    val description: String,
    val icon: Int,
    val start: Long,
    val end: Long,
    val teacher: String,
    val isExtra: Boolean,
    val isOnline: Boolean
)
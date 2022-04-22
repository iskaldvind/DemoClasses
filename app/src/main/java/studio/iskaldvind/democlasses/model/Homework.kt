package studio.iskaldvind.democlasses.model

data class Homework(
    val id: Int,
    val title: String,
    val icon: Int,
    val deadline: Long,
    val description: String
)
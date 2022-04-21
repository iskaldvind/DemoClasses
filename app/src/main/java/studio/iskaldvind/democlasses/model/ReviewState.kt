package studio.iskaldvind.democlasses.model

data class ReviewState(
    val name: String = "",
    val examTime: Long = 0,
    val classes: List<AppClass> = listOf(),
    val homeworks: List<Homework> = listOf()
)
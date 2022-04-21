package studio.iskaldvind.democlasses.viewmodel

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import studio.iskaldvind.democlasses.base.BaseViewModel
import studio.iskaldvind.democlasses.model.ReviewState
import studio.iskaldvind.democlasses.repository.IRepository

class ReviewViewModel(
    private val repository: IRepository
): BaseViewModel() {

    private val _data = MutableStateFlow(ReviewState())
    val data: StateFlow<ReviewState> = _data

    fun getData() {
        viewModelCoroutineScope.launch {
            _data.value = ReviewState(
                name = repository.getName(),
                examTime = repository.getExamTime(),
                classes = repository.getClasses(),
                homeworks = repository.getHomeworks()
            )
        }
    }

    override fun handleError(error: Throwable) {}
}
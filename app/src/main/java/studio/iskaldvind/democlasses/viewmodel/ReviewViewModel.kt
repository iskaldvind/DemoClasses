package studio.iskaldvind.democlasses.viewmodel

import android.os.Handler
import android.os.Looper
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import studio.iskaldvind.democlasses.base.BaseViewModel
import studio.iskaldvind.democlasses.model.ReviewState
import studio.iskaldvind.democlasses.repository.IRepository
import java.util.*

class ReviewViewModel(
    private val repository: IRepository
): BaseViewModel() {

    private val _data = MutableStateFlow(ReviewState())
    val data: StateFlow<ReviewState> = _data
    private val _time = MutableStateFlow(getCurrentTime())
    val time: StateFlow<Long> = _time
    private val handler = Handler(Looper.getMainLooper())

    private val timerTask = object : Runnable {
        override fun run() {
            viewModelCoroutineScope.launch {
                _time.value = getCurrentTime()
            }
            handler.postDelayed(this, 60000)
        }
    }

    fun getData() {
        startTimer()
        viewModelCoroutineScope.launch {
            _data.value = ReviewState(
                name = repository.getName(),
                examTime = repository.getExamTime(),
                classes = repository.getClasses(),
                homeworks = repository.getHomeworks()
            )
        }
    }

    private fun startTimer() {
        handler.post(timerTask)
    }

    private fun getCurrentTime(): Long =
        (Calendar.getInstance().timeInMillis/60000).toInt().toLong() * 60000

    override fun handleError(error: Throwable) {
        //
    }

    override fun onCleared() {
        handler.removeCallbacks(timerTask)
        super.onCleared()
    }
}
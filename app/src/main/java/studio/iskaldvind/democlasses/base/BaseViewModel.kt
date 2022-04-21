package studio.iskaldvind.democlasses.base

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.*

abstract class BaseViewModel : ViewModel() {

    val viewModelCoroutineScope = CoroutineScope(
        Dispatchers.Default + SupervisorJob() + CoroutineExceptionHandler { _, throwable ->
            handleError(throwable)
        }
    )

    override fun onCleared() {
        super.onCleared()
        cancelJob()
    }

    private fun cancelJob() =
        viewModelCoroutineScope.coroutineContext.cancelChildren()

    abstract fun handleError(error: Throwable)
}
package studio.iskaldvind.democlasses.viewmodel

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import studio.iskaldvind.democlasses.base.BaseViewModel
import studio.iskaldvind.democlasses.model.AppClass
import studio.iskaldvind.democlasses.repository.IRepository

class ClassesViewModel(
    private val repository: IRepository
): BaseViewModel() {

    private val _data = MutableStateFlow<List<AppClass>>(listOf())
    val data: StateFlow<List<AppClass>> = _data

    fun getData() {
        viewModelCoroutineScope.launch {
            _data.value = repository.getClasses()
        }
    }

    override fun handleError(error: Throwable) {}
}
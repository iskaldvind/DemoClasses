package studio.iskaldvind.democlasses.view

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import by.kirich1409.viewbindingdelegate.viewBinding
import com.xwray.groupie.GroupieAdapter
import kotlinx.coroutines.launch
import studio.iskaldvind.democlasses.base.BaseFragment
import studio.iskaldvind.democlasses.R.layout.classes_fragment
import studio.iskaldvind.democlasses.databinding.ClassesFragmentBinding
import studio.iskaldvind.democlasses.viewmodel.ClassesViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel
import studio.iskaldvind.democlasses.model.AppClass
import java.util.*

class ClassesFragment : BaseFragment(classes_fragment) {

    companion object {
        const val TAG = "CLASSES"
        fun newInstance(): Fragment = ClassesFragment()
    }

    private val binding: ClassesFragmentBinding by viewBinding()
    private val viewModel: ClassesViewModel by viewModel()
    private val activity: MainActivity by lazy { requireActivity() as MainActivity }
    private val classesAdapter: GroupieAdapter by lazy { GroupieAdapter() }
    private val classes: MutableList<ClassItem> = mutableListOf()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        activity.setActiveNav(tag = TAG)
        with(binding) {
            recycler.adapter = classesAdapter
        }
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.data.collect { data ->
                    handleData(data = data)
                }
            }
        }
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.time.collect { time ->
                    handleTime(time = time)
                }
            }
        }
        viewModel.getData()
    }

    private fun handleData(data: List<AppClass>) {
        removeOldClasses(newClasses = data)
        updateClasses(newClasses = data)
        scrollClasses()
    }

    private fun removeOldClasses(newClasses: List<AppClass>) {
        val newClassesIds = newClasses.map { it.id }
        classes.forEach { currentClass ->
            if (!newClassesIds.contains(currentClass.item.id)) {
                val index = classes.indexOf(currentClass)
                classes.remove(currentClass)
                classesAdapter.notifyItemRemoved(index)
            }
        }
    }

    private fun updateClasses(newClasses: List<AppClass>) {
        val currentHomeworksIds = classes.map { it.item.id }
        newClasses.forEach { newClass ->
            if (!currentHomeworksIds.contains(newClass.id)) {
                if (classes.isEmpty() || classes[0].item.start > newClass.start) {
                    addToAdapter(0, newClass)
                } else if (classes.last().item.start < newClass.start) {
                    addToAdapter(-1, newClass)
                } else {
                    val times = classes.map { it.item.start }
                    for (time in times) {
                        if (time > newClass.start) {
                            val index = times.indexOf(time)
                            addToAdapter(index, newClass)
                            break
                        }
                    }
                }
            }
        }
        updateClassesStates()
    }

    private fun addToAdapter(index: Int, appClass: AppClass) {
        val item = ClassItem(requireContext(), resources, appClass) { activity.openSkype() }
        if (index == -1) {
            classes.add(item)
            classesAdapter.add(item)
        } else {
            classes.add(index, item)
            classesAdapter.add(index, item)
        }
    }

    private fun updateClassesStates() {
        val now = viewModel.time.value
        var activeIsFound = false
        for ((index, classItem) in classes.withIndex()) {
            if (index == 0 && classItem.item.end > now && classItem.state != ClassState.ACTIVE) {
                classItem.state = ClassState.ACTIVE
                (classesAdapter.getItem(index) as ClassItem).update(newState = ClassState.ACTIVE)
                activeIsFound = true
            } else if (index == 0 && classItem.state != ClassState.FIRST) {
                (classesAdapter.getItem(index) as ClassItem).update(newState = ClassState.FIRST)
            } else if (index == classes.size - 1 && !activeIsFound && classItem.item.end > now
                && classItem.state != ClassState.ACTIVE_LAST) {
                classItem.state = ClassState.ACTIVE_LAST
                (classesAdapter.getItem(index) as ClassItem).update(newState = ClassState.ACTIVE_LAST)
            } else if (index == classes.size - 1 && classItem.state != ClassState.LAST) {
                classItem.state = ClassState.LAST
                (classesAdapter.getItem(index) as ClassItem).update(newState = ClassState.LAST)
            } else if (classItem.item.end > now && !activeIsFound && classItem.state != ClassState.ACTIVE) {
                classItem.state = ClassState.ACTIVE
                (classesAdapter.getItem(index) as ClassItem).update(newState = ClassState.ACTIVE)
                activeIsFound = true
            } else if (classItem.state != ClassState.GENERAL) {
                classItem.state = ClassState.GENERAL
                (classesAdapter.getItem(index) as ClassItem).update(newState = ClassState.GENERAL)
            }
        }
    }

    private fun scrollClasses() {
        val timeNow = viewModel.time.value / 60000
        if (classes.size > 1) {
            val times = classes.map { it.item.start/60000 }
            var index = 0
            for (time in times) {
                if (time >= timeNow) {
                    binding.recycler.scrollToPosition(index)
                } else {
                    index++
                }
            }
            if (index == times.size) {
                binding.recycler.scrollToPosition(index)
            }
        }
    }

    private fun handleTime(time: Long) {
        scrollClasses()
        val cal = Calendar.getInstance()
        cal.timeInMillis = time
        val date = cal.get(Calendar.DAY_OF_MONTH)
        val month = cal.getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.US)
        val dateText = "Today, $date $month"
        binding.date.text = dateText
    }
}
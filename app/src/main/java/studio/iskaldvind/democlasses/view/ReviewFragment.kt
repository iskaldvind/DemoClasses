package studio.iskaldvind.democlasses.view

import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import by.kirich1409.viewbindingdelegate.viewBinding
import com.xwray.groupie.GroupieAdapter
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel
import studio.iskaldvind.democlasses.base.BaseFragment
import studio.iskaldvind.democlasses.R.layout.review_fragment
import studio.iskaldvind.democlasses.databinding.ReviewFragmentBinding
import studio.iskaldvind.democlasses.model.AppClass
import studio.iskaldvind.democlasses.model.Homework
import studio.iskaldvind.democlasses.model.ReviewState
import studio.iskaldvind.democlasses.utils.timeLeft
import studio.iskaldvind.democlasses.viewmodel.ReviewViewModel

class ReviewFragment : BaseFragment(review_fragment) {
    companion object {
        const val TAG = "REVIEW"
        fun newInstance(): Fragment = ReviewFragment()
    }

    private val binding: ReviewFragmentBinding by viewBinding()
    private val viewModel: ReviewViewModel by viewModel()
    private val activity: MainActivity by lazy { requireActivity() as MainActivity }
    private val classes: MutableList<AppClass> = mutableListOf()
    private val homeworks: MutableList<Homework> = mutableListOf()
    private val classesAdapter by lazy {
        ClassesPagerAdapter(context = requireContext(), classes = classes) { activity.openSkype() }
    }
    private val homeworkAdapter: GroupieAdapter by lazy { GroupieAdapter() }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        activity.setActiveNav(tag = TAG)
        with(binding) {
            hello.text = "Hello!"
            classesPager.adapter = classesAdapter
            homeworkPager.adapter = homeworkAdapter
        }
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.data.collect { data ->
                    drawData(data = data)
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

    private fun drawData(data: ReviewState) {
        with(binding) {
            if (data.name.isNotBlank()) {
                val helloText = "Hello, ${data.name}"
                hello.text = helloText
            }
            removeOldClasses(newClasses = data.classes)
            updateClasses(newClasses = data.classes)
            scrollClasses()
            val classesCountText = "${classes.size} classes today"
            classesCount.text = classesCountText
            removeOldHomeworks(newHomeworks = data.homeworks)
            updateHomeworks(newHomeworks = data.homeworks)
            scrollHomeworks()
        }
    }

    private fun removeOldClasses(newClasses: List<AppClass>) {
        val newClassesIds = newClasses.map { it.id }
        classes.forEach { currentClass ->
            if (!newClassesIds.contains(currentClass.id)) {
                val index = classes.indexOf(currentClass)
                classes.remove(currentClass)
                classesAdapter.notifyItemRemoved(index)
            }
        }
    }

    private fun updateClasses(newClasses: List<AppClass>) {
        val currentClassesIds = classes.map { it.id }
        newClasses.forEach { newClass ->
            if (!currentClassesIds.contains(newClass.id)) {
                if (classes.isEmpty() || classes[0].start > newClass.start) {
                    classes.add(0, newClass)
                    classesAdapter.notifyItemInserted(0)
                } else if (classes.last().start < newClass.start) {
                    classes.add(newClass)
                    classesAdapter.notifyItemInserted(classes.size - 1)
                } else {
                    val times = classes.map { it.start }
                    for (time in times) {
                        if (time > newClass.start) {
                            val index = times.indexOf(time)
                            classes.add(index, newClass)
                            classesAdapter.notifyItemInserted(index)
                            break
                        }
                    }
                }
            }
        }
    }

    private fun scrollClasses() {
        val timeNow = viewModel.time.value / 60000
        if (classes.size > 1) {
            val times = classes.map { Pair(it.start/60000, it.end/60000) }
            var index = 0
            for (time in times) {
                if (time.second >= timeNow) {
                    binding.classesPager.currentItem = index
                } else {
                    index++
                }
            }
            if (index == times.size) {
                binding.classesPager.currentItem = index - 1
            }
        }
    }

    private fun removeOldHomeworks(newHomeworks: List<Homework>) {
        val newCHomeworksIds = newHomeworks.map { it.id }
        homeworks.forEach { currentHomework ->
            if (!newCHomeworksIds.contains(currentHomework.id)) {
                val index = homeworks.indexOf(currentHomework)
                homeworks.remove(currentHomework)
                homeworkAdapter.notifyItemRemoved(index)
            }
        }
    }

    private fun updateHomeworks(newHomeworks: List<Homework>) {
        val currentHomeworksIds = homeworks.map { it.id }
        newHomeworks.forEach { newHomework ->
            if (!currentHomeworksIds.contains(newHomework.id)) {
                if (homeworks.isEmpty() || homeworks[0].deadline > newHomework.deadline) {
                    homeworks.add(0, newHomework)
                    homeworkAdapter.add(
                        0,
                        HomeworkItem(context = requireContext(), homework = newHomework)
                    )
                } else if (homeworks.last().deadline < newHomework.deadline) {
                    homeworks.add(newHomework)
                    homeworkAdapter.add(
                        homeworks.size - 1,
                        HomeworkItem(context = requireContext(), homework = newHomework)
                    )
                } else {
                    val times = homeworks.map { it.deadline }
                    for (time in times) {
                        if (time > newHomework.deadline) {
                            val index = times.indexOf(time)
                            homeworks.add(index, newHomework)
                            homeworkAdapter.add(
                                HomeworkItem(context = requireContext(), homework = newHomework)
                            )
                            break
                        }
                    }
                }
            }
        }
    }

    private fun scrollHomeworks() {
        val timeNow = viewModel.time.value / 60000
        if (homeworks.size > 1) {
            val times = homeworks.map { it.deadline/60000 }
            var index = 0
            for (time in times) {
                if (time >= timeNow) {
                    binding.homeworkPager.scrollToPosition(index)
                } else {
                    index++
                }
            }
            if (index == times.size) {
                binding.homeworkPager.scrollToPosition(index)
            }
        }
    }

    private fun handleTime(time: Long) {
        scrollClasses()
        val examTime = viewModel.data.value.examTime
        val left = timeLeft(examTime - time)
        fillDigits(value = left.first, tensDigit = binding.tensDays, numbersDigit = binding.numberDays)
        fillDigits(value = left.second, tensDigit = binding.tensHours, numbersDigit = binding.numberHours)
        fillDigits(value = left.third, tensDigit = binding.tensMinutes, numbersDigit = binding.numberMinutes)
    }

    private fun fillDigits(value: Int, tensDigit: TextView, numbersDigit: TextView) {
        if (value > 10) {
            val tens = value / 10
            tensDigit.text = tens.toString()
            val numbers = value - tens * 10
            numbersDigit.text = numbers.toString()
        } else if (value > 0) {
            tensDigit.text = "0"
            numbersDigit.text = value.toString()
        } else {
            tensDigit.text = "0"
            numbersDigit.text = "0"
        }
    }
}
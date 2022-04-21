package studio.iskaldvind.democlasses.view

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import by.kirich1409.viewbindingdelegate.viewBinding
import kotlinx.coroutines.launch
import studio.iskaldvind.democlasses.base.BaseFragment
import studio.iskaldvind.democlasses.R.layout.classes_fragment
import studio.iskaldvind.democlasses.databinding.ClassesFragmentBinding
import studio.iskaldvind.democlasses.viewmodel.ClassesViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class ClassesFragment : BaseFragment(classes_fragment) {

    companion object {
        const val TAG = "CLASSES"
        fun newInstance(): Fragment = ClassesFragment()
    }

    private val binding: ClassesFragmentBinding by viewBinding()
    private val viewModel: ClassesViewModel by viewModel()
    private val activity: MainActivity by lazy { requireActivity() as MainActivity }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        activity.setActiveNav(tag = TAG)
        with(binding) {
        }
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.data.collect { data ->
                    //
                }
            }
        }
        viewModel.getData()
    }
}
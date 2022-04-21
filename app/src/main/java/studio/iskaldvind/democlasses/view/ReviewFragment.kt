package studio.iskaldvind.democlasses.view

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import by.kirich1409.viewbindingdelegate.viewBinding
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel
import studio.iskaldvind.democlasses.base.BaseFragment
import studio.iskaldvind.democlasses.R.layout.review_fragment
import studio.iskaldvind.democlasses.databinding.ReviewFragmentBinding
import studio.iskaldvind.democlasses.model.ReviewState
import studio.iskaldvind.democlasses.viewmodel.ReviewViewModel

class ReviewFragment : BaseFragment(review_fragment) {
    companion object {
        const val TAG = "REVIEW"
        fun newInstance(): Fragment = ReviewFragment()
    }

    private val binding: ReviewFragmentBinding by viewBinding()
    private val viewModel: ReviewViewModel by viewModel()
    private val activity: MainActivity by lazy { requireActivity() as MainActivity }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        activity.setActiveNav(tag = TAG)
        with(binding) {
            hello.text = "Hello!"
        }
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.data.collect { data ->
                    drawData(data = data)
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
        }
    }
}
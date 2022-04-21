package studio.iskaldvind.democlasses.view

import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import by.kirich1409.viewbindingdelegate.viewBinding
import studio.iskaldvind.democlasses.R
import studio.iskaldvind.democlasses.base.BaseActivity
import studio.iskaldvind.democlasses.R.layout.main_activity
import studio.iskaldvind.democlasses.databinding.MainActivityBinding

class MainActivity: BaseActivity(main_activity) {

    private val binding: MainActivityBinding by viewBinding()
    data class NavData(
        val tag: String,
        val container: LinearLayout,
        val icon: ImageView,
        val label: TextView
    )
    private var navsData: List<NavData> = listOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        with(binding) {
            navHome.setOnClickListener { navigateToReview() }
            navToday.setOnClickListener { navigateToClasses() }
            navsData = listOf(
                NavData(
                    tag = ReviewFragment.TAG,
                    container = navHome,
                    icon = navHomeIcon,
                    label = navHomeLabel
                ),
                NavData(
                    tag = ClassesFragment.TAG,
                    container = navToday,
                    icon = navTodayIcon,
                    label = navTodayLabel
                )
            )
        }
        navigateToReview()
    }

    private fun navigateToReview() =
        navigate(ReviewFragment.newInstance(), ReviewFragment.TAG)

    private fun navigateToClasses() =
        navigate(ClassesFragment.newInstance(), ClassesFragment.TAG)

    private fun navigate(fragment: Fragment, tag: String) =
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.fragment, fragment, tag)
            .addToBackStack(tag)
            .commit()

    fun setActiveNav(tag: String) {
        navsData.forEach { nav ->
            val background = if (nav.tag == tag)
                R.drawable.nav_background_active else R.drawable.nav_background_inactive
            nav.container.background = ContextCompat.getDrawable(this, background)
            val tint = if (nav.tag == tag)
                R.color.secondary else R.color.primary_text
            nav.icon.setColorFilter(this.getColor(tint))
            nav.label.setTextColor(this.getColor(tint))
            nav.label.visibility = if (nav.tag == tag) View.VISIBLE else View.GONE
        }
    }
}
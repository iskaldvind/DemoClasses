package studio.iskaldvind.democlasses.view

import android.content.Context
import android.content.res.Resources
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.xwray.groupie.viewbinding.BindableItem
import studio.iskaldvind.democlasses.R
import studio.iskaldvind.democlasses.databinding.ClassCardItemBinding
import studio.iskaldvind.democlasses.model.AppClass
import studio.iskaldvind.democlasses.utils.formatTime

import java.util.*

enum class ClassState {
    FIRST, ACTIVE, GENERAL, LAST, ACTIVE_LAST
}

class ClassItem(
    private val context: Context,
    resources: Resources,
    val item: AppClass,
    var state: ClassState = ClassState.GENERAL,
    private val onlineCallback: () -> Unit
) : BindableItem<ClassCardItemBinding>() {

    private val borderBgLeft = ContextCompat.getDrawable(context, R.drawable.line_background_left)
    private val borderBgRight = ContextCompat.getDrawable(context, R.drawable.line_background_right)
    private val borderBgInactive = ContextCompat.getDrawable(context, R.color.primary_dark)
    private val cardBgCommon = ContextCompat.getDrawable(context, R.drawable.card_background_full)
    private val cardBgExt = ContextCompat.getDrawable(context, R.drawable.timer_background)
    private var _binding: ClassCardItemBinding? = null
    private val rootHeight = resources
        .getDimensionPixelSize(R.dimen.class_card_container_height)
    private val rootHeightExt = resources
        .getDimensionPixelSize(R.dimen.class_card_ext_container_height)
    private val borderHeight = resources
        .getDimensionPixelSize(R.dimen.class_card_border_height)
    private val borderHeightExt = resources
        .getDimensionPixelSize(R.dimen.class_card_ext_border_height)
    private val cardHeight = resources
        .getDimensionPixelSize(R.dimen.class_card_height)
    private val cardHeightExt = resources
        .getDimensionPixelSize(R.dimen.class_card_ext_height)

    override fun getLayout(): Int = R.layout.class_card_item

    override fun initializeViewBinding(view: View): ClassCardItemBinding =
        ClassCardItemBinding.bind(view)

    override fun bind(binding: ClassCardItemBinding, position: Int) =
        onBind(binding = binding)

    private fun onBind(binding: ClassCardItemBinding) {
        _binding = binding
        with(binding) {
            val rootLayoutParams = root.layoutParams as RecyclerView.LayoutParams
            rootLayoutParams.height = if (item.isExtra) rootHeightExt else rootHeight
            root.layoutParams = rootLayoutParams
            val leftBorderLayoutParams = leftBorder.layoutParams as ConstraintLayout.LayoutParams
            leftBorderLayoutParams.height = if (item.isExtra) borderHeightExt else borderHeight
            leftBorder.layoutParams = leftBorderLayoutParams
            val rightBorderLayoutParams = rightBorder.layoutParams as ConstraintLayout.LayoutParams
            rightBorderLayoutParams.height = if (item.isExtra) rootHeightExt else rootHeight
            rightBorder.layoutParams = rightBorderLayoutParams
            val cardLayoutParams = cardContainer.layoutParams as ConstraintLayout.LayoutParams
            cardLayoutParams.height = if (item.isExtra) cardHeightExt else cardHeight
            cardContainer.layoutParams = cardLayoutParams
            cardContainer.background = if (item.isExtra) cardBgExt else cardBgCommon
            title.text = item.title
            Glide.with(context)
                .load(item.icon)
                .into(icon)
            val cal = Calendar.getInstance()
            cal.timeInMillis = item.start
            val start = formatTime(
                hours = cal.get(Calendar.HOUR_OF_DAY),
                minutes = cal.get(Calendar.MINUTE)
            )
            cal.timeInMillis = item.end
            val end = formatTime(
                hours = cal.get(Calendar.HOUR_OF_DAY),
                minutes = cal.get(Calendar.MINUTE)
            )
            val timeStr = "$start - $end"
            time.text = timeStr
            val teacherStr = "Teacher: ${item.teacher}"
            teacher.text = teacherStr
            onlineContainer.visibility = if (item.isOnline && !item.isExtra)
                View.VISIBLE else View.GONE
            onlineContainer.setOnClickListener { onlineCallback.invoke() }
            if (item.isExtra) {
                description.visibility = View.VISIBLE
                description.text = item.description
            } else {
                description.visibility = View.GONE
            }
            dotActive.visibility = if (
                state == ClassState.ACTIVE || state == ClassState.ACTIVE_LAST
            ) View.VISIBLE else View.GONE
            dotInactive.visibility = if (
                state == ClassState.ACTIVE || state == ClassState.ACTIVE_LAST
            ) View.GONE else View.VISIBLE
            if (state == ClassState.FIRST) {
                placeholder.background = borderBgInactive
                time.background = borderBgInactive
            } else {
                placeholder.background = borderBgLeft
                time.background = borderBgRight
            }
            if (state == ClassState.LAST && state == ClassState.ACTIVE_LAST) {
                leftBorder.background = borderBgInactive
                rightBorder.background = borderBgInactive
            } else {
                leftBorder.background = borderBgLeft
                rightBorder.background = borderBgRight
            }
        }
    }

    fun update(newState: ClassState) {
        state = newState
        _binding?.let{ binding ->
            onBind(binding = binding)
        }
    }
}
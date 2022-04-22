package studio.iskaldvind.democlasses.view

import android.content.Context
import android.view.View
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.xwray.groupie.viewbinding.BindableItem
import studio.iskaldvind.democlasses.R
import studio.iskaldvind.democlasses.databinding.HomeworkItemBinding
import studio.iskaldvind.democlasses.model.Homework
import studio.iskaldvind.democlasses.utils.timeLeft
import java.util.*

class HomeworkItem(
    private val context: Context,
    private val homework: Homework,
) : BindableItem<HomeworkItemBinding>() {

    private val textNormal = R.color.primary_light
    private val textDanger = R.color.accent

    override fun getLayout(): Int = R.layout.homework_item

    override fun initializeViewBinding(view: View): HomeworkItemBinding =
        HomeworkItemBinding.bind(view)

    override fun bind(binding: HomeworkItemBinding, position: Int) {
        with(binding) {
            with(binding) {
                title.text = homework.title
                Glide.with(context)
                    .load(homework.icon)
                    .into(icon)
                val cal = Calendar.getInstance()
                val now = cal.timeInMillis
                cal.timeInMillis = homework.deadline
                val deadline = cal.timeInMillis
                val diff = deadline - now
                if (diff <= 0) {
                    time.text = "Time has run out"
                    time.setTextColor(ContextCompat.getColor(context, textDanger))
                } else {
                    val left = timeLeft(diff)
                    when {
                        left.first > 0 -> {
                            val timeStr = "${left.first} days left"
                            val color = if (left.first > 2) textNormal else textDanger
                            time.setTextColor(ContextCompat.getColor(context, color))
                            clock.setColorFilter(ContextCompat.getColor(context, color))
                            time.text = timeStr
                        }
                        left.second > 0 -> {
                            val timeStr = "${left.second} hours left"
                            time.setTextColor(ContextCompat.getColor(context, textDanger))
                            clock.setColorFilter(ContextCompat.getColor(context, textDanger))
                            time.text = timeStr
                        }
                        left.third > 0 -> {
                            val timeStr = "${left.third} minutes left"
                            time.setTextColor(ContextCompat.getColor(context, textDanger))
                            clock.setColorFilter(ContextCompat.getColor(context, textDanger))
                            time.text = timeStr
                        }
                    }
                }
                description.text = homework.description
            }
        }
    }
}
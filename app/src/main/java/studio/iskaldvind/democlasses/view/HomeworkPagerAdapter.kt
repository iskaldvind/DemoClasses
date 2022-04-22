package studio.iskaldvind.democlasses.view

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import by.kirich1409.viewbindingdelegate.viewBinding
import com.bumptech.glide.Glide
import studio.iskaldvind.democlasses.R
import studio.iskaldvind.democlasses.databinding.HomeworkItemBinding
import studio.iskaldvind.democlasses.model.Homework
import studio.iskaldvind.democlasses.utils.timeLeft
import java.util.*

class HomeworkPagerAdapter(
    private val homeworks: List<Homework>,
    private val context: Context
): RecyclerView.Adapter<HomeworkVH>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeworkVH =
        HomeworkVH(
            context = context,
            itemView = LayoutInflater.from(parent.context)
                .inflate(R.layout.homework_item, parent, false)
        )

    override fun getItemCount(): Int =
        homeworks.size

    override fun onBindViewHolder(holder: HomeworkVH, position: Int) {
        holder.bind(homeworks[position])
    }
}

class HomeworkVH(private val context: Context, itemView: View) : RecyclerView.ViewHolder(itemView) {

    private val binding: HomeworkItemBinding by viewBinding(HomeworkItemBinding::bind)
    private val textNormal = R.color.primary_light
    private val textDanger = R.color.accent

    fun bind(homeworkItem: Homework) {
        with(binding) {
            title.text = homeworkItem.title
            Glide.with(context)
                .load(homeworkItem.icon)
                .into(icon)
            val cal = Calendar.getInstance()
            val now = cal.timeInMillis
            cal.timeInMillis = homeworkItem.deadline
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
            description.text = homeworkItem.description
        }
    }
}
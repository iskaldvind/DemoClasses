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
import studio.iskaldvind.democlasses.databinding.ClassItemBinding
import studio.iskaldvind.democlasses.model.AppClass
import studio.iskaldvind.democlasses.utils.formatTime
import java.util.*

class ClassesPagerAdapter(
    private val classes: List<AppClass>,
    private val context: Context,
    private val onlineCallback: () -> Unit
): RecyclerView.Adapter<ClassVH>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ClassVH =
        ClassVH(
            context = context,
            itemView = LayoutInflater.from(parent.context)
                .inflate(R.layout.class_item, parent, false),
            onlineCallback = onlineCallback
        )

    override fun getItemCount(): Int =
        classes.size

    override fun onBindViewHolder(holder: ClassVH, position: Int) {
        holder.bind(classes[position])
    }
}

class ClassVH(
    private val context: Context,
    itemView: View,
    private val onlineCallback: () -> Unit
) : RecyclerView.ViewHolder(itemView) {

    private val binding: ClassItemBinding by viewBinding(ClassItemBinding::bind)
    private val normalBg = R.drawable.card_background_full
    private val extendedBg = R.drawable.timer_background
    private val textNormal = R.color.primary_light
    private val textExtra = R.color.primary_text

    fun bind(classItem: AppClass) {
        with(binding) {
            title.text = classItem.title
            Glide.with(context)
                .load(classItem.icon)
                .into(icon)
            val cal = Calendar.getInstance()
            cal.timeInMillis = classItem.start
            val start = formatTime(
                hours = cal.get(Calendar.HOUR_OF_DAY),
                minutes = cal.get(Calendar.MINUTE)
            )
            cal.timeInMillis = classItem.end
            val end = formatTime(
                hours = cal.get(Calendar.HOUR_OF_DAY),
                minutes = cal.get(Calendar.MINUTE)
            )
            val timeStr = "$start - $end"
            time.text = timeStr
            onlineContainer.visibility = if (classItem.isOnline) View.VISIBLE else View.GONE
            onlineContainer.setOnClickListener { onlineCallback.invoke() }
            val background = if (classItem.isExtra) extendedBg else normalBg
            root.background = ContextCompat.getDrawable(context, background)
            val textColor = if (classItem.isExtra) textExtra else textNormal
            time.setTextColor(ContextCompat.getColor(context, textColor))
        }
    }
}
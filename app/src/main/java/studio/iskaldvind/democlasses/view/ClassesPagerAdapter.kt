package studio.iskaldvind.democlasses.view

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import studio.iskaldvind.democlasses.R

class ClassesPagerAdapter(private val classes: List<ClassItem>): RecyclerView.Adapter<ClassVH>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ClassVH =
        ClassVH(LayoutInflater.from(parent.context).inflate(R.layout.class_item, parent, false))

    override fun getItemCount(): Int =

}

class ClassVH(itemView: View) : RecyclerView.ViewHolder(itemView)
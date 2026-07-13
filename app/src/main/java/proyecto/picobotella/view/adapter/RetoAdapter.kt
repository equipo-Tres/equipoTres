package proyecto.picobotella.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import proyecto.picobotella.R
import proyecto.picobotella.model.RetoEntity
import proyecto.picobotella.view.viewholder.RetoViewHolder

class RetoAdapter(
    private val onEditClick: (RetoEntity) -> Unit,
    private val onDeleteClick: (RetoEntity) -> Unit
) : ListAdapter<RetoEntity, RetoViewHolder>(RetoDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RetoViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_reto, parent, false)
        return RetoViewHolder(view, onEditClick, onDeleteClick)
    }

    override fun onBindViewHolder(holder: RetoViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}

private class RetoDiffCallback : DiffUtil.ItemCallback<RetoEntity>() {
    override fun areItemsTheSame(oldItem: RetoEntity, newItem: RetoEntity): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: RetoEntity, newItem: RetoEntity): Boolean {
        return oldItem == newItem
    }
}

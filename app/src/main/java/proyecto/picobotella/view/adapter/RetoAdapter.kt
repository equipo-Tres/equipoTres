package proyecto.picobotella.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import proyecto.picobotella.R
import proyecto.picobotella.model.RetoEntity
import proyecto.picobotella.view.viewholder.RetoViewHolder

class RetoAdapter(
    private val onEditClick: (RetoEntity) -> Unit,
    private val onDeleteClick: (RetoEntity) -> Unit
) : RecyclerView.Adapter<RetoViewHolder>() {

    private var retos: List<RetoEntity> = emptyList()

    fun submitList(newRetos: List<RetoEntity>) {
        retos = newRetos
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RetoViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_reto, parent, false)
        return RetoViewHolder(view, onEditClick, onDeleteClick)
    }

    override fun onBindViewHolder(holder: RetoViewHolder, position: Int) {
        holder.bind(retos[position])
    }

    override fun getItemCount(): Int = retos.size
}

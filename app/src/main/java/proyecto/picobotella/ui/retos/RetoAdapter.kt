package proyecto.picobotella.ui.retos

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import proyecto.picobotella.R
import proyecto.picobotella.data.local.RetoEntity

class RetoAdapter(
    private val onDeleteClick: (RetoEntity) -> Unit
) : RecyclerView.Adapter<RetoAdapter.RetoViewHolder>() {

    private var retos: List<RetoEntity> = emptyList()

    fun submitList(newRetos: List<RetoEntity>) {
        retos = newRetos
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RetoViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_reto, parent, false)
        return RetoViewHolder(view)
    }

    override fun onBindViewHolder(holder: RetoViewHolder, position: Int) {
        holder.bind(retos[position])
    }

    override fun getItemCount(): Int = retos.size

    inner class RetoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(reto: RetoEntity) {
            itemView.findViewById<TextView>(R.id.txtRetoDescription).text = reto.description
            itemView.findViewById<ImageButton>(R.id.btnDeleteReto).setOnClickListener {
                onDeleteClick(reto)
            }
        }
    }
}

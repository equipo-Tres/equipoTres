package proyecto.picobotella.view.viewholder

import android.view.View
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import proyecto.picobotella.R
import proyecto.picobotella.model.RetoEntity
import proyecto.picobotella.utils.TouchAnimation

class RetoViewHolder(
    itemView: View,
    private val onEditClick: (RetoEntity) -> Unit,
    private val onDeleteClick: (RetoEntity) -> Unit
) : RecyclerView.ViewHolder(itemView) {

    fun bind(reto: RetoEntity) {
        itemView.findViewById<TextView>(R.id.txtRetoDescription).text = reto.description

        val btnEdit = itemView.findViewById<ImageButton>(R.id.btnEditReto)
        val btnDelete = itemView.findViewById<ImageButton>(R.id.btnDeleteReto)

        btnEdit.setOnClickListener { view ->
            playTouchAnimation(view) {
                onEditClick(reto)
            }
        }

        btnDelete.setOnClickListener { view ->
            playTouchAnimation(view) {
                onDeleteClick(reto)
            }
        }
    }

    private fun playTouchAnimation(view: View, onComplete: () -> Unit) {
        TouchAnimation.play(view, onComplete)
    }
}

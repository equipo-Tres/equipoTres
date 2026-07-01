package proyecto.picobotella.ui.retos

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import proyecto.picobotella.R
import proyecto.picobotella.data.local.RetoEntity

class RetoAdapter(
    private val onEditClick: (RetoEntity) -> Unit,
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

    private fun playTouchAnimation(view: View, onComplete: () -> Unit) {
        val animation = AnimationUtils.loadAnimation(view.context, R.anim.touch_animation)
        animation.setAnimationListener(object : android.view.animation.Animation.AnimationListener {
            override fun onAnimationStart(animation: android.view.animation.Animation?) {}
            override fun onAnimationRepeat(animation: android.view.animation.Animation?) {}
            override fun onAnimationEnd(animation: android.view.animation.Animation?) {
                onComplete()
            }
        })
        view.startAnimation(animation)
    }

    inner class RetoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
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
    }
}

package proyecto.picobotella.view

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.content.ContextCompat
import proyecto.picobotella.R

class OutlinedTextView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : AppCompatTextView(context, attrs, defStyleAttr) {

    private val strokeColor = ContextCompat.getColor(context, R.color.black)
    private val strokeWidth = textSize / 8f

    override fun onDraw(canvas: Canvas) {
        val fillColor = currentTextColor
        val textPaint = paint

        textPaint.style = Paint.Style.STROKE
        textPaint.strokeWidth = strokeWidth
        setTextColor(strokeColor)
        super.onDraw(canvas)

        textPaint.style = Paint.Style.FILL
        setTextColor(fillColor)
        super.onDraw(canvas)
    }
}

package ru.skillbranch.devintensive.ui.custom

import android.content.Context
import android.content.res.Resources
import android.graphics.*
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import androidx.annotation.ColorRes
import androidx.annotation.Dimension
import androidx.appcompat.widget.AppCompatImageView
import androidx.core.content.ContextCompat
import ru.skillbranch.devintensive.R
import ru.skillbranch.devintensive.utils.Utils.getThemeAccentColor
import java.lang.Integer.min

class CircleImageView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) :
    AppCompatImageView(context, attrs, defStyleAttr) {
    companion object {
        private const val DEFAULT_BORDER_WITH = 2f
        private const val DEFAULT_BORDER_COLOR = Color.WHITE
    }

    private var borderWidth = DEFAULT_BORDER_WITH.dp
    private var borderColor = DEFAULT_BORDER_COLOR

    private var civImage: Bitmap? = null

    private val paint: Paint = Paint().apply { isAntiAlias = true }
    private val paintBorder: Paint = Paint().apply { isAntiAlias = true }

    private var circleCenter = 0f
    private var heightCircle = 0

    fun Int.toDp() : Int = (this / Resources.getSystem().displayMetrics.density + 0.5f).toInt()

    val Float.dp
        get() = this * Resources.getSystem().displayMetrics.density

    init {
        if (attrs != null) {
            val a = context.obtainStyledAttributes(attrs, R.styleable.CircleImageView)
            borderWidth =
                a.getDimension(R.styleable.CircleImageView_cv_borderWidth, DEFAULT_BORDER_WITH)
            borderColor =
                a.getColor(R.styleable.CircleImageView_cv_borderColor, DEFAULT_BORDER_COLOR)
            a.recycle()
        }
    }

    @Dimension
    fun getBorderWidth(): Int = borderWidth.toInt().toDp()
    fun setBorderWidth(@Dimension dp: Int) {
        borderWidth = dp.toFloat().dp
        invalidate()
    }

    fun getBorderColor(): Int = borderColor
    fun setBorderColor(hex: String) {
        borderColor = Color.parseColor(hex)
        invalidate()
    }

    override fun onDraw(canvas: Canvas) {
        civImage = drawableToBitmap(drawable)
        updateShader()

        if (civImage == null) return
        val circleCenterWithBorder = circleCenter + borderWidth

        canvas.drawCircle(circleCenterWithBorder, circleCenterWithBorder, circleCenterWithBorder, paintBorder)
        canvas.drawCircle(circleCenterWithBorder, circleCenterWithBorder, circleCenter, paint)
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        update()
    }

    fun drawInitialsAvatar(initials: String, textSize: Float = 48f, textColor: Int = Color.WHITE) : Bitmap {
        val paint = Paint().apply {
            isAntiAlias = true
            this.textSize = textSize.dp
            color = textColor
            textAlign = Paint.Align.CENTER
        }

        val textBounds = Rect()
        paint.getTextBounds(initials, 0, initials.length, textBounds)

        val backgroundBounds = RectF()
        backgroundBounds.set(0f, 0f, layoutParams.width.toFloat(), layoutParams.height.toFloat())
        val textBottom = backgroundBounds.centerY() - textBounds.exactCenterY()

        val image = Bitmap.createBitmap(layoutParams.width, layoutParams.height, Bitmap.Config.ARGB_8888)
        image.eraseColor(getThemeAccentColor(context))
        val canvas = Canvas(image)
        canvas.drawText(initials, backgroundBounds.centerX(), textBottom , paint)

        return image
    }

    private fun update() {
        if (civImage != null)
            updateShader()

        val usableWidth = width - (paddingLeft + paddingRight)
        val usableHeight = height - (paddingTop + paddingBottom)

        heightCircle = Math.min(usableWidth, usableHeight)
        circleCenter = (heightCircle - borderWidth * 2) / 2
        paintBorder.color = borderColor
        invalidate()
    }

    private fun updateShader() {
        civImage?.also {
            val shader = BitmapShader(it, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP)

            val scale = if (it.width * height > width * it.height) height / it.height.toFloat() else width / it.width.toFloat()
            val dx = (width - it.width * scale) * 0.5f
            val dy = (height - it.height * scale) * 0.5f

            shader.setLocalMatrix(Matrix().apply {
                setScale(scale, scale)
                postTranslate(dx, dy)
            })
            paint.shader = shader
        }
    }

    private fun drawableToBitmap(drawable: Drawable?): Bitmap? = when (drawable) {
        null -> null
        is BitmapDrawable -> drawable.bitmap
        else -> {
            val bitmap = Bitmap.createBitmap(drawable.intrinsicWidth, drawable.intrinsicHeight, Bitmap.Config.ARGB_8888)
            val canvas = Canvas(bitmap)
            drawable.setBounds(0, 0, canvas.width, canvas.height)
            drawable.draw(canvas)
            bitmap
        }
    }
}
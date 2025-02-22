import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ShapeDrawable
import android.graphics.drawable.shapes.OvalShape
import android.os.Build
import android.view.View
import android.view.animation.AccelerateDecelerateInterpolator
import android.view.animation.AccelerateInterpolator
import android.view.animation.Animation
import android.view.animation.AnimationSet
import android.view.animation.AnimationUtils
import android.view.animation.DecelerateInterpolator
import android.view.animation.ScaleAnimation
import android.view.animation.TranslateAnimation
import android.widget.Button
import android.widget.FrameLayout
import androidx.annotation.RequiresApi
import com.example.mobilecheckers.models.Checker

@RequiresApi(Build.VERSION_CODES.O)
class CheckerView(context: Context, val checker: Checker) : FrameLayout(context) {
    private var button:Button = Button(context).apply {
        val buttonLayoutParams = FrameLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT)
        layoutParams = buttonLayoutParams

        background = ShapeDrawable(OvalShape()).apply {
            paint.color = if (checker.isWhite) Color.WHITE else Color.BLACK
        }

        text = if (checker.isWhite) "⚪" else "⚫"
        setTextColor(if (checker.isWhite) Color.BLACK else Color.WHITE)
        textAlignment = View.TEXT_ALIGNMENT_CENTER
        setAutoSizeTextTypeWithDefaults(Button.AUTO_SIZE_TEXT_TYPE_UNIFORM)
    }
    init {
        this.button.setOnClickListener {
            println("${checker.row} ${checker.col} ${checker.isWhite}")
        }
        addView(button)
    }
    fun setButtonListener(clickListener: OnClickListener){
        this.button.setOnClickListener(clickListener)
    }

    fun animateMoveSequence(toX: Float, toY: Float, onAnimationEnd: () -> Unit) {
        val moveAnim = TranslateAnimation(0f, toX, 0f, toY).apply {
            duration = 30000
            interpolator = AccelerateDecelerateInterpolator()
            fillAfter = true
        }

        val scaleUp = ScaleAnimation(1f, 1.2f, 1f, 1.2f,
            Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f).apply {
            duration = 15000
            interpolator = AccelerateInterpolator()
        }

        val scaleDown = ScaleAnimation(1.2f, 1f, 1.2f, 1f,
            Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f).apply {
            duration = 15000
            interpolator = DecelerateInterpolator()
        }

        val animationSet = AnimationSet(false).apply {
            addAnimation(scaleUp)
            addAnimation(moveAnim)
            addAnimation(scaleDown)
        }

        animationSet.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationStart(animation: Animation?) {}

            override fun onAnimationEnd(animation: Animation?) {
                onAnimationEnd()
            }

            override fun onAnimationRepeat(animation: Animation?) {}
        })

        this.startAnimation(animationSet)
    }

}

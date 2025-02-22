import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ShapeDrawable
import android.graphics.drawable.shapes.OvalShape
import android.os.Build
import android.view.View
import android.widget.Button
import android.widget.FrameLayout
import androidx.annotation.RequiresApi
import com.example.mobilecheckers.models.Checker

@RequiresApi(Build.VERSION_CODES.O)
class CheckerView(context: Context, checker: Checker) : FrameLayout(context) {
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
}

package mg.studio.weatherappdesign;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;

/**
 *  Description:自定义控件View
 * Created by Dan on 2018/3/15.
 */

public class MyCustomView  extends View{

    private static final String TAG = "MyCustomView";
    private static final boolean DEBUG = false;
    private String titleText = "Hello world";

    private int titleColor = Color.WHITE;
    private int titleBackgroundColor = Color.BLUE;
    private int titleSize = 16;

    private Paint mPaint;
    private Rect mBound;

    public MyCustomView(Context context) {
        this(context, null);
    }

    public MyCustomView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MyCustomView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        final Resources.Theme theme = context.getTheme();
        TypedArray a = theme.obtainStyledAttributes(attrs,
                R.styleable.MyCustomView, defStyleAttr, 0);
        if (null != a) {
            int n = a.getIndexCount();
            for (int i = 0; i < n; i++) {
                int attr = a.getIndex(i);
                switch (attr) {
                    case R.styleable.MyCustomView_titleColor:
                        titleColor = a.getColor(attr, Color.BLACK);
                        break;
                    case R.styleable.MyCustomView_titleSize:
                        titleSize = a.getDimensionPixelSize(attr, titleSize);
                        break;
                    case R.styleable.MyCustomView_titleText:
                        titleText = a.getString(attr);
                        break;
                    case R.styleable.MyCustomView_titleBackgroundColor:
                        titleBackgroundColor = a.getColor(attr, Color.WHITE);
                        break;
                }
            }
            a.recycle();
            init();
        }
    }

    /**
     * 初始化
     */
    private void init() {
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setTextSize(titleSize);
        /**
         * 得到自定义View的titleText内容的宽和高
         */
        mBound = new Rect();
        mPaint.getTextBounds(titleText, 0, titleText.length(), mBound);
    }


    @Override
    public void onDraw(Canvas canvas) {
        mPaint.setColor(titleBackgroundColor);
        canvas.drawCircle(getWidth() / 4f, getWidth() / 4f, getWidth() / 4f, mPaint);
        mPaint.setColor(titleColor);
        canvas.drawText(titleText, getWidth() / 3 - mBound.width() / 3, getHeight() / 3+ mBound.height() / 3, mPaint);
    }
}

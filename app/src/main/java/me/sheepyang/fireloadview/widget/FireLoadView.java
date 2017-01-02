package me.sheepyang.fireloadview.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

import me.sheepyang.fireloadview.R;
import me.sheepyang.fireloadview.utils.PxUtils;

/**
 * Created by SheepYang on 2017/1/2 20:35.
 */

public class FireLoadView extends View {

    private int mFire1Color;
    private int mFire2Color;
    private int mFire3Color;
    private int mFire4Color;
    private int mWoodColor;
    private Context mContext;
    private int mWidth;
    private int mHeight;
    private Paint mWoodPaint;
    private Paint mFirePaint;
    private Bitmap mBitmap;
    private Canvas mCanvas;
    private RectF mWoodRectf;
    private RectF mFireRectf;
    private float mFireX;
    private float mFireY;

    public FireLoadView(Context context) {
        this(context, null);
    }

    public FireLoadView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public FireLoadView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.FireLoadView, defStyleAttr, 0);
        mWoodColor = a.getColor(R.styleable.FireLoadView_wood_color, Color.parseColor("#622f27"));
        mFire1Color = a.getColor(R.styleable.FireLoadView_fire_color, Color.parseColor("#ffde03"));
        mFire2Color = a.getColor(R.styleable.FireLoadView_fire_color, Color.parseColor("#febc03"));
        mFire3Color = a.getColor(R.styleable.FireLoadView_fire_color, Color.parseColor("#fda903"));
        mFire4Color = a.getColor(R.styleable.FireLoadView_fire_color, Color.parseColor("#ff3b02"));
        a.recycle();

        mWoodPaint = new Paint();
        mWoodPaint.setAntiAlias(true);
        mWoodPaint.setStrokeWidth(15);
        mWoodPaint.setColor(mWoodColor);

        mFirePaint = new Paint();
        mFirePaint.setAntiAlias(true);
        mFirePaint.setStrokeWidth(15);
        mFirePaint.setColor(mFire1Color);

        mWoodRectf = new RectF();
        mFireRectf = new RectF();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);

        if (heightMode == MeasureSpec.EXACTLY) {
            mHeight = heightSize;
        } else {
            mHeight = PxUtils.dpToPx(40, mContext);
        }

        if (widthMode == MeasureSpec.EXACTLY) {
            mWidth = widthSize;
        } else {
            mWidth = mHeight;
        }

        mBitmap = Bitmap.createBitmap(mWidth, mHeight, Bitmap.Config.ARGB_8888); //生成一个bitmap
        mCanvas = new Canvas(mBitmap);//将bitmp放在我们自己的画布上，实际上mCanvas.draw的时候 改变的是这个bitmap对象

        float woodWidthStart = mWidth * 0.1f;
        float woodWidthStop = mWidth * 0.9f;
        float woodHeightStart = mHeight * 0.75f;
        float woodHeightStop = mHeight * 0.85f;
        mWoodRectf.left = woodWidthStart;
        mWoodRectf.top = woodHeightStart;
        mWoodRectf.right = woodWidthStop;
        mWoodRectf.bottom = woodHeightStop;

        float fireWidth = mWidth * 0.3f;
        mFireX = mWidth / 2;
        mFireY = mWoodRectf.top - fireWidth / 2;
        mFireRectf.left = mFireX - fireWidth / 2;
        mFireRectf.right = mFireX + fireWidth / 2;
        mFireRectf.bottom = mFireY + fireWidth / 2;
        mFireRectf.top = mFireY - fireWidth / 2;
        setMeasuredDimension(mWidth, mHeight);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        mBitmap.eraseColor(Color.parseColor("#00000000"));

        //绘制火苗
        mCanvas.rotate(45, mFireX, mFireY);
        mCanvas.drawRoundRect(mFireRectf, 20, 20, mFirePaint);
        mCanvas.rotate(-45, mFireX, mFireY);

        mCanvas.drawPoint(mFireX, mFireY, mWoodPaint);

        // 绘制木头
        mCanvas.rotate(15, mWidth / 2, mWoodRectf.top + mWoodRectf.height() / 2);
        mCanvas.drawRoundRect(mWoodRectf, 10, 10, mWoodPaint);
        mCanvas.rotate(-30, mWidth / 2, mWoodRectf.top + mWoodRectf.height() / 2);
        mCanvas.drawRoundRect(mWoodRectf, 10, 10, mWoodPaint);
        mCanvas.rotate(15, mWidth / 2, mWoodRectf.top + mWoodRectf.height() / 2);

        canvas.drawBitmap(mBitmap, 0, 0, null);
        invalidate();
    }
}

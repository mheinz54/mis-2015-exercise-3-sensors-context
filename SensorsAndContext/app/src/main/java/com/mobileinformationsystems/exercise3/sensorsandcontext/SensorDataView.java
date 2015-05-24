package com.mobileinformationsystems.exercise3.sensorsandcontext;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.PathShape;
import android.util.AttributeSet;
import android.view.View;


public class SensorDataView extends View
{
    private final int mXAxisColor = Color.RED;
    private final int mYAxisColor = Color.GREEN;
    private final int mZAxisColor = Color.BLUE;
    private final int mMagnitudeColor = Color.WHITE;

    private ShapeDrawable mDrawableX = new ShapeDrawable();
    private ShapeDrawable mDrawableY = new ShapeDrawable();
    private ShapeDrawable mDrawableZ = new ShapeDrawable();
    private ShapeDrawable mDrawableM = new ShapeDrawable();

    private Path mXAxisPath = new Path();
    private Path mYAxisPath = new Path();
    private Path mZAxisPath = new Path();
    private Path mMagnitudePath = new Path();

    private int mPointCount = 1;

    public SensorDataView(Context context)
    {
        super(context);
        init(null, 0);
    }

    public SensorDataView(Context context, AttributeSet attrs)
    {
        super(context, attrs);
        init(attrs, 0);
    }

    public SensorDataView(Context context, AttributeSet attrs, int defStyle)
    {
        super(context, attrs, defStyle);
        init(attrs, defStyle);
    }

    private void init(AttributeSet attrs, int defStyle)
    {
        int paddingLeft = getPaddingLeft();
        int paddingTop = getPaddingTop();
        int paddingRight = getPaddingRight();
        int paddingBottom = getPaddingBottom();

        int contentWidth = getWidth() - paddingLeft - paddingRight;
        int contentHeight = getHeight() - paddingTop - paddingBottom;

        initDrawable(mDrawableX,mXAxisPath,Color.RED,contentWidth,contentHeight);
        initDrawable(mDrawableY,mYAxisPath,Color.GREEN,contentWidth,contentHeight);
        initDrawable(mDrawableZ,mZAxisPath,Color.BLUE,contentWidth,contentHeight);
        initDrawable(mDrawableM,mMagnitudePath,Color.WHITE,contentWidth,contentHeight);
    }

    private void initDrawable(ShapeDrawable drawable, Path path, int color, int width, int height)
    {
        path.moveTo(0,getHeight()/2);
        drawable.setShape(new PathShape(path,width,height));
        drawable.getPaint().setStyle(Paint.Style.STROKE);
        drawable.getPaint().setColor(color);
        drawable.setBounds(0,0,width,height);
    }

    public void addMeasurements(float xValue, float yValue, float zValue)
    {
        mXAxisPath.lineTo(mPointCount,getHeight()/2 + xValue*10);
        mYAxisPath.lineTo(mPointCount,getHeight()/2 + yValue*10);
        mZAxisPath.lineTo(mPointCount,getHeight()/2 + zValue*10);
        mMagnitudePath.lineTo(mPointCount,getHeight()/2 + (xValue + yValue + zValue) * 10);

        this.invalidate();
        mPointCount++;
    }

    @Override
    protected void onDraw(Canvas canvas)
    {
        mDrawableX.draw(canvas);
        mDrawableY.draw(canvas);
        mDrawableZ.draw(canvas);
        mDrawableM.draw(canvas);

        super.onDraw(canvas);
    }

    @Override
    protected void onSizeChanged (int w, int h, int oldw, int oldh)
    {
        init(null,0);
    }
}

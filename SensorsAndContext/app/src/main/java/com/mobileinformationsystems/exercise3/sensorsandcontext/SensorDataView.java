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
    private ShapeDrawable mDrawableX = new ShapeDrawable();
    private ShapeDrawable mDrawableY = new ShapeDrawable();
    private ShapeDrawable mDrawableZ = new ShapeDrawable();
    private ShapeDrawable mDrawableM = new ShapeDrawable();

    private Path mXAxisPath = new Path();
    private Path mYAxisPath = new Path();
    private Path mZAxisPath = new Path();
    private Path mMagnitudePath = new Path();

    private int mPointCount = 1;
    private int mContentWidth = 0;
    private int mContentHeight = 0;

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

        mContentWidth = getWidth() - paddingLeft - paddingRight;
        mContentHeight = getHeight() - paddingTop - paddingBottom;

        initDrawable(mDrawableX,mXAxisPath,Color.RED,mContentWidth,mContentHeight);
        initDrawable(mDrawableY,mYAxisPath,Color.GREEN,mContentWidth,mContentHeight);
        initDrawable(mDrawableZ,mZAxisPath,Color.BLUE,mContentWidth,mContentHeight);
        initDrawable(mDrawableM,mMagnitudePath,Color.WHITE,mContentWidth,mContentHeight);
    }

    private void initDrawable(ShapeDrawable drawable, Path path, int color, int width, int height)
    {
        path.moveTo(0,mContentHeight/2);
        drawable.setShape(new PathShape(path,width,height));
        drawable.getPaint().setStyle(Paint.Style.STROKE);
        drawable.getPaint().setColor(color);
        drawable.setBounds(0,0,width,height);
    }

    public void addMeasurements(float xValue, float yValue, float zValue)
    {
        if(mPointCount >= mContentWidth)
        {
            mPointCount = 1;
            mXAxisPath.rewind();
            mXAxisPath.moveTo(0,mContentHeight/2);
            mYAxisPath.rewind();
            mXAxisPath.moveTo(0,mContentHeight/2);
            mZAxisPath.rewind();
            mZAxisPath.moveTo(0,mContentHeight/2);
            mMagnitudePath.rewind();
            mMagnitudePath.moveTo(0,mContentHeight/2);
        }

        int mid = mContentHeight / 2;
        mXAxisPath.lineTo(mPointCount,mid + xValue*10);
        mYAxisPath.lineTo(mPointCount,mid + yValue*10);
        mZAxisPath.lineTo(mPointCount,mid + zValue*10);
        mMagnitudePath.lineTo(mPointCount,mid + (xValue + yValue + zValue) * 10);

        mPointCount++;
        this.invalidate();

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

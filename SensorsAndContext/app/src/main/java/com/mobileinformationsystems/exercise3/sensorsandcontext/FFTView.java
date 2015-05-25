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

import java.util.Arrays;

public class FFTView  extends View
{
    private ShapeDrawable mDrawableM = new ShapeDrawable();
    private Path mMagnitudePath = new Path();
    private int mPointCount = 1;
    private int mContentHeight = 0;
    private int mContentWidth = 0;

    public FFTView(Context context)
    {
        super(context);
        init();
    }

    public FFTView(Context context, AttributeSet attrs)
    {
        super(context, attrs);
        init();
    }

    public FFTView(Context context, AttributeSet attrs, int defStyle)
    {
        super(context, attrs, defStyle);
        init();
    }

    private void init()
    {
        int paddingLeft = getPaddingLeft();
        int paddingTop = getPaddingTop();
        int paddingRight = getPaddingRight();
        int paddingBottom = getPaddingBottom();

        mContentWidth = getWidth() - paddingLeft - paddingRight;
        mContentHeight = getHeight() - paddingTop - paddingBottom;

        initDrawable(mDrawableM,mMagnitudePath,Color.WHITE,mContentWidth,mContentHeight);
    }


    private void initDrawable(ShapeDrawable drawable, Path path, int color, int width, int height)
    {
        path.moveTo(0,0);
        drawable.setShape(new PathShape(path,width,height));
        drawable.getPaint().setStyle(Paint.Style.STROKE);
        drawable.getPaint().setColor(color);
        drawable.setBounds(0,0,width,height);
    }
    public void addMeasurements(int fftWindowsSize, double[] signals)
    {
        float magnitudeValue = FFT.getFFTSignalMagnitude(fftWindowsSize, signals);

        if(mPointCount >= mContentWidth)
        {
            mPointCount = 1;
            mMagnitudePath.rewind();
            mMagnitudePath.moveTo(0,0);
        }

       mMagnitudePath.lineTo(mPointCount, magnitudeValue/10);

        mPointCount++;
        this.invalidate();

    }

    @Override
    protected void onDraw(Canvas canvas)
    {
        mDrawableM.draw(canvas);
        super.onDraw(canvas);
    }

    @Override
    protected void onSizeChanged (int w, int h, int oldw, int oldh)
    {
        init();
    }

}

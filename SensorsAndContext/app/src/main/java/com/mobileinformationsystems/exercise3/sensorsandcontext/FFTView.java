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
        path.moveTo(0,mContentHeight/2);
        drawable.setShape(new PathShape(path,width,height));
        drawable.getPaint().setStyle(Paint.Style.STROKE);
        drawable.getPaint().setColor(color);
        drawable.setBounds(0,0,width,height);
    }
    public void addMeasurements(int fftWindowsSize, double[] signals)
    {
        float magnitudeValue = getFFTSignalMagnitude(fftWindowsSize, signals);
        int mid = mContentHeight / 2;

        if(mPointCount >= mContentWidth)
        {
            mPointCount = 1;
            mMagnitudePath.rewind();
            mMagnitudePath.moveTo(0,mid);
        }


       mMagnitudePath.lineTo(mPointCount,mid + magnitudeValue* 10);

        mPointCount++;
        this.invalidate();

    }
    private float getFFTSignalMagnitude(int fftWindowsSize, double[] signals)
    {
        FFT fft = new FFT(fftWindowsSize);
        double[] y= new double[fftWindowsSize];
        Arrays.fill(y, 0);

        fft.fft(signals, y);
        double magnitude= calculateAbsoluteValue(signals, y);
        float magnitudeF = (float) magnitude;
        return magnitudeF;
    }
    private double calculateAbsoluteValue(double[] x, double[] y)
    {
        double absoluteValue = 0;
        for (int i=0; i < x.length; i++)
        {
            double sum = x[i] +y[i];
            absoluteValue += Math.pow(sum, 2);
        }
        return absoluteValue;
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

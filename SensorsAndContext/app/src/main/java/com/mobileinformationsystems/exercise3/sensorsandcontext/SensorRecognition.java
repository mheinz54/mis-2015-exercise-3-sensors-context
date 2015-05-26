package com.mobileinformationsystems.exercise3.sensorsandcontext;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.Queue;


public class SensorRecognition extends Activity implements SensorEventListener
{
    private static final String TAG = "SensorRecognition";
    private final int WALKING_THRESHOLD = 200;
    private final int RUNNING_THRESHOLD = 450;
    private final int SITTING_THRESHOLD = 10;
    private final int TIME_RANGE = 5000; //5 second
    private final int WINDOW_SIZE = 2;

    private double[] mSignals;
    private int mRegisteredSignals = 0;
    private Queue<Float> mFFTValues = new LinkedList<Float>();
    private boolean mBusy = false;

    private SensorManager mSensorManager;
    private Sensor mAccelerometer;
    private Handler mHandler = new Handler();
    private Object mLock = new Object();

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sensor_recognition);

        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        mAccelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        mSignals = new double[WINDOW_SIZE];
        if(mAccelerometer == null)
        {
            // quit app or toast a message
        }
        else
        {
            mHandler.postDelayed(new Runnable()
            {
                @Override
                public void run()
                {
                    //somehow process fft values
                    int values ;
                    synchronized (mLock)
                 //   if(mBusy == false)
                    {
                        mBusy = true;
                        Float mean = 0.0f;
                        int len = mFFTValues.size();
                        while(!mFFTValues.isEmpty())
                        {
                            mean += mFFTValues.remove();
                        }
                        mean /= len;

                        if (mean > RUNNING_THRESHOLD)
                            changeRecognizeText("Running");
                        else if (mean > WALKING_THRESHOLD)
                            changeRecognizeText("Walking");
                        else
                            changeRecognizeText("Sitting");

                        Log.v(TAG, "Mean value = " + mean);
                        mBusy = false;
                    }
                    mHandler.postDelayed(this, TIME_RANGE);
                }
            },TIME_RANGE);
        }
    }

    private void changeRecognizeText(final String text)
    {
        runOnUiThread(new Runnable()
        {
            @Override
            public void run()
            {
                TextView textRecognition = (TextView) findViewById(R.id.textRecognition);
                textRecognition.setText(text);
            }
        });
    }

    @Override
    protected void onResume()
    {
        super.onResume();
        mSensorManager.registerListener(this, mAccelerometer, SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    protected void onPause()
    {
        super.onPause();
        mSensorManager.unregisterListener(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_sensor_visualization, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch (item.getItemId())
        {
            case R.id.Process:
                startActivity(new Intent(this,SensorProcessing.class));
                finish();
                break;
            case R.id.Recognize:
                break;
            case R.id.Visualize:
                startActivity(new Intent(this,SensorVisualization.class));
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onSensorChanged(SensorEvent event)
    {
        addSignals(event.values[0], event.values[1], event.values[2]);
    }

    private void addSignals(Float x, Float y, Float z)
    {
        //build up the fft values
        synchronized (mLock)
     //   if(mBusy == false)
        {
            mBusy = true;
            mSignals[mRegisteredSignals] = x + y + z;
            mRegisteredSignals++;
            if(mRegisteredSignals == WINDOW_SIZE)
            {
                float magnitudeValue = FFT.getFFTSignalMagnitude(WINDOW_SIZE, mSignals);
                mFFTValues.add(magnitudeValue);

                //reset registered signals
                Arrays.fill(mSignals, 0);
                mRegisteredSignals = 0;
            }
            mBusy = false;
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy)
    {

    }
}

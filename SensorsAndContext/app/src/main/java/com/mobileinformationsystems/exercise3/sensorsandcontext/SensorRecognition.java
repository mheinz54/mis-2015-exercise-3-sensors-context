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
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;


public class SensorRecognition extends Activity implements SensorEventListener
{
    private final int WALKING_THRESHOLD = 50;
    private final int RUNNING_THRESHOLD = 150;
    private final int SITTING_THRESHOLD = 10;
    private final int TIME_RANGE = 1000; //1 second

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
                    {
                        values = 10;

                        if (values > RUNNING_THRESHOLD)
                            changeRecognizeText("Running");
                        else if (values > WALKING_THRESHOLD)
                            changeRecognizeText("Walking");
                        else
                            changeRecognizeText("Sitting");
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
        //build up the fft values
        synchronized (mLock)
        {

        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy)
    {

    }
}

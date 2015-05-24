package com.mobileinformationsystems.exercise3.sensorsandcontext;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.SeekBar;


public class SensorVisualization extends Activity implements SensorEventListener
{
    private SensorManager mSensorManager;
    private Sensor mAccelerometer;
    private SensorDataView mSensorData;
    private SeekBar mSampleSeekBar;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sensor_visualization);

        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        mAccelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        if(mAccelerometer == null)
        {
            // quit app or toast a message
        }

        mSensorData = (SensorDataView)findViewById(R.id.SensorData);
        mSampleSeekBar = (SeekBar)findViewById(R.id.SampleSeekBar);
        mSampleSeekBar.setMax(100);
        mSampleSeekBar.setProgress(50);
        mSampleSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener()
        {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser)
            {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar)
            {
                unregisterSensor();
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar)
            {
                registerSensor(seekBar.getProgress() * 10000);
            }
        });
    }

    private void unregisterSensor()
    {
        mSensorManager.unregisterListener(this);
    }

    private void registerSensor(int microseconds)
    {
        mSensorManager.registerListener(this, mAccelerometer, microseconds);
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
        unregisterSensor();
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
                startActivity(new Intent(this,SensorRecognition.class));
                finish();
                break;
            case R.id.Visualize:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onSensorChanged(SensorEvent event)
    {
        mSensorData.addMeasurements(event.values[0],event.values[1],event.values[2]);
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy)
    {

    }
}

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

import java.util.Arrays;


public class SensorProcessing extends Activity  implements SensorEventListener
{
    private SensorManager mSensorManager;
    private Sensor mAccelerometer;
    private SeekBar mSampleSeekBar;
    private int fftWindowsSize;
    private double[] signals;
    private int registeredSignals = 0;
    private FFTView fftView;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sensor_processing);

        fftView = (FFTView)findViewById(R.id.FFTData);

        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        mAccelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        if(mAccelerometer == null)
        {
            // quit app or toast a message
        }


        mSampleSeekBar = (SeekBar)findViewById(R.id.WindowsSeekBar);

        mSampleSeekBar.setMax(5);
        mSampleSeekBar.setProgress(2);
        fftWindowsSize = 2*2;
        signals = new double[fftWindowsSize];
        registeredSignals = 0;
        mSampleSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                fftWindowsSize = (int) Math.pow(2, seekBar.getProgress());
                signals = new double[fftWindowsSize];
                registeredSignals = 0;
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.menu_sensor_visualization, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch (item.getItemId())
        {
            case R.id.Process:
                break;
            case R.id.Recognize:
                startActivity(new Intent(this,SensorRecognition.class));
                finish();
                break;
            case R.id.Visualize:
                startActivity(new Intent(this,SensorVisualization.class));
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
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
    public void onSensorChanged(SensorEvent event)
    {
        signals[registeredSignals] =event.values[0] + event.values[1] + event.values[2] ;
        registeredSignals++;
        if(registeredSignals ==fftWindowsSize)
        {
            fftView.addMeasurements(fftWindowsSize, signals);

            //reset registered signals
            Arrays.fill(signals, 0);
            registeredSignals = 0;
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
}

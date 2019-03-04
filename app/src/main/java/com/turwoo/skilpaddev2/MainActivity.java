//************************************//
// MainActivity.java                  //
//      by: Josh Arms                 //
// Purpose: Main java class for the   //
//          app                       //
//************************************//

package com.turwoo.skilpaddev2;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity implements SensorEventListener {

    public static MainActivity main;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        main = this;

        View view = new MainMenu(this);
        setContentView(view); //sets the game to start
        //NO ACTION BAR
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
        //SENSOR MANAGEMENT
        this.sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        this.sensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        sensorManager.registerListener(this, sensor , SensorManager.SENSOR_DELAY_FASTEST);
    }

    //SENSOR MANAGEMENT
    public static double dir; //direction and multiple to move the turtle
    private SensorManager sensorManager; //sensor manager
    private Sensor sensor; //sensor var
    private final float dX = 0f; //default X: this can be changed in the future to allow people to play at weird angles

    //Function: onAccuracyChanged(Sensor, int)
    //Purpose: Handles when the accuracy of a sensor changes
    //Parameters: Sensor sensor: the sensor that changed
    //          int accuracy: the amount the sensor's accuracy changed
    //Returns: none
    public void onAccuracyChanged(Sensor sensor, int accuracy) { }
    //Function: onResume()
    //Purpose: handles when the users returns to the app
    //Parameters: none
    //Returns: none
    protected void onResume() {
        super.onResume();
        sensorManager.registerListener(this, sensor, SensorManager.SENSOR_DELAY_FASTEST);
    }
    //Function: onPause()
    //Purpose: Handles when the user pauses the app (i.e. exits the app)
    //Parameters: none
    //Returns: none
    protected void onPause() {
        super.onPause();
        sensorManager.unregisterListener(this);
    }
    //Function: onSensorChanged(SensorEvent)
    //Purpose: to detect the tilt of the phone
    //Parameters: SensorEvent event: tilt event
    //Returns: none
    public void onSensorChanged(SensorEvent event){
        float x = (Math.round(event.values[0]));
        //float y = (Math.round(event.values[1]));
        //float z = (Math.round(event.values[2]));
        if(x<dX-1.25){
            dir = 1.25;//"fast right";
        }else if(x>dX+1.25) {
            dir = -1.25;//"fast left";
        }else if(x<dX-.95){
            dir = 1;//"right";
        }else if(x>dX+.95){
            dir = -1;//"left";
        }else{
            dir = 0;//"still";
        }
    }
}

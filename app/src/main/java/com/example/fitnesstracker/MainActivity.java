package com.example.fitnesstracker;

import androidx.appcompat.app.AppCompatActivity;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.mikhaellopez.circularprogressbar.CircularProgressBar;


public class MainActivity extends AppCompatActivity implements SensorEventListener {
private SensorManager sensorManager;
private Sensor stepCounterSensor;

private CircularProgressBar stepsProgress;
private TextView stepsShow;
private EditText et_goal;
private Button btn_setGoal;

private int currentSteps=0,goal_steps=1;
private Boolean stepCounterIsAvailable=false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);

        sensorManager=(SensorManager) getSystemService(SENSOR_SERVICE);
        stepCounterSensor=sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER);

        if(stepCounterSensor!=null) {
            Log.d("sen","Sensor available");
            stepCounterIsAvailable=true;
            stepsProgress = findViewById(R.id.stepsProgress);
            stepsShow = findViewById(R.id.stepsShow);
            et_goal = findViewById(R.id.et_goal);
            btn_setGoal = findViewById(R.id.btn_setGoal);


            btn_setGoal.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    goal_steps = Integer.parseInt(et_goal.getText().toString());
                    Toast.makeText(getApplicationContext(), "Goal setted", Toast.LENGTH_SHORT).show();
                    updateStepsProgress();
                }
            });


        }else
            Toast.makeText(getApplicationContext(),"No Step counter detected",Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onSensorChanged(SensorEvent event) {
      if(event.sensor==stepCounterSensor){

          Log.d("step","user moved !");
          currentSteps= (int) event.values[0];
          stepsShow.setText(String.valueOf(currentSteps));

          updateStepsProgress();

      }
    }


    @Override
    protected void onPause() {
        super.onPause();
        if(stepCounterIsAvailable)
        sensorManager.unregisterListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(stepCounterIsAvailable)
        sensorManager.registerListener(this,stepCounterSensor,SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    private void updateStepsProgress(){
        if(!((goal_steps==0 && currentSteps==0) || currentSteps==goal_steps)){
            stepsProgress.setProgressMax((float) goal_steps);
            stepsProgress.setProgress((float) currentSteps);
        }
    }
}
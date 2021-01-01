package com.example.fitnesstracker;

import android.content.SharedPreferences;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.mikhaellopez.circularprogressbar.CircularProgressBar;

import static android.content.Context.MODE_PRIVATE;
import static android.content.Context.SENSOR_SERVICE;


public class OverviewFrag extends Fragment implements SensorEventListener {
    private View view;

    //SENSOR OBJECTS
    private SensorManager sensorManager;
    private Sensor stepCounterSensor;

    //COMPONENT OBJECTS
    private CircularProgressBar stepsProgress;
    private TextView stepsShow,calorieShow,stepsLeftShow;
    private EditText et_goal;
    private Button btn_setGoal;

    //DATATYPE VARIABLES
    private int currentSteps
            ,goal_steps
            ,stepsLeft
            ,currentCal;
    private float CAL_PER_STEP=0.045f;
    private Boolean stepCounterIsAvailable=false;


    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    public OverviewFrag() {
        // Required empty public constructor
    }


    public static OverviewFrag newInstance(String param1, String param2) {
        OverviewFrag fragment = new OverviewFrag();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        getGoalFromSharedPref();

        view=inflater.inflate(R.layout.fragment_overview, container, false);
        AppCompatActivity activity = (AppCompatActivity) view.getContext();

        sensorManager=(SensorManager) activity.getSystemService(SENSOR_SERVICE);
        stepCounterSensor=sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER);


        if(stepCounterSensor!=null) {
            Log.d("sen","Sensor available");
            initializeVariablesOnSuccess();
            btn_setGoal.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    goal_steps = Integer.parseInt(et_goal.getText().toString());
                    saveGoalToSharedPref();
                    Toast.makeText(getContext(), "Goal setted", Toast.LENGTH_SHORT).show();
                    updateStepsProgress();
                }
            });


        }else
            Toast.makeText(getContext(),"No Step counter detected",Toast.LENGTH_SHORT).show();




        return view;
    }



    //SENSOR METHODS->
    @Override
    public void onSensorChanged(SensorEvent event) {
        if(event.sensor==stepCounterSensor){

            Log.d("step","user moved !");
            currentSteps= (int) event.values[0];
            currentCal=(int)(currentSteps*CAL_PER_STEP);

            stepsShow.setText(String.valueOf(currentSteps));
            calorieShow.setText(currentCal+" cal");

            updateStepsProgress();

        }
    }
    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
//DO NOTHING
    }

    //ACTIVITY LIFECYCLE METHODS->
    @Override
    public void onPause() {
        super.onPause();
        if(stepCounterIsAvailable)
            sensorManager.unregisterListener(this);
    }
    @Override
    public void onResume() {
        super.onResume();
        if(stepCounterIsAvailable)
            sensorManager.registerListener(this,stepCounterSensor,SensorManager.SENSOR_DELAY_NORMAL);
    }



    //USER DEFINED METHODS->
    private void initializeVariablesOnSuccess(){
        stepCounterIsAvailable=true;

        stepsProgress = view.findViewById(R.id.stepsProgress);
        stepsShow = view.findViewById(R.id.stepsShow);
        stepsLeftShow = view.findViewById(R.id.stepsLeft);

        calorieShow = view.findViewById(R.id.calorie_indicator);
        et_goal = view.findViewById(R.id.et_goal);
        btn_setGoal = view.findViewById(R.id.btn_setGoal);
    }
    private void updateStepsProgress(){
        try {
            if(currentSteps!=goal_steps){
                stepsProgress.setProgressMax((float) goal_steps);
                stepsProgress.setProgress((float) currentSteps);

                stepsLeft=goal_steps-currentSteps;
                stepsLeftShow.setText(stepsLeft+" steps are left to move");
            }
        }
        catch (ArithmeticException e){
            Toast.makeText(getContext(),"An arithmetic error has occurred",Toast.LENGTH_SHORT).show();
        }

    }
    private void saveGoalToSharedPref(){

        SharedPreferences goal_saved =getActivity().getSharedPreferences("SavedData",MODE_PRIVATE);
        SharedPreferences.Editor editor=goal_saved.edit();
        editor.putInt("Goal",goal_steps);
        editor.apply();
        Log.d("Stay Fit","Saved Goal to sharedPreferences");
    }
    private void getGoalFromSharedPref(){
        SharedPreferences sharedPreferences=getActivity().getSharedPreferences("SavedData",MODE_PRIVATE);
        goal_steps=sharedPreferences.getInt("Goal",0);
        Log.d("Stay Fit","Got the Goal from sharedPreferences");

    }
}
package com.example.alarmmanager;

import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.provider.AlarmClock;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.Calendar;


public class AlarmFragment extends Fragment {

    TextView hoursTV,minutesTV;
    Button setAlarmBtn;
    TimePickerDialog timePickerDialog;
    Calendar calendar;
    int currentHour;
    int currrentMinute;
    EditText timeNext,NumAlarms;
    LinearLayout setTime;
    ArrayList<TimeModal> timeList = new ArrayList<>();
    Dialog dialog;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_alarm,container,false);
        hoursTV = v.findViewById(R.id.textView);
        minutesTV = v.findViewById(R.id.textView2);
        setTime = v.findViewById(R.id.timePicker);
        setAlarmBtn = v.findViewById(R.id.setAlarmBtn);
        timeNext = v.findViewById(R.id.nextTime);
        NumAlarms = v.findViewById(R.id.count);
        dialog = new Dialog(getActivity());
        setTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calendar = Calendar.getInstance();
                currentHour = calendar.get(Calendar.HOUR_OF_DAY);
                currrentMinute = calendar.get(Calendar.MINUTE);

                timePickerDialog = new TimePickerDialog(getActivity(), new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        hoursTV.setText(String.format("%02d",hourOfDay));
                        minutesTV.setText(String.format("%02d",minute));

                    }
                },currentHour,currrentMinute,false);
                timePickerDialog.show();
            }

        });
        setAlarmBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                timeList.clear();
                if(hoursTV.getText().toString().isEmpty() && minutesTV.getText().toString().isEmpty()){
                    Toast.makeText(getActivity(),"Please select time to set alarm",Toast.LENGTH_LONG).show();
                    return;
                }
                if(timeNext.getText().toString().isEmpty() && !NumAlarms.getText().toString().isEmpty()){
                    Snackbar.make(v,"Enter time gap between alarms",Snackbar.LENGTH_SHORT)
                            .setAction("OK", new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                }
                            })
                            .show();
                    return;
                }
                if(!timeNext.getText().toString().isEmpty() && NumAlarms.getText().toString().isEmpty()){
                    Snackbar.make(v,"Enter number of alarms",Snackbar.LENGTH_SHORT)
                            .setAction("OK", new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                }
                            })
                            .show();
                    return;
                }
                if(timeNext.getText().toString().isEmpty() && NumAlarms.getText().toString().isEmpty()){
                    Snackbar.make(v,"Enter number of alarms and time gap between alarms",Snackbar.LENGTH_SHORT)
                            .setAction("OK", new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                }
                            })
                            .show();
                    return;
                }
                int c = Integer.parseInt(NumAlarms.getText().toString());
                int increment = Integer.parseInt(timeNext.getText().toString());
                int currHours = Integer.parseInt(hoursTV.getText().toString());
                int currMin = Integer.parseInt(minutesTV.getText().toString());
                
                while(c!=0){
                    //handle next hours conditions
                    if(currMin >= 60){
                        currMin = currMin - 60;
                        currHours++;
                    }
                    if(currHours >= 24){
                        currHours = currHours - 24;
                    }
                    timeList.add(new TimeModal(currMin,currHours));
                    currMin = currMin + increment;
                    c--;
                }
                int i=0;

//                Intent intent = new Intent(AlarmClock.ACTION_SET_ALARM);
//                intent.putExtra(AlarmClock.EXTRA_HOUR,10);
//                intent.putExtra(AlarmClock.EXTRA_MINUTES,20);
//                intent.putExtra(AlarmClock.EXTRA_SKIP_UI,true);
//                if(intent.resolveActivity(getActivity().getPackageManager()) != null){
//                    startActivity(intent);
//                }
//                Intent intent2 = new Intent(AlarmClock.ACTION_SET_ALARM);
//                intent.putExtra(AlarmClock.EXTRA_HOUR,10);
//                intent.putExtra(AlarmClock.EXTRA_MINUTES,40);
//                intent.putExtra(AlarmClock.EXTRA_SKIP_UI,true);
//                if(intent.resolveActivity(getActivity().getPackageManager()) != null){
//                    startActivity(intent2);
//                }
                while(i<timeList.size()){
                    TimeModal pos = timeList.get(i);
                    if(!hoursTV.getText().toString().isEmpty() && !minutesTV.getText().toString().isEmpty()){
                        Intent intent = new Intent(AlarmClock.ACTION_SET_ALARM);
                        intent.putExtra(AlarmClock.EXTRA_HOUR,pos.getmHours());
                        intent.putExtra(AlarmClock.EXTRA_MINUTES,pos.getmMinutes());
                        intent.putExtra(AlarmClock.EXTRA_SKIP_UI,true);
                        if(intent.resolveActivity(getActivity().getPackageManager()) != null){
                            startActivity(intent);
                            //openDialog();
                            Toast.makeText(getActivity(), String.valueOf(i), Toast.LENGTH_SHORT).show();
                        }else{
                            Toast.makeText(getActivity(), "There is no alarm app for the action", Toast.LENGTH_SHORT).show();
                        }
                    }else{
                        Toast.makeText(getActivity(), "Please choose a time", Toast.LENGTH_SHORT).show();
                    }
                    i++;
                }
            }
        });

        if(savedInstanceState != null) {
            String h = savedInstanceState.getString("hoursValue");
            String m = savedInstanceState.getString("MinutesValue");
            hoursTV.setText(h);
            minutesTV.setText(m);
        }

        return v;
    }

    private void openDialog() {
        dialog.setContentView(R.layout.alarmset_layout);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        Button close = dialog.findViewById(R.id.button);
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }

}
package com.example.alarmmanager;

public class TimeModal {

    private int mHours;
    private int mMinutes;

    public TimeModal(int minutes,int hours) {
        this.mHours = hours;
        this.mMinutes = minutes;
    }

    public int getmHours() {
        return mHours;
    }

    public void setmHours(int mHours) {
        this.mHours = mHours;
    }

    public int getmMinutes() {
        return mMinutes;
    }

    public void setmMinutes(int mMinutes) {
        this.mMinutes = mMinutes;
    }
}

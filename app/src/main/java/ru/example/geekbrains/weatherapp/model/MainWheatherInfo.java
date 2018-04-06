package ru.example.geekbrains.weatherapp.model;


import com.google.gson.annotations.SerializedName;


public class MainWheatherInfo {

    @SerializedName("temp")
    public Double tempBig;

    public long pressure;
    public long humidity;
    public long temp_min;
    public long temp_max;
}

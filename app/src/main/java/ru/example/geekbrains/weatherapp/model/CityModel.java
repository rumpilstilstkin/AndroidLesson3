package ru.example.geekbrains.weatherapp.model;


import java.util.ArrayList;
import java.util.List;


public class CityModel {

    public CoordModel coord;
    public List<WeatherModel> weather = new ArrayList<WeatherModel>();
    public String base;
    public Long id;
    public String name;
    public int cod;
    public MainWheatherInfo main;
    public SystemModel sys;
    public long dt;
}

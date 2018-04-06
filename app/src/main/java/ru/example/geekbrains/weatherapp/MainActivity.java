package ru.example.geekbrains.weatherapp;


import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DateFormat;
import java.util.Date;
import java.util.Locale;

import ru.example.geekbrains.weatherapp.model.CityModel;
import ru.example.geekbrains.weatherapp.ui.AddCityDialog;
import ru.example.geekbrains.weatherapp.ui.dialogs.AddCityDialogListener;

///////////////////////////////////////////////////////////////////////////
// MainActivity
///////////////////////////////////////////////////////////////////////////

public class MainActivity extends AppCompatActivity implements AddCityDialogListener {

    //Классовые переменные
    private static final String LOG_TAG = MainActivity.class.getSimpleName();
    private static final String FONT_FILENAME = "fonts/weather.ttf";
    private static final String POSITIVE_BUTTON_TEXT = "Go";

    // Handler - это класс, позволяющий отправлять и обрабатывать сообщения и объекты runnable. Он используется в двух
    // случаях - когда нужно применить объект runnable когда-то в будущем, и, когда необходимо передать другому потоку
    // выполнение какого-то метода. Второй случай наш.
    private final Handler handler = new Handler();

    //Реализация иконок погоды через шрифт (но можно и через картинки)
    private Typeface weatherFont;
    private TextView cityTextView;
    private TextView updatedTextView;
    private TextView detailsTextView;
    private TextView currentTemperatureTextView;
    private TextView weatherIcon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        cityTextView = (TextView) findViewById(R.id.city_field);
        updatedTextView = (TextView) findViewById(R.id.updated_field);
        detailsTextView = (TextView) findViewById(R.id.details_field);
        currentTemperatureTextView = (TextView) findViewById(R.id.current_temperature_field);
        weatherIcon = (TextView) findViewById(R.id.weather_icon);

        weatherFont = Typeface.createFromAsset(getAssets(), FONT_FILENAME);
        weatherIcon.setTypeface(weatherFont);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.weather, menu);
        return true;
    }

    //Ловим нажатие кнопки меню
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.change_city) {
            showInputDialog();
            return true;
        }
        return false;
    }

    //Показываем диалоговое окно с выбором города
    private void showInputDialog() {
        new AddCityDialog().show(getSupportFragmentManager(), "branch_filter_mode_dialog");
    }


    //Обновление/загрузка погодных данных
    private void updateWeatherData(final String city) {
        new Thread() {//Отдельный поток для получения новых данных в фоне
            public void run() {
                final CityModel model = WeatherDataLoader.getWeatherByCity(getApplicationContext(), city);
                // Вызов методов напрямую может вызвать runtime error
                // Мы не можем напрямую обновить UI, поэтому используем handler, чтобы обновить интерфейс в главном потоке.
                if (model == null) {
                    handler.post(new Runnable() {
                        public void run() {
                            Toast.makeText(getApplicationContext(), getString(R.string.place_not_found),
                                    Toast.LENGTH_LONG).show();
                        }
                    });
                } else {
                    handler.post(new Runnable() {
                        public void run() {
                            renderWeather(model);
                        }
                    });
                }
            }
        }.start();
    }

    //Обработка загруженных данных и обновление UI
    private void renderWeather(CityModel model) {
        try {
            cityTextView.setText(model.name.toUpperCase(Locale.US) + ", " + model.sys.country);

            String description = "";
            long id = 0;

            if(model.weather.size() != 0){
                description = model.weather.get(0).description.toUpperCase(Locale.US);
                id =model.weather.get(0).id;
            }
            detailsTextView.setText(description + "\n" + "Humidity: "
                                    + model.main.humidity + "%" + "\n" + "Pressure: " + model.main.pressure + " hPa");

            currentTemperatureTextView.setText(String.format("%.2f", model.main.tempBig) + " ℃");

            DateFormat df = DateFormat.getDateTimeInstance();
            String updatedOn = df.format(new Date(model.dt * 1000));
            updatedTextView.setText("Last update: " + updatedOn);

            setWeatherIcon(id, model.sys.sunrise * 1000,
                    model.sys.sunset * 1000);

        } catch (Exception e) {
            Log.d(LOG_TAG, "One or more fields not found in the JSON data");//FIXME Обработка ошибки
        }
    }

    // Подстановка нужной иконки
    // Парсим коды http://openweathermap.org/weather-conditions
    private void setWeatherIcon(long actualId, long sunrise, long sunset) {
        long id = actualId / 100; // Упрощение кодов (int оставляет только целочисленное значение)
        String icon = "";
        if (actualId == 800) {
            long currentTime = new Date().getTime();
            if (currentTime >= sunrise && currentTime < sunset) {
                icon = getString(R.string.weather_sunny);
            } else {
                icon = getString(R.string.weather_clear_night);
            }
        } else {
            Log.d(LOG_TAG, "id " + id);
            switch ((int)id) {
                case 2:
                    icon = getString(R.string.weather_thunder);
                    break;
                case 3:
                    icon = getString(R.string.weather_drizzle);
                    break;
                case 5:
                    icon = getString(R.string.weather_rainy);
                    break;
                case 6:
                    icon = getString(R.string.weather_snowy);
                    break;
                case 7:
                    icon = getString(R.string.weather_foggy);
                    break;
                case 8:
                    icon = getString(R.string.weather_cloudy);
                    break;
                // Можете доработать приложение, найдя все иконки и распарсив все значения
                default:
                    break;
            }
        }
        weatherIcon.setText(icon);
    }

    //Метод для доступа кнопки меню к данным
    @Override
    public void onChangeCity(String city) {
        updateWeatherData(city);
    }
}
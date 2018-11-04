package ru.example.geekbrains.weatherapp.ui;


import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import ru.example.geekbrains.weatherapp.R;


public class MainPreferenceActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_pref);

        loadPrefs();
        loadSharedPrefs();

        Button prefButton = (Button) findViewById(R.id.buttonPref);
        prefButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // подключаемся к SharedPreferences в режиме MODE_PRIVATE
                // т.е. к файлу будет доступ только у нашего приложения
                // существуют еще два режима: MODE_WORLD_READABLE и MODE_WORLD_WRITABLE
                // при которых данные будут доступны другим приложениям, которым известен идентификатор файла
                SharedPreferences pref = getPreferences(Context.MODE_PRIVATE);
                // метод getPreferences открывает файл с именем текущего активити
                // в данном случае файл будет называться MainPreferenceActivity.xml

                // сохраняем
                saveToSharedPreferences(pref);
                // загружаем из SharedPreferences в поле R.id.textViewPref
                loadPrefs();
            }
        });
        Button sharedPrefButton = (Button) findViewById(R.id.buttonSharedPref);
        sharedPrefButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // подключаемся к SharedPreferences в режиме MODE_PRIVATE
                // т.е. к файлу будет доступ только у нашего приложения
                // существуют еще два режима: MODE_WORLD_READABLE и MODE_WORLD_WRITABLE
                // при которых данные будут доступны другим приложениям, которым известен идентификатор файла
                SharedPreferences sharedPref = getSharedPreferences("TestPreferences" , Context.MODE_PRIVATE);
                // метод getSharedPreferences подключается к файлу имя которого задается в первом параметре
                // в данном случае файл будет называться TestPreferences.xml

                // сохраняем
                saveToSharedPreferences(sharedPref);

                // загружаем из SharedPreferences в поле R.id.textViewSharedPref
                loadSharedPrefs();
            }
        });
    }

    private void saveToSharedPreferences(SharedPreferences sharedPref) {
        EditText editText = (EditText) findViewById(R.id.editText);

        if((editText != null) && !(editText.getText().toString().isEmpty())) {
            SharedPreferences.Editor editor = sharedPref.edit();
            editor.putString(getString(R.string.shared_prefs_key_text), editText.getText().toString());
            editor.apply();
        }

        editText.setText("");
    }

    private void loadPrefs() {
        SharedPreferences sharedPref = getPreferences(Context.MODE_PRIVATE);
        // загружаем из SharedPreferences значение нашего ключа
        String loadedPrefs = sharedPref.getString(getString(R.string.shared_prefs_key_text),getString(R.string.pref));
        // записываем значение в TextView
        TextView textViewPrefs = (TextView) findViewById(R.id.textViewPref);
        textViewPrefs.setText(loadedPrefs);
    }

    private void loadSharedPrefs() {
        SharedPreferences sharedPref = getSharedPreferences("TestPreferences" , Context.MODE_PRIVATE);
        // загружаем из SharedPreferences значение нашего ключа
        String loadedPrefs = sharedPref.getString(getString(R.string.shared_prefs_key_text),getString(R.string.pref));
        // записываем значение в TextView
        TextView textViewPrefs = (TextView) findViewById(R.id.textViewSharedPref);
        textViewPrefs.setText(loadedPrefs);
    }
}

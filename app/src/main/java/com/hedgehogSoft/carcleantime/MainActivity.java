package com.hedgehogSoft.carcleantime;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.ismaeldivita.chipnavigation.ChipNavigationBar;
import com.rollbar.android.Rollbar;

import java.util.HashSet;
import java.util.Set;

import api_weather.WeatherApiExample;
import api_weather.WeatherHelper;
import logic.NetworksHelper;

import static com.hedgehogSoft.carcleantime.ArchivePage.updateDateRVData;
import static com.hedgehogSoft.carcleantime.HomePage.updateButton;

public class MainActivity extends AppCompatActivity {

    public static final String APP_PREFERENCES = "settings";
    public static SharedPreferences mySettings;
    public static final String TAG = "EzhidzeLog";
    static Boolean relocate;
    private static MainActivity instance;

    public static Rollbar rollbar;
    /**
     * Rollbar - сервис для отслеживания и сбора ошибок приложений на разных устройствах
     * в нашем случае использовался для отладки на чужом, удалённом устройстве
     *
     * @see <a href="https://rollbar.com">https://rollbar.com/</a>
     */

    public ChipNavigationBar chipNavigationBar;
    /**
     * Объект для создания NavigationBar из библиотеки ismaeldivita.chipnavigation
     *
     * @see <a href="https://github.com/ismaeldivita/chip-navigation-bar">https://github.com/ismaeldivita/chip-navigation-bar</a>
     */
    public int previousFragment = R.id.home;
    public static TextView userLocation;

    public static WeatherApiExample weatherData;
    /**
     * Основной объект, содержащий погодные данные, которые приходят с сервера
     *
     * @see <a href="https://openweathermap.org/">https://openweathermap.org/</a>
     */
    public static String dates = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Rollbar.init(this); //Запускаем Rollbar

        //Устанавливаем кастомный ActionBar
        this.getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setDisplayShowCustomEnabled(true);
        getSupportActionBar().setCustomView(R.layout.custom_action_bar);
        View actionBarView = getSupportActionBar().getCustomView();

        userLocation = actionBarView.findViewById(R.id.userLocation);
        rollbar = new Rollbar(MainActivity.this);
        instance = this;
        mySettings = getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);
        chipNavigationBar = findViewById(R.id.bottom_nav);
        chipNavigationBar.setItemSelected(R.id.home, true);

        /*Если коорднаты пользователя ещё не известны, т.е. это первый заупск,
        то открываем экран начальных настроек*/
        if (!mySettings.contains("Location")) {
            Log.d(TAG, "initSetStart");
            Intent intent = new Intent(this, InitSettingsActivity.class);
            intent.putExtra("relocation", false);
            startActivity(intent);
        }

        //Устанавливаем страницу, которая запускается при запуске приложения
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                new HomePage()).commit();

        updateWeatherData(false);
    }

    @Override
    protected void onResume() {
        super.onResume();

        userLocation.setText(mySettings.getString("cityName", "")); //Указываем название города в ActionBar

        //Listener нашего NavigationBar
        chipNavigationBar.setOnItemSelectedListener(new ChipNavigationBar.OnItemSelectedListener() {
            @SuppressLint("NonConstantResourceId")
            @Override
            public void onItemSelected(int i) {
                switch (i) {
                    case R.id.settings:
                        getSupportFragmentManager().beginTransaction()
                                .setCustomAnimations(R.anim.enter_right_to_left, R.anim.exit_left_to_right)
                                .replace(R.id.fragment_container, new SettingsPage()).commit();
                        previousFragment = R.id.settings;
                        break;
                    case R.id.home:
                        if (previousFragment == R.id.settings) {
                            getSupportFragmentManager().beginTransaction()
                                    .setCustomAnimations(R.anim.enter_left_to_right, R.anim.exit_right_to_left)
                                    .replace(R.id.fragment_container, new HomePage()).commit();
                        } else if (previousFragment == R.id.archive) {
                            getSupportFragmentManager().beginTransaction()
                                    .setCustomAnimations(R.anim.enter_right_to_left, R.anim.exit_left_to_right)
                                    .replace(R.id.fragment_container, new HomePage()).commit();
                        } else if (previousFragment == R.id.mapFragment) {
                            getSupportFragmentManager().beginTransaction()
                                    .setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out)
                                    .replace(R.id.fragment_container, new HomePage()).commit();
                        }
                        previousFragment = R.id.home;
                        break;
                    case R.id.archive:
                        getSupportFragmentManager().beginTransaction()
                                .setCustomAnimations(R.anim.enter_left_to_right, R.anim.exit_right_to_left)
                                .replace(R.id.fragment_container, new ArchivePage()).commit();
                        previousFragment = R.id.archive;
                        break;
                }
            }
        });
    }

    //Метод для обновления данных внтури RecycleView
    public static void updateWeatherData(Boolean relocating) {
        WeatherHelper.WeatherResult weatherResult = new WeatherHelper.WeatherResult() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void gotWeather(WeatherApiExample weather) {
                weatherData = weather;
                HomePage.setData(weatherData, relocate);
            }
        };

        relocate = relocating;
        Log.d(TAG, String.valueOf(relocating));

        if (NetworksHelper.isOnline(MainActivity.getInstance()) &&
                mySettings.contains("Location")) {
            String mLocation = mySettings.getString("Location", "");
            WeatherHelper.getWeather(Double.valueOf(mLocation.split(" ")[0]),
                    Double.valueOf(mLocation.split(" ")[1]), weatherResult);
        }
    }

    public void relocate(View v) {
        Log.d(TAG, "initSetStart");
        Intent intent = new Intent(this, InitSettingsActivity.class);
        intent.putExtra("relocation", true);
        startActivity(intent);
    }

    public void openMap(View v) {
        chipNavigationBar.setItemSelected(previousFragment, false);
        getSupportFragmentManager().beginTransaction().
                replace(R.id.fragment_container, new Map()).commit();
        previousFragment = R.id.mapFragment;
    }

    public void updateBut(View v) {
        updateWeatherData(false);
        if (NetworksHelper.isOnline(MainActivity.getInstance())) {
            updateButton.setVisibility(View.INVISIBLE);
            updateButton.setClickable(false);
        }
    }

    public void saveDateBut(View v) {
        /*Если данные о предыдущих походах на мойку имеются,
         то сохранить новую дату поверх, иначе создать ячейку памяти и сохранить туда*/
        Set<String> dates = mySettings.contains("Dates") ?
                mySettings.getStringSet("Dates", null) : new HashSet<>();
        dates.add(weatherData.getList()[0].getDtTxt());
        SharedPreferences.Editor edit =
                mySettings.edit().remove("Dates");
        edit.commit();
        edit.putStringSet("Dates", dates).commit();
        updateDateRVData();
    }

    public static MainActivity getInstance() {
        return instance;
    }
}
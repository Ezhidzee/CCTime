package com.hedgehogSoft.carcleantime;

import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.HashMap;

import api_weather.WeatherApiExample;
import logic.MainAdapter;
import logic.MainModel;
import logic.NetworksHelper;

public class HomePage extends Fragment {
    static RecyclerView days;
    static ArrayList<MainModel> mainModels;
    static MainAdapter mainAdapter;
    static Button updateButton;
    static Integer[] dayIV = new Integer[4], nightIV = new Integer[4],
            eveningIV = new Integer[4], morningIV = new Integer[4];
    static String[] dayPOP = new String[4], nightPOP = new String[4],
            eveningPOP = new String[4], morningPOP = new String[4],
            dayTempTv = new String[4], nightTempTv = new String[4],
            eveningTempTv = new String[4], morningTempTv = new String[4],
            dayDescTv = new String[4], nightDescTv = new String[4],
            eveningDescTv = new String[4], morningDescTv = new String[4],
            dayConDescTv = new String[4], nightConDescTv = new String[4],
            eveningConDescTv = new String[4], morningConDescTv = new String[4];
    static ArrayList<Double> POPs = new ArrayList<>();

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.home, container, false);
        days = view.findViewById(R.id.days);
        mainModels = new ArrayList<>();
        mainAdapter = new MainAdapter(mainModels, MainActivity.getInstance());
        updateButton = (Button) view.findViewById(R.id.updateButton);

        //Запускаем основной метод для заполнения данных
        if (MainActivity.weatherData != null && !InitSettingsActivity.relocation) {
            setData(MainActivity.weatherData, false);
        }

        Log.i(MainActivity.TAG, "onCreateView");

        /*Если при открытии приложения нет подключения к сети,
        то появляется кнопка для обновления данных после включения WI-FI или мобильных данных*/
        if (!(NetworksHelper.isOnline(MainActivity.getInstance()))) {
            updateButton.setClickable(true);
            updateButton.setVisibility(View.VISIBLE);
        }
        return view;
    }

    /**Основной метод для заполнения данными RecycleView, в котором происходит:
     * <ul>
     *     <li>Заполнение погодными данными</li>
     *     <li>Просчёт благоприятности дня для мытья автомобиля</li>
     *     <li>Заполнения адаптера RecycleView</li>
     *   </ul>*/
    @RequiresApi(api = Build.VERSION_CODES.O)
    public static void setData(WeatherApiExample weatherData, Boolean relocating) {
        int day = Integer.parseInt(weatherData.getList()[0].
                getDtTxt().split(" ")[0].split("-")[2]);
        int i = 0, nextDay, counter = 0;
        HashMap<Integer, Integer> months = new HashMap<Integer, Integer>() {{
            put(1, 31);
            put(2, 28);
            put(3, 31);
            put(4, 30);
            put(5, 31);
            put(6, 30);
            put(7, 31);
            put(8, 31);
            put(9, 30);
            put(10, 31);
            put(11, 30);
            put(12, 31);
        }};
        if (day == months.get(Integer.parseInt(weatherData.getList()[i].getDtTxt().
                split(" ")[0].split("-")[1]))) {
            nextDay = 1;
        } else {
            nextDay = day + 1;
        }

        if (relocating) mainModels.clear();

        while (day != nextDay) {
            i++;
            day = Integer.parseInt(weatherData.getList()[i].getDtTxt().
                    split(" ")[0].split("-")[2]);
        }

        for (int k = i; k < weatherData.getList().length - 1; k += 2) {
            POPs.add(average(weatherData.getList()[k].getPop(),
                    weatherData.getList()[k + 1].getPop(), false));
        }

        for (int j = 0; j < 4; j++) {
            while (day != nextDay) {
                i++;
                day = Integer.parseInt(weatherData.getList()[i].getDtTxt().
                        split(" ")[0].split("-")[2]);
            }

            nightTempTv[j] = String.valueOf(average(weatherData.getList()[i].getMain().getTemp(),
                    weatherData.getList()[i + 1].getMain().getTemp(), true));
            nightDescTv[j] = weatherData.getList()[i].getWeather().get(0).getDescription();
            nightPOP[j] = String.valueOf(average(weatherData.getList()[i].getPop(),
                    weatherData.getList()[i + 1].getPop(), false));
            nightConDescTv[j] = POP(j, 0, i, i + 1, weatherData);
            nightIV[j] = setImage(nightConDescTv[j]);

            morningTempTv[j] = String.valueOf(average(weatherData.getList()[i + 2].getMain().getTemp(),
                    weatherData.getList()[i + 3].getMain().getTemp(), true));
            morningDescTv[j] = weatherData.getList()[i + 2].getWeather().get(0).getDescription();
            morningPOP[j] = String.valueOf(average(weatherData.getList()[i + 2].getPop(),
                    weatherData.getList()[i + 3].getPop(), false));
            morningConDescTv[j] = POP(j, 1, i + 2, i + 3, weatherData);
            morningIV[j] = setImage(morningConDescTv[j]);

            dayTempTv[j] = String.valueOf(average(weatherData.getList()[i + 4].getMain().getTemp(),
                    weatherData.getList()[i + 5].getMain().getTemp(), true));
            dayDescTv[j] = weatherData.getList()[i + 4].getWeather().get(0).getDescription();
            dayPOP[j] = String.valueOf(average(weatherData.getList()[i + 4].getPop(),
                    weatherData.getList()[i + 5].getPop(), false));
            dayConDescTv[j] = POP(j, 2, i + 4, i + 5, weatherData);
            dayIV[j] = setImage(dayConDescTv[j]);

            eveningTempTv[j] = String.valueOf(average(weatherData.getList()[i + 6].getMain().getTemp(),
                    weatherData.getList()[i + 7].getMain().getTemp(), true));
            eveningDescTv[j] = weatherData.getList()[i + 6].getWeather().get(0).getDescription();
            eveningPOP[j] = String.valueOf(average(weatherData.getList()[i + 6].getPop(),
                    weatherData.getList()[i + 7].getPop(), false));
            eveningConDescTv[j] = POP(j, 3, i + 6, i + 7, weatherData);
            eveningIV[j] = setImage(eveningConDescTv[j]);

            String[] date = weatherData.getList()[i].getDtTxt().split(" ")[0].split("-");

            mainModels.add(new MainModel(date[2] + "." + date[1],
                    dayIV[j], nightIV[j], eveningIV[j], morningIV[j],
                    "День", "Ночь", "Вечер", "Утро",
                    dayPOP[j], nightPOP[j], eveningPOP[j], morningPOP[j],
                    dayTempTv[j] + "°", nightTempTv[j] + "°",
                    eveningTempTv[j] + "°", morningTempTv[j] + "°",
                    dayDescTv[j], nightDescTv[j], eveningDescTv[j], morningDescTv[j],
                    dayConDescTv[j], nightConDescTv[j], eveningConDescTv[j], morningConDescTv[j]));
            LinearLayoutManager layoutManager = new LinearLayoutManager(
                    MainActivity.getInstance(), LinearLayoutManager.HORIZONTAL, false);
            days.setLayoutManager(layoutManager);
            days.setItemAnimator(new DefaultItemAnimator());
            days.setAdapter(mainAdapter);
            if (nextDay == months.get(Integer.parseInt(weatherData.getList()[i].getDtTxt().
                    split(" ")[0].split("-")[1]))) {
                nextDay = 1;
            } else nextDay++;
        }
    }

    //POP - Probability Of Precipitation
    public static String POP(int j, int k, int i1, int i2, WeatherApiExample weatherData) {
        try {
            if (POPs.get(j * 4 + k) > 0.4 || weatherData.getList()[i1].getRain() != null ||
                    weatherData.getList()[i2].getRain() != null) {
                return "Ожидаются\nосадки в\nэто период";
            } else if (POPs.get(j * 4 + k + 1) > 0.4 || POPs.get(j * 4 + k + 2) > 0.4 ||
                    POPs.get(j * 4 + k + 3) > 0.4 || POPs.get(j * 4 + k + 4) > 0.4 ||
                    weatherData.getList()[i1].getRain() != null ||
                    weatherData.getList()[i2].getRain() != null) {
                return "Ожидаются\nосадки\nв ближайшие\n24 часа";
            } else {
                return "Удачные\nусловия для\nмытья";
            }
        } catch (Exception e) {
            return "-";
        }
    }

    public static int setImage(String line) {
        switch (line) {
            case "Ожидаются\nосадки в\nэто период":
                return R.drawable.red_car;
            case "Ожидаются\nосадки\nв ближайшие\n24 часа":
                return R.drawable.yellow_car;
            case "Удачные\nусловия для\nмытья":
                return R.drawable.green_car;
            default:
                return R.drawable.cross;
        }
    }

    private static double average(double a, double b, boolean isRound) {
        return isRound ? Math.round((a + b) / 2.0) : Math.round(((a + b) / 2.0) * 100.0) / 100.0;
    }
}
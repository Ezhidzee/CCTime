package api_weather;

import android.app.ProgressDialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.util.Log;
import android.widget.Toast;

import com.hedgehogSoft.carcleantime.InitSettingsActivity;
import com.hedgehogSoft.carcleantime.MainActivity;
import com.hedgehogSoft.carcleantime.R;

import org.jetbrains.annotations.NotNull;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class WeatherHelper {
    static final String KEY = MainActivity.getInstance().getString(R.string.WeatherApiKey);
    static final String LANG = "ru";
    static final String UNITS = "metric";

    static WeatherApiExample myData;
    static WeatherResult weatherResult;
    static final ProgressDialog getWeatherDataDialog =
            new ProgressDialog(MainActivity.getInstance(), R.style.CCTimeDialogTheme);

    static final String TAG = "EzhidzeLog";

    static public WeatherApiExample getWeather(Double latitude,
                                               Double longitude,
                                               WeatherResult weatherRes) {
        Log.d(TAG, "Weather start");
        getWeatherDataDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#1b1b1b")));
        getWeatherDataDialog.setMessage("Получение данных о погоде");
        getWeatherDataDialog.show();
        weatherResult = weatherRes;

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://api.openweathermap.org/data/2.5/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        WeatherApi myApi = retrofit.create(WeatherApi.class);
        Call<WeatherApiExample> exampleCall = myApi.getWeather(latitude,
                longitude, LANG, UNITS, KEY);
        exampleCall.enqueue(new Callback<WeatherApiExample>() {

            @Override
            public void onResponse(@NotNull Call<WeatherApiExample> call,
                                   @NotNull Response<WeatherApiExample> response) {
                getWeatherDataDialog.dismiss();

                if (response.code() == 400) {
                    Toast.makeText(MainActivity.getInstance(), "Please Enter a valid city",
                            Toast.LENGTH_LONG).show();
                } else if (!(response.isSuccessful())) {
                    Toast.makeText(MainActivity.getInstance(), String.valueOf(response.code()),
                            Toast.LENGTH_LONG).show();
                }

                try {
                    Log.d(TAG, "onResponse");
                    myData = response.body();
                    weatherResult.gotWeather(myData);
                } catch (NullPointerException e) {
                    Toast.makeText(MainActivity.getInstance(), "No data",
                            Toast.LENGTH_LONG).show();
                    e.printStackTrace();
                    Log.d(TAG, weatherResult.toString());
                }
            }

            @Override
            public void onFailure(@NotNull Call<WeatherApiExample> call, @NotNull Throwable t) {
                Toast.makeText(MainActivity.getInstance(), t.getMessage(),
                        Toast.LENGTH_LONG).show();
            }

        });
        Log.d(TAG, "Weather end");
        return myData;
    }

    public static abstract class WeatherResult {
        public abstract void gotWeather(WeatherApiExample weather);
    }
}

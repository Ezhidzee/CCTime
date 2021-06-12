package api_geocoding;

import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.hedgehogSoft.carcleantime.MainActivity;
import com.hedgehogSoft.carcleantime.R;

import org.jetbrains.annotations.NotNull;

import api_weather.Main;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import java.lang.String;

public class GeocodingHelper extends AsyncTask<String, Integer, String> {
    public Double latitude;
    public Double longitude;
    public final String KEY = MainActivity.getInstance().getString(R.string.GeocodingApiKey);
    public String lang;
    public final String FORMAT = "json";

    static final String TAG = "EzhidzeLog";

    public GeoResult geoRes;

    public GeocodingHelper(Double latitude, Double longitude, String lang, GeoResult geoRes) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.lang = lang;
        this.geoRes = geoRes;
    }

    @Override
    protected String doInBackground(String... strings) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://us1.locationiq.com/v1/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        GeoApi myApi = retrofit.create(GeoApi.class);
        Call<GeocodingApiExample> exampleCall = myApi.getAddress(KEY, this.latitude,
                this.longitude, this.lang, FORMAT);
        exampleCall.enqueue(new Callback<GeocodingApiExample>() {

            @Override
            public void onResponse(@NotNull retrofit2.Call<GeocodingApiExample> call,
                                   @NotNull Response<GeocodingApiExample> response) {
                if (response.code() == 400) {
                    Toast.makeText(MainActivity.getInstance(),
                            "Please Enter a valid city",
                            Toast.LENGTH_LONG).show();
                } else if (!(response.isSuccessful())) {
                    Toast.makeText(MainActivity.getInstance(),
                            String.valueOf(response.code()),
                            Toast.LENGTH_LONG).show();
                }

                try {
                    Log.d(TAG, "onResponse");
                    geoRes.gotCity(response.body().getAddress().getState());
                } catch (NullPointerException e) {
                    Toast.makeText(MainActivity.getInstance(), "No data",
                            Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(@NotNull Call<GeocodingApiExample> call, @NotNull Throwable t) {
                Toast.makeText(MainActivity.getInstance(), t.getMessage(),
                        Toast.LENGTH_LONG).show();
            }

        });
        return null;
    }

    public static abstract class GeoResult {
        public abstract void gotCity(String cityName);
    }
}

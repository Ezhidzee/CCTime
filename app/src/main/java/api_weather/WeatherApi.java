package api_weather;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface WeatherApi {
        @GET("forecast")
        Call<WeatherApiExample> getWeather(@Query("lat") Double lat,
                                           @Query("lon") Double lon,
                                           @Query("lang") String lang,
                                           @Query("units") String units,
                                           @Query("appid") String key);
}

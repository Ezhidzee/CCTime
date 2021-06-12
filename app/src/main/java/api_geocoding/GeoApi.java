package api_geocoding;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface GeoApi {
    @GET("reverse.php")
    Call<GeocodingApiExample> getAddress(@Query("key") String key,
                                         @Query("lat") Double lat,
                                         @Query("lon") Double lon,
                                         @Query("accept-language")  String lang,
                                         @Query("format") String format);
}

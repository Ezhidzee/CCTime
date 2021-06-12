package api_weather;

import com.google.gson.annotations.SerializedName;

public class WeatherApiExample {
    @SerializedName("list")
    api_weather.List[] list;

    public City getCity() {
        return city;
    }

    public void setCity(City city) {
        this.city = city;
    }

    @SerializedName("city")
    City city;

    public api_weather.List[] getList() {
        return list;
    }

    public void setList(api_weather.List[] list) {
        this.list = list;
    }
}

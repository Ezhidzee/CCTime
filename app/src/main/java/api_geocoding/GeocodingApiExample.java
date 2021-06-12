package api_geocoding;

import com.google.gson.annotations.SerializedName;

public class GeocodingApiExample {
    @SerializedName("address")
    Address address;

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }
}

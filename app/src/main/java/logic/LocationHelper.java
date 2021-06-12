package logic;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;

import com.hedgehogSoft.carcleantime.MainActivity;
import com.hedgehogSoft.carcleantime.InitSettingsActivity;
import com.hedgehogSoft.carcleantime.R;

import java.util.Timer;
import java.util.TimerTask;

public class LocationHelper {

    boolean gps_enabled = false;
    boolean network_enabled = false;
    Timer timer1;
    LocationResult locationResult;
    String coordinates;

    final String TAG = "EzhidzeLog";

    protected final ProgressDialog getLocationDialog =
            new ProgressDialog(InitSettingsActivity.getContext(), R.style.CCTimeDialogTheme);
  
    private LocationManager locationManager =
            (LocationManager) MainActivity.getInstance().
                    getSystemService(Context.LOCATION_SERVICE);
    private final LocationListener locationListenerGPS = new LocationListener() {
        @Override
        public void onLocationChanged(@NonNull Location location) {
            timer1.cancel();
            Log.d(TAG, "GPSch");
            MainActivity.rollbar.debug("GPSch");
            if (locationManager != null) {
                locationManager.removeUpdates(locationListenerNETWORK);
                locationManager.removeUpdates(locationListenerGPS);
                locationManager = null;
            }
            String loc = location.getLatitude() + " " + location.getLongitude();
            setCoordinates(loc);
            Log.d(TAG, loc);
            LocationHelper.this.getLocationDialog.dismiss();
            MainActivity.rollbar.debug(loc);
        }

        @Override
        public void onProviderEnabled(@NonNull String provider) {
        }

        @Override
        public void onProviderDisabled(@NonNull String provider) {
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {
        }
    };

    private final LocationListener locationListenerNETWORK = new LocationListener() {
        @Override
        public void onLocationChanged(@NonNull Location location) {
            timer1.cancel();
            Log.d(TAG, "NETWORKch");
            MainActivity.rollbar.debug("NETWORKch");
            if (locationManager != null) {
                locationManager.removeUpdates(locationListenerNETWORK);
                locationManager.removeUpdates(locationListenerGPS);
                locationManager = null;
            }
            String loc = location.getLatitude() + " " + location.getLongitude();
            setCoordinates(loc);
            locationResult.gotLocation(loc);
            LocationHelper.this.getLocationDialog.dismiss();
            Log.d(TAG, loc);
            MainActivity.rollbar.debug(loc);
        }

        @Override
        public void onProviderEnabled(@NonNull String provider) {
        }

        @Override
        public void onProviderDisabled(@NonNull String provider) {
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {
        }
    };

    public LocationHelper() {
    }

    @SuppressLint("MissingPermission")
    public boolean getLocation(LocationResult locationRes) {
        this.getLocationDialog.setMessage("Определение местоположения");
        this.getLocationDialog.show();
        locationResult = locationRes;
        try {
            gps_enabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        } catch (Exception ex) {
            MainActivity.rollbar.warning(ex);
        }

        try {
            network_enabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        } catch (Exception ex) {
            MainActivity.rollbar.warning(ex);
        }

        if (!gps_enabled && !network_enabled) return false;

        if (gps_enabled) {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,
                    0, 0, locationListenerGPS);
        }
        if (network_enabled) {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,
                    0, 0, locationListenerNETWORK);
        }
        timer1 = new Timer();
        Log.d(TAG, "Timer!");
        MainActivity.rollbar.debug("Timer!");
        timer1.schedule(new GetLastLocation(), 20000);

        return true;
    }

    public void setCoordinates(String coordinates) {
        this.coordinates = coordinates;
    }

    public String getCoordinates() {
        return coordinates;
    }

    class GetLastLocation extends TimerTask {
        @SuppressLint("MissingPermission")
        @Override
        public void run() {
            Log.d(TAG, "run");
            MainActivity.rollbar.debug("run");
            locationManager.removeUpdates(locationListenerGPS);
            locationManager.removeUpdates(locationListenerNETWORK);

            Location net_loc = null, gps_loc = null;
            if (gps_enabled)
                gps_loc = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            if (network_enabled)
                net_loc = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);

            if (gps_loc != null && net_loc != null) {
                if (gps_loc.getTime() > net_loc.getTime()) {
                    String loc = gps_loc.getLatitude() + " " + gps_loc.getLongitude();
                    setCoordinates(loc);
                    Log.d(TAG, loc);
                    MainActivity.rollbar.debug(loc);
                    locationResult.gotLocation(loc);
                    LocationHelper.this.getLocationDialog.dismiss();
                } else {
                    String loc = net_loc.getLatitude() + " " + net_loc.getLongitude();
                    setCoordinates(loc);
                    Log.d(TAG, loc);
                    MainActivity.rollbar.debug(loc);
                    locationResult.gotLocation(loc);
                    LocationHelper.this.getLocationDialog.dismiss();
                }
                return;
            }

            if (gps_loc != null) {
                String loc = gps_loc.getLatitude() + " " + gps_loc.getLongitude();
                setCoordinates(loc);
                Log.d(TAG, loc);
                MainActivity.rollbar.debug(loc);
                locationResult.gotLocation(loc);
                LocationHelper.this.getLocationDialog.dismiss();
                return;
            }
            if (net_loc != null) {
                String loc = net_loc.getLatitude() + " " + net_loc.getLongitude();
                setCoordinates(loc);
                Log.d(TAG, loc);
                MainActivity.rollbar.debug(loc);
                locationResult.gotLocation(loc);
                LocationHelper.this.getLocationDialog.dismiss();
                return;
            }
        }
    }

    public static abstract class LocationResult {
        public abstract void gotLocation(String location);
    }
}

package logic;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import androidx.annotation.NonNull;

import com.hedgehogSoft.carcleantime.InitSettingsActivity;
import com.hedgehogSoft.carcleantime.MainActivity;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import api_weather.Main;

public class NetworksHelper {

    public static boolean isOnline(Context context) {
        ConnectivityManager cm =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();

        if (netInfo != null && netInfo.isConnectedOrConnecting()) {
            return true;
        } else {
            onCreateDialog("Ошибка подключения", "Не удалось подключится к сети," +
                    " провертье подключение Wi-Fi или мобильных данных", context);
            return false;
        }
    }

    public static void createLocationRequest(GPSStatus statusRes, Activity activity) {
        LocationRequest locationRequest = LocationRequest.create();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(5000);
        locationRequest.setFastestInterval(2000);

        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                .addLocationRequest(locationRequest);
        builder.setAlwaysShow(true);
        Task<LocationSettingsResponse> task =
                LocationServices.getSettingsClient(activity)
                        .checkLocationSettings(builder.build());
        task.addOnCompleteListener(new OnCompleteListener<LocationSettingsResponse>() {
            @Override
            public void onComplete(@NonNull Task<LocationSettingsResponse> task) {
                try {
                    LocationSettingsResponse response = task.getResult(ApiException.class);
                    Log.i(MainActivity.TAG, "try");
                    statusRes.gotStatus(true);
                } catch (ApiException e) {
                    Log.i(MainActivity.TAG, "catch");
                    switch (e.getStatusCode()) {
                        case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                            try {
                                ResolvableApiException resolvableApiException =
                                        (ResolvableApiException) e;
                                resolvableApiException.
                                        startResolutionForResult(activity, 1);
                                statusRes.gotStatus(false);
                            } catch (IntentSender.SendIntentException sendIntentException) {
                                statusRes.gotStatus(false);
                            }
                            break;
                        case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                            break;
                    }
                }

            }
        });
    }

    public static void onCreateDialog(String title, String message, Context context) {
        AlertDialog.Builder alert = new AlertDialog.Builder(context);
        alert.setTitle(title)
                .setMessage(message)
                .setNeutralButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
        alert.create().show();
    }

    public static abstract class GPSStatus {
        public abstract void gotStatus(Boolean status);
    }
}

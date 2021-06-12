package com.hedgehogSoft.carcleantime;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.rollbar.android.Rollbar;

import java.util.ArrayList;

import api_geocoding.GeocodingHelper;
import logic.DBHelper;
import logic.LocationHelper;
import logic.NetworksHelper;

import static com.hedgehogSoft.carcleantime.MainActivity.mySettings;
import static com.hedgehogSoft.carcleantime.MainActivity.updateWeatherData;

public class InitSettingsActivity extends AppCompatActivity {
    public static Rollbar rollbar;
    final String TAG = "EzhidzeLog";

    public String myLocation;
    LocationHelper location; //Объект класса LocationHekper, предназначенного для автоматического определения местоположения

    DBHelper databaseHelper; //База данных с координатами городов, для рус=чного выбора местопложения
    SQLiteDatabase db;
    Cursor userCursor;

    private static InitSettingsActivity instance;

    EditText countryED;
    Button saveBut;
    Spinner countriesSpinner;
    static Boolean relocation = false;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.initial_settings);
        Rollbar.init(this);
        rollbar = new Rollbar(InitSettingsActivity.this);

        Log.d(TAG, "initSet");
        instance = this;

        countryED = findViewById(R.id.countryED);
        saveBut = findViewById(R.id.saveBut);
        countriesSpinner = findViewById(R.id.countriesSpinner);

        databaseHelper = new DBHelper(getApplicationContext());
        databaseHelper.createDb();

        Bundle arguments = getIntent().getExtras();
        relocation = arguments.getBoolean("relocation");
    }

    @Override
    protected void onResume() {
        super.onResume();
        addCountriesSpinnerData();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        db.close();
        userCursor.close();
        databaseHelper.close();
        userCursor.close();
    }

    public void autoDetectionBut(View v) {
        if (ContextCompat.checkSelfPermission(InitSettingsActivity.this,
                Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_DENIED) {
            ActivityCompat.requestPermissions(InitSettingsActivity.this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        } else {
            getData(relocation);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void saveBut(View v) {
        SharedPreferences.Editor edit =
                mySettings.edit().putString("Location", myLocation);
        edit.apply();
        updateWeatherData(relocation);
        relocation = false;
        db.close();
        userCursor.close();
        databaseHelper.close();
        userCursor.close();
        finish();
    }

    //Метод, в котором происходит автоматическое определение местоположения пользователя
    public void getData(boolean relocation) {
        if (mySettings.getString("Location", "").equals("null")) mySettings.edit().clear().apply();
        Log.d(TAG, "Pressed!");

        try {
            //Обратное геокодирование для опрделения корректного названия города
            GeocodingHelper.GeoResult geoRes = new GeocodingHelper.GeoResult() {
                @Override
                public void gotCity(String cityName) {
                    countryED.setText(cityName);
                    SharedPreferences.Editor edit =
                            mySettings.edit().putString("cityName", cityName);
                    edit.apply();
                    saveBut.setEnabled(true);
                }
            };

            location = new LocationHelper();
            LocationHelper.LocationResult locationResult = new LocationHelper.LocationResult() {
                @Override
                public void gotLocation(String location) {
                    myLocation = location;
                    rollbar.debug("Final data: " + location);
                    GeocodingHelper myGeoCode =
                            new GeocodingHelper(Double.valueOf(myLocation.split(" ")[0]),
                                    Double.valueOf(myLocation.split(" ")[1]),
                                    "en", geoRes);
                    myGeoCode.execute();
                }
            };

            //Если все разрешения получены и сети включены начинаем определение местоположения
            NetworksHelper.GPSStatus GPSStatus = new NetworksHelper.GPSStatus() {
                @Override
                public void gotStatus(Boolean status) {
                    if (status) location.getLocation(locationResult);
                }
            };

            if (mySettings.contains("Location") && !relocation) {
                Log.d(TAG, "Данные есть!");
                MainActivity.rollbar.debug("Данные есть: "
                        + mySettings.getString("Location", ""));
                locationResult.gotLocation(mySettings.getString("Location", ""));
            } else {
                if (!mySettings.contains("APP_PREFERENCE")
                        && NetworksHelper.isOnline(InitSettingsActivity.getContext())) {
                    NetworksHelper.createLocationRequest(GPSStatus, InitSettingsActivity.instance);
                }
            }

        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
            rollbar.warning(e);
            Toast.makeText(InitSettingsActivity.this,
                    "Неизвестная ошибка\nобратитесь к разработчику",
                    Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case 1:
                switch (resultCode) {
                    case Activity.RESULT_OK:
                        getData(false);
                        break;
                    case Activity.RESULT_CANCELED:
                        Log.i(TAG, "onActivityResult: User rejected GPS request");
                        break;
                    default:
                        break;
                }
                break;
        }
    }

    //Метод для заполнения данными списка, предназначенного для ручного выбора страны
    public void addCountriesSpinnerData() {
        db = databaseHelper.open();

        userCursor = db.rawQuery("SELECT * FROM " + DBHelper.TABLE
                + " ORDER BY " + DBHelper.COLUMN_COUNTRY + " ASC", null);
        userCursor.moveToFirst();

        ArrayList<String> countriesNames = new ArrayList<>();
        countriesNames.add("Choose country");
        while (!userCursor.isAfterLast()) {
            if (!countriesNames.contains(
                    userCursor.getString(userCursor.getColumnIndex(DBHelper.COLUMN_COUNTRY)))) {
                countriesNames.add(userCursor.getString(
                        userCursor.getColumnIndex(DBHelper.COLUMN_COUNTRY)));
            }
            userCursor.moveToNext();
        }

        ArrayAdapter<String> countriesSpinnerAdapter = new ArrayAdapter<>(this,
                R.layout.custom_spinner_item, countriesNames);
        countriesSpinnerAdapter.setDropDownViewResource(
                R.layout.custom_spinner_dropdown_item);
        countriesSpinner.setAdapter(countriesSpinnerAdapter);

        countriesSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Log.d(TAG, countriesNames.get(position));
                if (!countriesNames.get(position).equals("Choose country")) {
                    addCitiesSpinnerData(countriesNames, position);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    //Метод для заполнения данными списка, предназначенного для ручного выбора города
    public void addCitiesSpinnerData(ArrayList<String> countriesNames, int position) {
        ArrayList<String> citiesNames = new ArrayList<>();
        citiesNames.add("Choose city");

        userCursor = db.rawQuery("SELECT * FROM " + DBHelper.TABLE
                + " WHERE " + DBHelper.COLUMN_COUNTRY + " = '"
                + countriesNames.get(position) + "'", null);
        userCursor.moveToFirst();
        while (!userCursor.isAfterLast()) {
            citiesNames.add(userCursor.getString(
                    userCursor.getColumnIndex(DBHelper.COLUMN_CITY)));
            userCursor.moveToNext();
        }

        AlertDialog.Builder builder =
                new AlertDialog.Builder(InitSettingsActivity.this);
        builder.setTitle("Cities");
        View customLayout =
                getLayoutInflater().inflate(R.layout.cities_alert_dialog, null);
        builder.setView(customLayout);
        AlertDialog dialog = builder.create();
        dialog.getWindow().
                setBackgroundDrawable(new ColorDrawable(Color.parseColor("#FF292626")));
        dialog.setButton(AlertDialog.BUTTON_POSITIVE, "OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        dialog.show();

        Spinner citiesSpinner = customLayout.findViewById(R.id.citiesSpinner);
        ArrayAdapter<String> citiesSpinnerAdapter =
                new ArrayAdapter<>(InitSettingsActivity.this,
                        R.layout.custom_spinner_item, citiesNames);
        citiesSpinnerAdapter.
                setDropDownViewResource(R.layout.custom_spinner_dropdown_item);
        citiesSpinner.setAdapter(citiesSpinnerAdapter);

        citiesSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (!citiesNames.get(position).equals("Choose city")) {
                    userCursor = db.rawQuery("SELECT * FROM " + DBHelper.TABLE + " WHERE " +
                            DBHelper.COLUMN_CITY + " = '" + citiesNames.get(position) + "'", null);
                    userCursor.moveToFirst();
                    Double lat = Double.valueOf(
                            userCursor.getString(userCursor.getColumnIndex(DBHelper.COLUMN_LAT)));
                    Double lng = Double.valueOf(
                            userCursor.getString(userCursor.getColumnIndex(DBHelper.COLUMN_LNG)));
                    myLocation = lat + " " + lng;

                    String cityName = userCursor.getString(userCursor.getColumnIndex(DBHelper.COLUMN_CITY));
                    countryED.setText(cityName);
                    saveBut.setEnabled(true);
                    SharedPreferences.Editor edit =
                            mySettings.edit().putString("cityName", cityName);
                    edit.apply();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        switch (requestCode) {
            case 1: {
                if (grantResults.length > 0 && grantResults[0]
                        == PackageManager.PERMISSION_GRANTED) {
                    if (ContextCompat.checkSelfPermission(InitSettingsActivity.this,
                            Manifest.permission.ACCESS_FINE_LOCATION) ==
                            PackageManager.PERMISSION_GRANTED) {
                        getData(relocation);
                    }
                } else {
                    NetworksHelper.onCreateDialog("Permission error",
                            "Requires permission to autodetect location", InitSettingsActivity.this);
                }
                return;
            }
        }
    }

    public static Context getContext() {
        return instance;
    }
}

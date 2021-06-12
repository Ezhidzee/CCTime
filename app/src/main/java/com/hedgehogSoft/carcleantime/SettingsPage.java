package com.hedgehogSoft.carcleantime;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import api_weather.Main;

public class SettingsPage extends Fragment {

    EditText userLoactionED;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.settings, container, false);
        userLoactionED = view.findViewById(R.id.userLocationED);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (MainActivity.mySettings.contains("cityName")) {
            userLoactionED.setText(MainActivity.mySettings.getString("cityName", ""));
        }
    }
}
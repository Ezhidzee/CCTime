package com.hedgehogSoft.carcleantime;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import logic.DatesAdapter;

import static com.hedgehogSoft.carcleantime.MainActivity.mySettings;

public class ArchivePage extends Fragment {
    static RecyclerView datesRecyclerView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.archive, container, false);
        EditText dateED = view.findViewById(R.id.dateED);
        datesRecyclerView = view.findViewById(R.id.datesRV);

        //Если данные уже содержатся, то заполнить RecycleView при создании страницы
        if (mySettings.contains("Dates")) {
            DatesAdapter myDateAdapter =
                    new DatesAdapter(new ArrayList(mySettings.getStringSet("Dates", null)),
                            MainActivity.getInstance());
            LinearLayoutManager llm = new LinearLayoutManager(MainActivity.getInstance());
            llm.setOrientation(LinearLayoutManager.VERTICAL);
            datesRecyclerView.setLayoutManager(llm);
            datesRecyclerView.setAdapter(myDateAdapter);
        }
        dateED.setText(MainActivity.weatherData.getList()[0].getDtTxt());
        return view;
    }

    //Метод для обновления данных внутри RecycleView
    public static void updateDateRVData() {
        DatesAdapter myDateAdapter =
                new DatesAdapter(new ArrayList(mySettings.getStringSet("Dates", null)),
                        MainActivity.getInstance());
        LinearLayoutManager llm = new LinearLayoutManager(MainActivity.getInstance());
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        datesRecyclerView.setLayoutManager(llm);
        myDateAdapter.notifyDataSetChanged();
        datesRecyclerView.setAdapter(myDateAdapter);
    }
}
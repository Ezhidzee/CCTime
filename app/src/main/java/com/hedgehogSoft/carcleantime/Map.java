package com.hedgehogSoft.carcleantime;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class Map extends Fragment implements OnMapReadyCallback {
    GoogleMap map;
    View mapView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.map, null, false);

        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().
                findFragmentById(R.id.mapFragment);

        mapFragment.getMapAsync(this);

        mapView = mapFragment.getView();
        mapFragment.getMapAsync(this);

        return view;
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        map = googleMap;
        map.getUiSettings().setAllGesturesEnabled(true);
        map.getUiSettings().setZoomControlsEnabled(true);
        map.getUiSettings().setMyLocationButtonEnabled(true);
        map.moveCamera(CameraUpdateFactory.zoomTo(10.0f));
        if (MainActivity.mySettings.contains("Location")) {
            double[] coords =
                    {Double.parseDouble(MainActivity.mySettings.
                            getString("Location", "").split(" ")[0]),
                            Double.parseDouble(MainActivity.mySettings.
                                    getString("Location", "").split(" ")[1])};
            LatLng place = new LatLng(coords[0], coords[1]);
            map.addMarker(new MarkerOptions().position(place).
                    title(MainActivity.mySettings.getString("cityName", "")));
            map.moveCamera(CameraUpdateFactory.newLatLng(place));
        } else {
            LatLng place = new LatLng(59.937890, 30.307950);
            map.addMarker(new MarkerOptions().position(place).title("Saint-Petersburg"));
            map.moveCamera(CameraUpdateFactory.newLatLng(place));
        }

    }
}

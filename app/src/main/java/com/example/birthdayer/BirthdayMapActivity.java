package com.example.birthdayer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.mapbox.api.geocoding.v5.MapboxGeocoding;
import com.mapbox.api.geocoding.v5.models.CarmenFeature;
import com.mapbox.api.geocoding.v5.models.GeocodingResponse;
import com.mapbox.geojson.Point;
import com.mapbox.mapboxsdk.Mapbox;
import com.mapbox.mapboxsdk.annotations.MarkerOptions;
import com.mapbox.mapboxsdk.camera.CameraPosition;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;
import com.mapbox.mapboxsdk.maps.Style;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BirthdayMapActivity extends AppCompatActivity implements OnMapReadyCallback, LocationListener {

    private MapView mapView;
    private LocationManager locationManager;
    private double latitude;
    private double longitude;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Mapbox.getInstance(this, getString(R.string.mapbox_access_token));
        setContentView(R.layout.activity_birthday_map);

        getLocation();

        mapView = (MapView) findViewById(R.id.mapView);
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(this);
    }

    @SuppressLint("MissingPermission")
    private void getLocation() {
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        Criteria criteria = new Criteria();
        String bestProvider = String.valueOf(locationManager.getBestProvider(criteria, true));

        Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);

        if (location != null) {
            latitude = location.getLatitude();
            longitude = location.getLongitude();
        } else {
            locationManager.requestLocationUpdates(bestProvider, 1000, 0, this);
        }
    }

    @SuppressWarnings("deprecation")
    @Override
    public void onMapReady(@NonNull MapboxMap mapboxMap) {

        new setMarkersTask(mapboxMap, this).execute();

        mapboxMap.addMarker(new MarkerOptions()
                .position(new LatLng(51.441642, 5.4697225))
                .title("Current Location"));

        mapboxMap.setStyle(Style.MAPBOX_STREETS, new Style.OnStyleLoaded() {
            @Override
            public void onStyleLoaded(@NonNull Style style) {
                Toast toast = Toast.makeText(mapView.getContext().getApplicationContext(),
                        "Map styles are ready", Toast.LENGTH_SHORT);
                toast.show();

                mapboxMap.setCameraPosition(new CameraPosition.Builder().target(new LatLng(latitude, longitude)).zoom(15).build());
            }
        });
    }

    @Override
    public void onLocationChanged(Location location) {

        locationManager.removeUpdates(this);

        latitude = location.getLatitude();
        longitude = location.getLongitude();
        mapView.getMapAsync(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        mapView.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mapView.onStop();
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }

    @SuppressWarnings({"deprecation", "StaticFieldLeak"})
    private static class setMarkersTask extends AsyncTask<Void, Void, List<String>>
    {
        private final MapboxMap mapboxMap;
        private final Context context;

        public setMarkersTask(MapboxMap mapboxMap, Context context){
            this.mapboxMap = mapboxMap;
            this.context = context;
        }

        @Override
        protected List<String> doInBackground(Void... params) {
            // -- FOR TESTING --

            BirthdayDatabase birthdayDb = BirthdayDatabase.getInstance(context);

            Birthday birthday = new Birthday("Michelle", "Kennedylaan 2, Veghel");
//            birthdayDb.birthdayDao().emptyTable();
            birthdayDb.birthdayDao().insertBirthday(birthday);

            // -----------------

            List<String> locations = birthdayDb.birthdayDao().getBirthdayLocations();

            for (int i = 0; i < locations.size(); i++) {
                MapboxGeocoding mapboxGeocoding = MapboxGeocoding.builder()
                        .accessToken(context.getString(R.string.mapbox_access_token))
                        .query(locations.get(i))
                        .build();

                mapboxGeocoding.enqueueCall(new Callback<GeocodingResponse>() {

                    @SuppressWarnings("deprecation")
                    @Override
                    public void onResponse(@NonNull Call<GeocodingResponse> call, @NonNull Response<GeocodingResponse> response) {

                        List<CarmenFeature> results = response.body().features();
                        if (results.size() > 0) {
                            Point firstResultPoint = results.get(0).center();
                            Log.i("GEOCODE_RESPONSE", "onResponse: " + firstResultPoint.toString());

                            mapboxMap.addMarker(new MarkerOptions()
                                    .position(new LatLng(firstResultPoint.latitude(), firstResultPoint.longitude()))
                                    .title(String.valueOf(firstResultPoint.latitude()) + ", " + String.valueOf(firstResultPoint.longitude())));
                        } else {
                            Log.i("GEOCODE_RESPONSE", "onResponse: No result found");
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<GeocodingResponse> call, @NonNull Throwable throwable) {
                        throwable.printStackTrace();
                    }
                });
            }
            return locations;
        }
    }
}
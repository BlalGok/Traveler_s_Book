package com.muhammetbilalgok.travelbook.view;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.snackbar.Snackbar;
import com.muhammetbilalgok.travelbook.R;
import com.muhammetbilalgok.travelbook.databinding.ActivityMapsBinding;
import com.muhammetbilalgok.travelbook.model.Place;
import com.muhammetbilalgok.travelbook.roomdb.PlaceDAO;
import com.muhammetbilalgok.travelbook.roomdb.PlaceDatabase;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;
import com.muhammetbilalgok.travelbook.BuildConfig;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, GoogleMap.OnMapLongClickListener{

    private GoogleMap mMap;
    private ActivityMapsBinding binding;
    LocationManager locationManager;
    LocationListener locationListener;
    ActivityResultLauncher<String> permissionLauncher;
    SharedPreferences sharedPreferences;
    boolean control;
    PlaceDatabase db;
    PlaceDAO placeDAO;
    double selectedLat; double selectedLon;
    private CompositeDisposable compositeDisposable = new CompositeDisposable();
    Place selectedPlace;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMapsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        registerLauncher();
        sharedPreferences = MapsActivity.this.getSharedPreferences("com.muhammetbilalgok.travelbook",MODE_PRIVATE);

        db = Room.databaseBuilder(getApplicationContext(), PlaceDatabase.class, "Places")
                //.allowMainThreadQueries()
                .build();
        placeDAO = db.placeDAO();
        selectedLat = 0; selectedLon = 0;
        binding.savebtn.setEnabled(false);

    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setOnMapLongClickListener(this);

        Intent intent = getIntent();
        String oldnew = intent.getStringExtra("oldnew");

        if(oldnew.equals("new"))
        {
            binding.savebtn.setVisibility(View.VISIBLE);
            binding.deleteBtn.setVisibility(View.GONE);

            locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
            locationListener = new LocationListener() {
                @Override
                public void onLocationChanged(@NonNull Location location) {

                    control = sharedPreferences.getBoolean("control", false);
                    if(!control)
                    {
                        LatLng userLoc = new LatLng(location.getLatitude(), location.getLongitude());
                        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(userLoc, 15f));
                        sharedPreferences.edit().putBoolean("control", true).apply();
                    }

                }
            };
            if(ContextCompat.checkSelfPermission(this,Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED)
            {
                if(ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION))
                {
                    Snackbar.make(binding.getRoot(),"Permission for Maps", Snackbar.LENGTH_INDEFINITE).setAction("Give permission", new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            permissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION);
                        }
                    }).show();
                }
                else
                {
                    permissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION);
                }
            }
            else
            {
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 3000, 0, locationListener);

                Location lastLoc = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                if(lastLoc!=null)
                {
                    LatLng lastUserLoc = new LatLng(lastLoc.getLatitude(), lastLoc.getLongitude());
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(lastUserLoc, 15f));
                }
                mMap.setMyLocationEnabled(true);
            }
        }
        else
        {
            mMap.clear();

            selectedPlace = (Place) intent.getSerializableExtra("place");
            LatLng selectedPlaceLatLng = new LatLng(selectedPlace.latitude, selectedPlace.longitude);
            mMap.addMarker(new MarkerOptions().position(selectedPlaceLatLng).title(selectedPlace.name));

            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(selectedPlaceLatLng, 15f));
            binding.placeTxt.setText(selectedPlace.name);
            binding.savebtn.setVisibility(View.GONE);
            binding.deleteBtn.setVisibility(View.VISIBLE);
        }

    }
    private void registerLauncher()
    {
        permissionLauncher = registerForActivityResult(new ActivityResultContracts.RequestPermission(), new ActivityResultCallback<Boolean>() {
            @Override
            public void onActivityResult(Boolean result) {
                if(result)
                {
                    if(ContextCompat.checkSelfPermission(MapsActivity.this,Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED)
                    {
                        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 3000, 0, locationListener);

                        Location lastLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                        if(lastLocation != null)
                        {
                            LatLng lastLocUser = new LatLng(lastLocation.getLatitude(), lastLocation.getLongitude());
                            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(lastLocUser,15f));
                        }
                    }
                }
                else
                {
                    Toast.makeText(MapsActivity.this,"Permission Needed",Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    @Override
    public void onMapLongClick(@NonNull LatLng latLng) {
        mMap.clear();

        mMap.addMarker(new MarkerOptions().position(latLng));
        selectedLat = latLng.latitude; selectedLon = latLng.longitude;

        binding.savebtn.setEnabled(true);
    }

    public void save(View view)
    {
        Place place = new Place(binding.placeTxt.getText().toString(), selectedLat, selectedLon);

        //placeDAO.insert(place).subscribeOn(Schedulers.io()).subscribe();
        compositeDisposable.add(placeDAO.insert(place)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(MapsActivity.this::handleResponse)
        );
    }
    private void handleResponse()
    {
        Intent intent = new Intent(MapsActivity.this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }
    public void delete(View view)
    {
        if(selectedPlace != null)
        {
            compositeDisposable.add(placeDAO.delete(selectedPlace)
                    .observeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(MapsActivity.this:: handleResponse));
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        compositeDisposable.clear();
    }
}
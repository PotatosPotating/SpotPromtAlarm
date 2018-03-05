package edu.depaul.phingora.spotpromtalarm;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import static android.content.pm.PackageManager.PERMISSION_GRANTED;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener, GoogleMap.OnMarkerClickListener, View.OnClickListener {

    private GoogleMap mGoogleMap;
    private GoogleApiClient mGoogleApiClient;
    private LocationManager locationManager;
    private Location l;
    private static Button alarmListBt;
    private static double currentLatitude = 0;
    private static double currentLongitude = 0;
    private static TextView textviewLat;
    private static TextView textviewLong;
    private static boolean isClicked =true;
    SupportMapFragment mapFragment;
    RelativeLayout main;
    LinearLayout popuplayout;
    Location location;
    private static int flag=0;
    PopupWindow popUpWindow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        main= new RelativeLayout(this);
        popuplayout= new LinearLayout(this);
        mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                    PERMISSION_GRANTED);

        }
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 0, new android.location.LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                l=location;
                currentLongitude = location.getLongitude();
                currentLatitude = location.getLatitude();
                textviewLat.setText(" " + currentLatitude);
                textviewLong.setText(" " + currentLongitude+"/");
                checkAlarmList(l);

            }
            @Override
            public void onStatusChanged(String s, int i, Bundle bundle) {

            }

            @Override
            public void onProviderEnabled(String s) {

            }

            @Override
            public void onProviderDisabled(String s) {

            }
        });

        textviewLat = (TextView) findViewById(R.id.display_lat);
        textviewLong = (TextView) findViewById(R.id.display_long);
        alarmListBt = (Button) findViewById(R.id.goto_alarms_button);
        alarmListBt.setOnClickListener(this);

    }

    private void checkAlarmList(Location location) {

        if(Alarms.getLength()<1)
            return;
        if(location==null)
            return;
        for(SingleAlarm s: Alarms.returnList())
        {            if(s.getLocation().distanceTo(location) <100)
            {
                Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
                Ringtone r = RingtoneManager.getRingtone(getApplicationContext(), notification);
                r.play();
                Intent intent = new Intent(this,Ring.class);
                Bundle bundle = new Bundle();

                bundle.putString("title", s.getName());
                bundle.putString("description",s.getDescription());
                bundle.putInt("id",s.getid());
                intent.putExtras(bundle);
                startActivity(intent);
                return;
            }
            else
            return;
        }
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mGoogleMap = googleMap;
        mGoogleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);

        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(this, android.
                    Manifest.permission.ACCESS_FINE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED) {
                buildGoogleApiClient();
                mGoogleMap.setMyLocationEnabled(true);
                mGoogleMap.setOnMarkerClickListener(this);
            }
        } else {
            buildGoogleApiClient();
            mGoogleMap.setMyLocationEnabled(true);
            mGoogleMap.setOnMarkerClickListener(this);
        }
    }

    private void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        mGoogleApiClient.connect();
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        mGoogleMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {

            @Override
            public void onMapClick(LatLng latLng) {

                MarkerOptions markerOptions = new MarkerOptions();

                markerOptions.position(latLng);

                markerOptions.title(latLng.latitude + " : " + latLng.longitude);

                mGoogleMap.clear();

                mGoogleMap.animateCamera(CameraUpdateFactory.newLatLng(latLng));

                mGoogleMap.addMarker(markerOptions);
            }
        });
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        Intent intent = new Intent(this, AlarmHandler.class);
        LatLng l = marker.getPosition();
        Bundle extras = new Bundle();
        extras.putDouble("latitude",l.latitude);
        extras.putDouble("longitude",l.longitude);
        intent.putExtras(extras);
        startActivity(intent);
        flag=1;
        return true;
    }

    @Override
    public void onClick(View view) {
        switch(view.getId())
        {
            case R.id.goto_alarms_button:
            {
                Intent intent = new Intent(this, AlarmListDisplay.class);
                startActivity(intent);
            }
        }
    }
}


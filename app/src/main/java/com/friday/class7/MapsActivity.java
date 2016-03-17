package com.friday.class7;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.Manifest;

import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polygon;
import com.google.android.gms.maps.model.PolygonOptions;


public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener, GoogleMap.OnPolygonClickListener {

    private GoogleMap mMap;
    private GoogleApiClient googleApiClient;
    private LocationRequest request;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        googleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();

        // fused location services
        request = LocationRequest.create()
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                .setInterval(5000)
                .setFastestInterval(1000);
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

        // Add a marker in Sydney and move the camera
        // LATITUDE, LONGITUDE
        LatLng zone1 = new LatLng(20.736866, -103.453614);
        //mMap.addMarker(new MarkerOptions().position(zone1).title("ZONE 1").alpha(0.5f));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(zone1, 15));
        Polygon polygon0 = mMap.addPolygon(new PolygonOptions()
                .add(new LatLng(20.737596, -103.453963),
                        new LatLng(20.737456, -103.453070),
                        new LatLng(20.736209, -103.453290),
                        new LatLng(20.736269, -103.454081))
                .strokeWidth(0)
                .fillColor(0x3F0000FF));

        polygon0.setClickable(true);

        LatLng zone2 = new LatLng(20.735528, -103.453767);
        //mMap.addMarker(new MarkerOptions().position(zone2).title("ZONE 2").alpha(0.5f));
       
        Polygon polygon1 = mMap.addPolygon(new PolygonOptions()
                .add(new LatLng(20.736112, -103.454094),
                        new LatLng(20.736054, -103.453300),
                        new LatLng(20.734704, -103.453509),
                        new LatLng(20.734758, -103.454222))
                .strokeWidth(0)
                .clickable(true)
                .fillColor(0x3F00FF00));

        polygon1.setClickable(true);


    }

    @Override
    public void onConnected(Bundle bundle) {

        // check for permissions (android 6+)
        if(ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED){

            // if we don't have the fine location permission ask for it
            Log.d("ON CONNECTED", "NO PERMISSION GRANTED YET");

            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 0);
            return;
        }

        Location location = LocationServices.FusedLocationApi.getLastLocation(googleApiClient);

        if(location == null){

            // request a location update
            LocationServices.FusedLocationApi.requestLocationUpdates(googleApiClient, request, this);

        } else {
            Log.d("location", location.toString());
        }
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }

    @Override
    protected void onPause() {
        super.onPause();
        if(googleApiClient.isConnected()){
            googleApiClient.disconnect();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        googleApiClient.connect();
    }

    @Override
    public void onLocationChanged(Location location) {

        Log.d("LOCATION", location.toString());
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);



        googleApiClient.connect();

    }

    @Override
    public void onPolygonClick(Polygon polygon) {
        Toast.makeText(getApplicationContext(),polygon.toString(),Toast.LENGTH_SHORT).show();
        Intent i = new Intent(this, Main2Activity.class);
        startActivity(i);
    }

    //AIzaSyDNPRmRmmLz74NrBAtzs4udDBY528-75HE
    //AIzaSyBgeNJz9_O1cQHyF-jtFQPAMUE5hotjexY
}

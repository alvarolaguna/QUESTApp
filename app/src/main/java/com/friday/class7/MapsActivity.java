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

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.GroundOverlay;
import com.google.android.gms.maps.model.GroundOverlayOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polygon;
import com.google.android.gms.maps.model.PolygonOptions;


public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener, GoogleMap.OnPolygonClickListener {

    private static final String FIREBASE_URL = "https://smartpark1.firebaseio.com";
    private GoogleMap mMap;
    private GoogleApiClient googleApiClient;
    private LocationRequest request;
    long[][][] parkSystem;
    long[] noZones;
    long noParks;
    Polygon[] polygons;
    Location myLocation;
    GroundOverlayOptions parkingMap;
    GroundOverlay imageOverlay;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        Firebase.setAndroidContext(this);
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
                .setInterval(2000)
                .setFastestInterval(1000);

        Firebase parkArray = new Firebase(FIREBASE_URL);


        parkArray.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                System.out.println("ESTACIONAMIENTO: " + dataSnapshot.getChildrenCount());
                noParks = dataSnapshot.getChildrenCount();

            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });
        noZones = new long[(int)noParks];

        Firebase[] park = new Firebase[(int)noParks];
        for (int i = 0; i < noParks; i++){
            String temp = "Estacionamiento"+(i+1);
            park[i] = new Firebase(FIREBASE_URL).child(temp);

            final int finalI = i;
            park[i].addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    noZones[finalI] = dataSnapshot.getChildrenCount();
                    System.out.println("noZones"+noZones[finalI]+"");

                }

                @Override
                public void onCancelled(FirebaseError firebaseError) {

                }
            });

        }
        //testin purposes
        Firebase zone = new Firebase(FIREBASE_URL).child("Estacionamiento1/Zona0");
        zone.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                System.out.println("children of zones"+dataSnapshot.getChildrenCount());
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });
        //orientado a objetos pendiente!!
        //corregir con datos dinámicos
        parkSystem = new long[1][2][2];
        //(int)noZones[0]

        //implementar iteracion con json corregido Zona0 no ZonaA
        Firebase carLimit = new Firebase(FIREBASE_URL).child("Estacionamiento1/Zona0/carLimit");
        Firebase currentCars = new Firebase(FIREBASE_URL).child("Estacionamiento1/Zona0/currentCars");
        System.out.println("noParks "+noParks+"");
        carLimit.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                parkSystem[0][0][0] = (long) dataSnapshot.getValue();
                long i = (long) dataSnapshot.getValue();

                System.out.println("geting carlimit: "+i);

            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });
        currentCars.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                parkSystem[0][0][1] = (long) dataSnapshot.getValue();
                polygons[0].setFillColor(evalColor(parkSystem[0][0][0], parkSystem[0][0][1]));
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });

        Firebase carLimit2 = new Firebase(FIREBASE_URL).child("Estacionamiento1/Zona1/carLimit");
        Firebase currentCars2 = new Firebase(FIREBASE_URL).child("Estacionamiento1/Zona1/currentCars");
        //for(int i = )


        carLimit2.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                parkSystem[0][1][0] = (long) dataSnapshot.getValue();
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });
        currentCars2.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                parkSystem[0][1][1] = (long) dataSnapshot.getValue();
                polygons[1].setFillColor(evalColor(parkSystem[0][1][0], parkSystem[0][1][1]));
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });

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

        polygons = new Polygon[2];
        //(int)noZones[0]
        // Add a marker in Sydney and move the camera
        // LATITUDE, LONGITUDE
        LatLng parkCoord = new LatLng(20.736218, -103.454357);
        LatLng carCood = new LatLng(20.737091, -103.453091);
        mMap.addMarker(new MarkerOptions()
                .position(parkCoord)
                .title("ITESM Parking LOT")
                        //.visible(false)
                        //.icon(BitmapDescriptorFactory.fromResource(R.drawable.car))
                .alpha(0.5f));

        parkingMap = new GroundOverlayOptions()
                .image(BitmapDescriptorFactory.fromResource(R.drawable.car))
                .position(carCood, 10f, 13f);
                //.position(new LatLng(myLocation.getLatitude(), myLocation.getLongitude()), 10f, 13f);


        imageOverlay = googleMap.addGroundOverlay(parkingMap);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(parkCoord, 17));
        LatLng zone1 = new LatLng(20.736866, -103.453614);
        LatLng zone2 = new LatLng(20.735528, -103.453767);
        LatLng[] pointMap1 = new LatLng[]{new LatLng(20.737596, -103.453963),
                new LatLng(20.737456, -103.453070),
                new LatLng(20.736209, -103.453290),
                new LatLng(20.736269, -103.454081)};
        LatLng[] pointMap2 = new LatLng[]{new LatLng(20.736112, -103.454094),
                new LatLng(20.736054, -103.453300),
                new LatLng(20.734704, -103.453509),
                new LatLng(20.734758, -103.454222)};
        LatLng[][] pointSet = new LatLng[][]{pointMap1, pointMap2};


        for (int i = 0; i < polygons.length; i++){
            polygons[i] = mMap.addPolygon(new PolygonOptions()
                    .add(pointSet[i])
                    .strokeWidth(0));

            polygons[i].setClickable(true);
        }


        googleMap.setOnPolygonClickListener(this);


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
        mMap.setMyLocationEnabled(true);
        Location location = LocationServices.FusedLocationApi.getLastLocation(googleApiClient);


        if(location == null){

            // request a location update
            LocationServices.FusedLocationApi.requestLocationUpdates(googleApiClient, request, this);

        } else {
            Log.d("location", location.toString());
            myLocation = location;
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
        myLocation = location;
        Log.d("LOCATION", location.toString());
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);



        googleApiClient.connect();

    }

    @Override
    public void onPolygonClick(Polygon polygon) {
        Toast.makeText(getApplicationContext(),"Zona"+polygon.getId().substring(2),Toast.LENGTH_SHORT).show();
        Intent i = new Intent(this, Main2Activity.class);
        long maxCar = parkSystem[0][Integer.parseInt(polygon.getId().substring(2))][0],
                curCar = parkSystem[0][Integer.parseInt(polygon.getId().substring(2))][1];
        i.putExtra("values",
                new String[]{"0",
                        polygon.getId().substring(2),
                        maxCar+"",
                        curCar+"",
                        (maxCar - curCar)+""});
        System.out.println("Estacionamiento: 0");
        System.out.println("Zona: "+polygon.getId().substring(2));
        System.out.println("Max: "+maxCar);
        System.out.println("ActualCars: "+curCar);
        System.out.println("Delta: "+(maxCar-curCar)+"");
        startActivity(i);
    }

    public int evalColor(long maxCar, long curCar){
        int deltaCar = (int) (maxCar-curCar);
        System.out.println("maxCar "+maxCar);
        System.out.println("curCar "+curCar);
        if(deltaCar == maxCar) return 0x3F0000FF;
        else if(deltaCar < maxCar && deltaCar >= maxCar/2) return 0x3F00FF00;
        else if(deltaCar < maxCar/2 && deltaCar >= maxCar/3) return 0x3FFFFF00;
        else if(deltaCar < maxCar/3 && deltaCar > 0) return 0x3FFFA500;
        else if(deltaCar == 0) return 0x3FFF0000;

        return 0x3FFFFFFF;
    }

}

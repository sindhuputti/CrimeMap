package com.sindhu.cirmemap;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.quickblox.auth.QBAuth;
import com.quickblox.auth.model.QBSession;
import com.quickblox.core.QBEntityCallback;
import com.quickblox.core.exception.QBResponseException;
import com.quickblox.customobjects.QBCustomObjects;
import com.quickblox.customobjects.model.QBCustomObject;
import com.quickblox.users.QBUsers;
import com.quickblox.users.model.QBUser;

import java.util.ArrayList;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    public static QBUser myUser;
    FloatingActionButton fab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        fab = (FloatingActionButton) findViewById(R.id.fab);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), SendCrime.class));
            }
        });

        createSession();
    }

    private void createSession() {
        QBAuth.createSession(new QBEntityCallback<QBSession>() {
            @Override
            public void onSuccess(QBSession qbSession, Bundle bundle) {
                QBUser user = new QBUser();
                user.setLogin("sindhuCrime");
                user.setPassword("12345678");
                try {
                    QBUsers.signUp(user);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                try {
                    QBUsers.signIn(user, new QBEntityCallback<QBUser>() {

                        @Override
                        public void onSuccess(QBUser qbUser, Bundle bundle) {
                            myUser = qbUser;
                            getCrimes();
                        }

                        @Override
                        public void onError(QBResponseException e) {

                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(QBResponseException error) {
                // Show errors
            }
        });
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        googleMap.setMyLocationEnabled(true);

        // Add a marker in Sydney and move the camera

    }

    public void getCrimes() {

        QBCustomObjects.getObjects("Crimes", new QBEntityCallback<ArrayList<QBCustomObject>>() {
            @Override
            public void onSuccess(ArrayList<QBCustomObject> qbCustomObjects, Bundle bundle) {

                if (qbCustomObjects.size() > 0) {

                    for (QBCustomObject object : qbCustomObjects) {
                        double lat = Double.parseDouble(object.getString("Latitude"));
                        double lng = Double.parseDouble(object.getString("Longitude"));
                        LatLng latLng = new LatLng(lat, lng);
                        if (object.getString("Type").equalsIgnoreCase("Medium")) {
                            mMap.addMarker(new MarkerOptions().position(latLng).title(object.getString("Description")).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_YELLOW)));
                        } else if (object.getString("Type").equalsIgnoreCase("High")) {
                            mMap.addMarker(new MarkerOptions().position(latLng).title(object.getString("Description")).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)));
                        } else {
                            mMap.addMarker(new MarkerOptions().position(latLng).title(object.getString("Description")).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)));
                        }
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "No crimes found", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onError(QBResponseException e) {

            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (myUser != null) {
            getCrimes();
        }
    }
}

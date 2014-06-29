package ab.gpsinfo;

import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class MainActivity extends FragmentActivity implements LocationListener {

    private TextView ProviderInfo;
    private GoogleMap mMap;
    private LocationManager locationManager;
    private LocationManager locationManagerU;
    private String provider;
    private String providerU;

    // no costs, high accuracy, low battery consumption means that wifi or mobile networks will be
    // used for the location provider when possible. Otherwise GPS will be used.


    // Called on the start of the activity
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        ProviderInfo = (TextView)findViewById(R.id.ProviderInfoTextView);
        mMap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map)).getMap();

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);


        if(locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)){
            provider = locationManager.NETWORK_PROVIDER;
        }
        else if(locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)){
            provider = locationManager.GPS_PROVIDER;
        }

        Location location = locationManager.getLastKnownLocation(provider);

        if (location != null) {
            ProviderInfo.setText("Location provider: " + provider);

            LatLng coordinates = new LatLng(location.getLatitude(), location.getLongitude());

            mMap.addMarker(new MarkerOptions()
                    .position(coordinates)
                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.here)));

            CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(coordinates, 17);
            mMap.animateCamera(cameraUpdate);

            // Called continuously when the location changes..
            onLocationChanged(location);

        } else {
            ProviderInfo.setText("Location provider not available");
        }
    }


    @Override
    public void onLocationChanged(Location location) {

        if(locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)){
            provider = locationManager.NETWORK_PROVIDER;
        }
        else if(locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)){
            provider = locationManager.GPS_PROVIDER;
        }


        ProviderInfo.setText("Location provider: " + provider);

        Toast.makeText(getApplicationContext(), "Latitude: "  + String.valueOf(location.getLatitude()) + "\n Longitude: " + String.valueOf(location.getLongitude()), Toast.LENGTH_SHORT).show();

        LatLng coordinates = new LatLng(location.getLatitude(), location.getLongitude());

        if(provider.equals("network")) {
            mMap.addMarker(new MarkerOptions()
                    .position(coordinates)
                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.next)));
        }

        else if(provider.equals("gps")){
            mMap.addMarker(new MarkerOptions()
                    .position(coordinates)
                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.next_gps)));
        }

        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(coordinates, 17);
        mMap.animateCamera(cameraUpdate);

    }

    // For handling changes in the set of available providers.
    @Override
    public void onProviderEnabled(String provider) {

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        if(locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)){
            provider = locationManager.NETWORK_PROVIDER;
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,1000,0,this);
        }
        else if(locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)){
            provider = locationManager.GPS_PROVIDER;
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,1000,0,this);
        }
        Toast.makeText(this, "New location provider: " + provider, Toast.LENGTH_SHORT).show();
    }

    // For handling changes in the set of available providers.
    @Override
    public void onProviderDisabled(String provider) {

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        if(!locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)){
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,1000,0,this);
        }
        else if(!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)){
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,1000,0,this);
        }

        Toast.makeText(this, "Current location provider disabled" , Toast.LENGTH_SHORT).show();
    }

    // Request updates at startup
    @Override
    protected void onResume() {
        super.onResume();
        locationManager.requestLocationUpdates(provider, 400, 1, this);
    }

    // Remove the LocationListener updates when Activity is paused
    @Override
    protected void onPause() {
        super.onPause();
        locationManager.removeUpdates(this);
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        // TODO Auto-generated method stub

    }


}
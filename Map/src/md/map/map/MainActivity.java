package md.map.map;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.widget.TextView;

public class MainActivity extends FragmentActivity implements LocationListener {

    GoogleMap googleMap; 

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //show error dialog if GoolglePlayServices not available
        if (!isGooglePlayServicesAvailable()) {
            finish();
        }
        setContentView(R.layout.activity_main);
        SupportMapFragment supportMapFragment =   //getting our map from support map fragment by id
                (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        googleMap = supportMapFragment.getMap();
        googleMap.setMyLocationEnabled(true); // enabling tracking our location
        LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        Criteria criteria = new Criteria();  // Criteria by wich we choose our provider ( speed, accurasy etc.)
        String bestProvider = locationManager.getBestProvider(criteria, true); // getting provider
        Location location = locationManager.getLastKnownLocation(bestProvider); //Setting our location as last known
        if (location != null) {  // if there is no last known location - get the new one
            onLocationChanged(location);
        }
        locationManager.requestLocationUpdates(bestProvider, 20000, 0, this); // requesting for location update, 20000 - min time interval
    }

    @Override
    public void onLocationChanged(Location location) {
        TextView locationTv = (TextView) findViewById(R.id.latlongLocation); // Our text under the map
        double latitude = location.getLatitude();
        double longitude = location.getLongitude();
        LatLng latLng = new LatLng(latitude, longitude);
        googleMap.clear();  // clearing the map of old markers
        googleMap.addMarker(new MarkerOptions().position(latLng));  //adding marker on out position
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));  // moving camera to new position
        googleMap.animateCamera(CameraUpdateFactory.zoomTo(15));  // slowly zoom in to new position
        locationTv.setText("Latitude:" + latitude + ", Longitude:" + longitude);  // setting the text coords
    }

    @Override
    public void onProviderDisabled(String provider) {
        // TODO Auto-generated method stub
    }

    @Override
    public void onProviderEnabled(String provider) {
        // TODO Auto-generated method stub
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        // TODO Auto-generated method stub
    }

    private boolean isGooglePlayServicesAvailable() { // checking if google services are available
        int status = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
        if (ConnectionResult.SUCCESS == status) {
            return true;
        } else {
            GooglePlayServicesUtil.getErrorDialog(status, this, 0).show(); // show error dialog
            return false;
        }
    }
}
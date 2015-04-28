package com.franbaena.mappingapp;

import java.util.ArrayList;
import java.util.List;

import com.mapquest.android.maps.MapActivity;
import com.mapquest.android.maps.MapView;
import com.mapquest.android.maps.GeoPoint;

import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;



public class MainActivity extends MapActivity implements LocationListener {

	
	MapView map;
	List<POI> pois;
	LocationManager mgr;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        GeoPoint map_center;
        Location start_location = null;
        
        /* Set Location Manager*/
        mgr=(LocationManager)getSystemService(Context.LOCATION_SERVICE);
        mgr.requestLocationUpdates(LocationManager.GPS_PROVIDER,0,0,this);
        
        if(mgr.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
        	start_location = mgr.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        } 

        if((start_location == null) && 
        		mgr.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
        	start_location = mgr.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
        }
        
        if (start_location == null) {
        	map_center=new GeoPoint(50.9,-1.4);
        } else {
        	map_center=new GeoPoint(start_location.getLatitude(),
        			start_location.getLongitude());
        }
        
        map = (MapView)findViewById(R.id.map1);
        map.setBuiltInZoomControls(true);
        map.getController().setZoom(14);
        map.getController().setCenter(map_center);
        pois = new ArrayList<POI>();
        
        
        Log.d("Fran Cat", "App Started!");
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here
    	
        int id = item.getItemId();
        if (id == R.id.addPOI) {
        	// Add POI menu item
        	Intent intent = new Intent(this,AddPOIActivity.class);
        	startActivityForResult(intent,0);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent){
    	if(requestCode==0){
    		
    		
            if (resultCode==RESULT_OK){
            	String name, type, description;
                Bundle extras=intent.getExtras();
                name = extras.getString("com.franbaena.mappingapp.poiname");
                type = extras.getString("com.franbaena.mappingapp.poitype");
                description = extras.getString("com.franbaena.mappingapp.poidescription");
                Location loc = mgr.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                POI new_poi = new POI(name, 
        				type,
        				description,
        				loc.getLatitude(),
        				loc.getLongitude() );
                pois.add(new_poi);
                
                Log.d("Fran Cat", new_poi.toString());
                
            }
        }
    	
    }


	@Override
	protected boolean isRouteDisplayed() {
		// TODO Auto-generated method stub
		return false;
	}


	@Override
	public void onLocationChanged(Location location) {
		// TODO Auto-generated method stub
		 map.getController().setCenter(
				 new GeoPoint(location.getLatitude(),
						 location.getLongitude()));
		
	}


	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void onProviderEnabled(String provider) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void onProviderDisabled(String provider) {
		// TODO Auto-generated method stub
		
	}

}

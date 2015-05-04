package com.franbaena.mappingapp;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;

import com.mapquest.android.maps.DefaultItemizedOverlay;
import com.mapquest.android.maps.MapActivity;
import com.mapquest.android.maps.MapView;
import com.mapquest.android.maps.GeoPoint;
import com.mapquest.android.maps.OverlayItem;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import android.app.Activity;
import android.os.AsyncTask;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

import android.content.Context;
import android.app.AlertDialog;
import android.os.Environment;
import android.preference.PreferenceManager;


public class MainActivity extends MapActivity implements LocationListener {
	/**
	 * AsyncTask to save POIs to a file
	 * @author franbaena
	 */
	class POISOut extends AsyncTask<String,Void,String>{
		
		protected String doInBackground(String... params) {
			String message = "POIS successfuly saved!";
	        try{
	            String filename = Environment.getExternalStorageDirectory().
	            getAbsolutePath()+"/POIsPer.txt"; 
	            PrintWriter pw = new PrintWriter(new FileWriter(filename, false));
	            pw.println(params[0]);
	            pw.close();
	        }catch(IOException e){
	            message = e.toString();
	        }
	        return message;
		}
		
		public void onPostExecute(String message){
	        new AlertDialog.Builder(MainActivity.this).setMessage(message).
	            setPositiveButton("OK",null).show();
	    }
	}
	/**
	 * AsyncTask to save POIs to Internet
	 * @author franbaena
	 */
	class POISOutInt extends AsyncTask<String,Void,String>{
		
		protected String doInBackground(String... params) {
			String message = "";
			HttpURLConnection conn = null;
            try{
                URL url = new URL("http://www.free-map.org.uk/course/mad/ws/addpoi.php");
                 conn = (HttpURLConnection) url.openConnection();
                 
                 String postData = params[0];
                 conn.setDoOutput(true);
                 conn.setFixedLengthStreamingMode(postData.length());
                 OutputStream out = null;
                 out = conn.getOutputStream();
                 out.write(postData.getBytes());
                if(conn.getResponseCode() == 200){
                	message = "success!";
                } else{
                	message = "error!";
                }
                    
                
            }catch(IOException e){
            	message = "error!";
            } finally {
                if(conn!=null){
                	 conn.disconnect();
                }    
            }
	        return message;
		}
		
		public void onPostExecute(String message){
			Log.d("Fran Cat", message);
	    }
	}
	
	/**
	 * AsyncTask to load POIs from a file
	 * @author franbaena
	 */
	class POISIn extends AsyncTask<Void,Void,String>{
		
		protected String doInBackground(Void... unused) {
			StringBuilder sb = new StringBuilder();
	        try{
	            String filename = Environment.getExternalStorageDirectory().
	            getAbsolutePath()+"/POIsPer.txt"; 
	            BufferedReader reader = new BufferedReader(new FileReader(filename));
	            String l = "";
	            while ((l = reader.readLine()) != null){
	            	sb.append(l);
	            }
	            reader.close();
	        }catch(IOException e){
	        	 new AlertDialog.Builder(MainActivity.this).setMessage("The POIs File doesn't exist yet!").
		            setPositiveButton("OK",null).show();
	        }
	        return sb.toString();
		}
		
		public void onPostExecute(String message){
			(MainActivity.this).loadPOISJSON(message);
	    }
	}
	
	/**
	 * AsyncTask to load POIs from a file
	 * @author franbaena
	 */
	class POISInternet extends AsyncTask<Void,Void,String>{
		
		protected String doInBackground(Void... unused) {
			StringBuilder sb = new StringBuilder();
			HttpURLConnection conn = null;
	        try{
	        	URL url = new URL("http://www.free-map.org.uk/course/mad/ws/getpoi.php?username=user006&format=json");
                conn = (HttpURLConnection) url.openConnection();
               InputStream in = conn.getInputStream();
               if(conn.getResponseCode() == 200){
            	   	BufferedReader reader = new BufferedReader(new InputStreamReader(in));
   	            	String l = "";
   	            	while ((l = reader.readLine()) != null){
   	            		sb.append(l);
   	            	}
   	            	reader.close();   
               }
	        	
	            
	            
	        }catch(IOException e){
	        	 new AlertDialog.Builder(MainActivity.this).setMessage("The POIs File doesn't exist yet!").
		            setPositiveButton("OK",null).show();
	        } finally{
                if(conn!=null)
                    conn.disconnect();
            }
	        return sb.toString();
		}
		
		public void onPostExecute(String message){
			(MainActivity.this).loadPOISJSON(message);
	    }
	}

	
	MapView map;
	List<POI> pois;
	LocationManager mgr;
	Location last_known_location; // Stores the last known location
	SharedPreferences preferences;
    @Override
    
    public Object onRetainNonConfigurationInstance() {
    	   return this.pois;
    }
    
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        setContentView(R.layout.activity_main);
        GeoPoint map_center;
        Location start_location = null;
        
        /* Set Location Manager*/
        mgr=(LocationManager)getSystemService(Context.LOCATION_SERVICE);
        mgr.requestLocationUpdates(LocationManager.GPS_PROVIDER,0,0,this);
        //Is it working?
        
        if(mgr.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
        	start_location = mgr.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        } 

        if((start_location == null) && 
        		mgr.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
        	start_location = mgr.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
        }
        
        if (start_location == null) {
        	// For emulator info, if not ready we fake it
        	start_location = new Location(LocationManager.GPS_PROVIDER);
        	start_location.setLatitude(50.907987);
        	start_location.setLongitude(-1.4002);
        } 
        
        last_known_location = start_location;
        map_center=new GeoPoint(start_location.getLatitude(),
    			start_location.getLongitude());
        
        map = (MapView)findViewById(R.id.map1);
        map.setBuiltInZoomControls(true);
        map.getController().setZoom(16);
        map.getController().setCenter(map_center);
        
        
        /* If list of POIS exists, loads it */
        Object prevList = (Object)getLastNonConfigurationInstance();
        if (prevList==null){
        	pois = new ArrayList<POI>();
        } else {
        	pois = (List<POI>) prevList;
        	for (POI p : pois){
        		addMarker(p);
        	}
        }
        
        Log.d("Fran Cat", "App Started!");
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }
    
    /**
     * Adds a new marker to the map
     * 
     * @param location
     * @param title
     * @param description
     */
    private void addMarker(GeoPoint location, String title, String description){
        DefaultItemizedOverlay overlay =new DefaultItemizedOverlay(
                getResources().getDrawable(R.drawable.marker));

        overlay.addItem(new OverlayItem(
        						location, 
        						title, 
        						description));
        map.getOverlays().add(overlay); 
    }
    
    /**
     * Adds a marker to the map using a POI
     * @param p	POI object
     */
    private void addMarker(POI p){
    	addMarker(p.position(), p.name(), p.description());
    }
    
    public void loadPOISJSON(String json){
    	if (json!=""){
    		try {
    			// Iterate through the array of POIs
				JSONArray a = new JSONArray(json);
				for(int i=0; i<a.length(); i++){
					POI np = new POI(a.getJSONObject(i));
					pois.add(np);
					addMarker(np);
                }
				
				new AlertDialog.Builder(this).setMessage("All POIs successfully added to the Map").
	            setPositiveButton("OK",null).show();
				
			} catch (JSONException e) {
				new AlertDialog.Builder(this).setMessage("You POIs file might be corrupt!").
	            setPositiveButton("OK",null).show();
			}
    	} else {
    		Log.d("Fran Cat", "Not loadin' the POIs");
    	}
    }
    /**
     * Returns a JSON String with all POIs in memory
     * 
     * @return JSON String with all POIs
     */
    public String getPOIS(){
    	JSONArray a = new JSONArray();
    	for (POI p : pois){
    		a.put(p.toJSONObject());
    	}
    	
    	return a.toString();
    }
    
    /**
     * Saves all POIs in memory into a File
     */
    public void savePOIS(){
    	POISOut savePOIs = new POISOut();
    	savePOIs.execute(getPOIS());
    	Log.d("Fran Cat", "Savin' the POIs");
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here
    	
        int id = item.getItemId();
        if  (id == R.id.viewPOIList){
        	
        	Intent intent = new Intent(this,POIListActivity.class);
        	intent.putExtra("poi_names", poi_names());
        	intent.putExtra("poi_descriptions", poi_descriptions());
        	startActivityForResult(intent,1);
        } else if (id == R.id.addPOI) {

        	// Add POI menu item
        	Intent intent = new Intent(this,AddPOIActivity.class);
        	startActivityForResult(intent,0);
            return true;
        } else if (id == R.id.saveToFile){
        	savePOIS();
        } else if (id == R.id.loadFromFile){
        	POISIn loadPOIs = new POISIn();
        	loadPOIs.execute();
        } else if (id == R.id.loadFromInternet){
        	POISInternet loadPOIs = new POISInternet();
        	loadPOIs.execute();
        } else if (id == R.id.preferences){
        	startActivity(new Intent(this, MyPreferences.class));
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
               
                POI new_poi = new POI(name, 
        				type,
        				description,
        				last_known_location.getLatitude(),
        				last_known_location.getLongitude() );
                
                pois.add(new_poi); // The new POI is added to the pois list
                
                //And a new marker is added to the map
                addMarker(new_poi);         
                
               if(preferences.getBoolean("autoupload", true)){
            	   // Handle Upload
            	   String postData = "username=user006&name=" + new_poi.name() 
            			   + "&type=" + new_poi.type() 
            			   + "&description=" + new_poi.description() 
            			   + "&lat=" + new_poi.latitude() 
            			   + "&lon=" + new_poi.longitude();
            	   POISOutInt postPOI = new POISOutInt();
            	   postPOI.execute(postData);
               }
            }
        } else if(requestCode==1){
        	if (resultCode==RESULT_OK){
        		Bundle extras=intent.getExtras();
        		centerMapIn(extras.getInt("com.franbaena.mappingapp.poi_id"));
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
		 last_known_location = location;
		 
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
	
	@Override
	public void onStop(){
		super.onStop();
		
	}
	
	public String[] poi_names(){
		String[] entries = new String[pois.size()];
		for (int i=0; i<pois.size(); i++){
			entries[i] = pois.get(i).name();
		}
    	return entries;
    }

    public String[] poi_descriptions(){
    	String[] entries = new String[pois.size()];
		for (int i=0; i<pois.size(); i++){
			String s = pois.get(i).description();
			entries[i] = s.substring(0, Math.min(s.length(), 10)).concat("...");
		}
		return entries;
    }
    
    public void centerMapIn(int poi_index){
    	map.getController().setCenter(pois.get(poi_index).position());
    }

}

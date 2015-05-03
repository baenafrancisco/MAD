package com.franbaena.mappingapp;

import android.util.Log;

import com.mapquest.android.maps.GeoPoint;


import org.json.JSONObject;
import org.json.JSONException;

/**
 * @author Francisco Baena (baena.francisco@gmail.com)
 * POI is a class to handle Points of Interest
 */
public class POI {
	
	private String name, type, description;
	private double latitude, longitude;
	/**
	 * Constructs a new POI specifying its parameters
	 * 
	 * @param n	name
	 * @param t	type
	 * @param d	description
	 * @param lat	latitude
	 * @param lon	longitude
	 */
	public POI(String n, String t, String d, double lat, double lon){
		name = n;
		type = t;
		description = d;
		latitude = lat;
		longitude = lon;
	}
	
	/**
	 * Constructs a new POI from JSONObject instance
	 * @param o	JSONObject instance representing a POI
	 */
	public POI(JSONObject o){
		try{
			name = o.getString("name");
			type = o.getString("type");
			description = o.getString("description");
			latitude = o.getDouble("lat");
			longitude = o.getDouble("lon");
		} catch (JSONException e){
			Log.d("Fran Cat", "There's been a JSON error!" + e.getMessage());
		}	
	}
	
	/**
	 * Constructs a new POI from JSON String
	 * @param json	JSON string
	 * @throws JSONException 
	 */
	public POI(String json) throws JSONException{
		this(new JSONObject(json));
	}
	
	/**
	 * GETTERS
	 */
	public String name(){
		return name;
	}
	public String type(){
		return type;
	}
	public String description(){
		return description;
	}
	public double latitude(){
		return latitude;
	}
	
	public double longitude(){
		return longitude;
	}
	
	/**
	 * Returns a com.mapquest.android.maps.GeoPoint object
	 * with the position of the POI
	 * @return 	current position of the POI
	 */
	public GeoPoint position(){
		return new GeoPoint(latitude,longitude);
	}
	
	/**
	 * Sets the position of the POI
	 * @param lat	latitude of the POI
	 * @param lon	longitude of the POI
	 */
	public void setPosition(double lat, double lon){
		latitude = lat;
		longitude = lon;
	}
	public void name(String n){
		name = n;
	}
	public void type(String t){
		type = t;
	}
	public void description(String d){
		description = d;
	}
	
	/** 
	 * Returns a org.json.JSONObject with information about the POI
	 * @return org.json.JSONObject with information about the POI
	 */
	public JSONObject toJSONObject(){
		JSONObject o = new JSONObject();
		try{
			o.put("name", name);
			o.put("type", type);
			o.put("description", description);
			o.put("lat", latitude);
			o.put("lon", longitude);
		} catch (JSONException e){
			Log.d("Fran Cat", "There's been a JSON error!" + e.getMessage());
		}
		return o;
	}
	/**
	 * Returns a String with information about the POI
	 * @return JSON String with with information about the POI
	 */
	public String toJSON(){
		return toJSONObject().toString();
	}

	public String toString(){
		return "<POI: "+ name +"  @ ("+latitude+", "+ longitude +")>";
	}

}

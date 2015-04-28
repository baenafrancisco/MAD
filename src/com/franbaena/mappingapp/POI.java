package com.franbaena.mappingapp;

import com.mapquest.android.maps.GeoPoint;

/**
 * @author Francisco Baena (baena.francisco@gmail.com)
 * POI is a class to handle Points of Interest
 */
public class POI {
	
	private String name, type, description;
	private double latitude, longitude;
	
	public POI(String n, String t, String d, double lat, double lon){
		name = n;
		type = t;
		description = d;
		latitude = lat;
		longitude = lon;
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

	public String toString(){
		return "<POI: "+ name +"  @ ("+latitude+", "+ longitude +")>";
	}

}

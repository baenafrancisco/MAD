package com.franbaena.mappingapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

public class POIListActivity extends Activity{
	
	
	
	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getResources().getConfiguration().orientation == android.content.res.Configuration.ORIENTATION_LANDSCAPE) {
            finish();
            return;
        }
        setContentView(R.layout.activity_poilist);
    }
	
	public String[] poi_names(){
		 Intent intent= (Intent)getIntent();
		 return intent.getStringArrayExtra("poi_names");
	}
	
	public String[] poi_descriptions(){
		 Intent intent= (Intent)getIntent();
		 return intent.getStringArrayExtra("poi_descriptions");
	}
	public void centerMapIn(int idx){
		Intent intent = new Intent();
	    Bundle bundle=new Bundle();
	    bundle.putInt("com.franbaena.mappingapp.poi_id",idx);
	    intent.putExtras(bundle);
	    setResult(RESULT_OK,intent); 
	    finish();
	}

}

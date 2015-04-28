package com.franbaena.mappingapp;// or whatever it is in your case

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.content.Intent;

public class AddPOIActivity extends Activity implements OnClickListener{
	
	protected EditText name, type, description;

    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.addpoilayout);
        name = (EditText)findViewById(R.id.poiname);
        type = (EditText)findViewById(R.id.poitype);
        description = (EditText)findViewById(R.id.poidescription);
        ((Button)findViewById(R.id.addPOIBtn)).setOnClickListener(this);
    }

	@Override
	public void onClick(View v) {
		Intent intent = new Intent();
	    Bundle bundle=new Bundle();
	    bundle.putString("com.franbaena.mappingapp.poiname",name.getText().toString());
	    bundle.putString("com.franbaena.mappingapp.poitype",type.getText().toString());
	    bundle.putString("com.franbaena.mappingapp.poidescription",description.getText().toString());
	    intent.putExtras(bundle);
	    setResult(RESULT_OK,intent); 
	    finish();
		
	}
}


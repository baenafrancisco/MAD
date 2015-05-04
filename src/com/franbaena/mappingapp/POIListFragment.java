package com.franbaena.mappingapp;


import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.app.ListFragment;
import android.content.Context;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.view.View;
import android.widget.Toast;

public class POIListFragment extends ListFragment {
    		String[] names;
    		String[] descriptions;
    		int[] indexes =  { 0, 1, 2, 3 };
   
    public void onActivityCreated (Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        
        
        if (getResources().getConfiguration().orientation == android.content.res.Configuration.ORIENTATION_LANDSCAPE){
        	MainActivity parent = (MainActivity)getActivity();
        	names = parent.poi_names();
        	descriptions = parent.poi_descriptions();
        } else {
        	POIListActivity parent = (POIListActivity)getActivity();
        	names = parent.poi_names();
        	descriptions = parent.poi_descriptions();
        }
        
        POICustomAdapter adapter = new POICustomAdapter();
        setListAdapter(adapter);
    }
    
    public class POICustomAdapter extends ArrayAdapter<String>{
        public POICustomAdapter(){
        	// We have to use ExampleListActivity.this to refer to the outer class (the activity)
            super(POIListFragment.this.getActivity(), android.R.layout.simple_list_item_1, names);
        }
    
        public View getView(int index, View convertView, ViewGroup parent){
        	View view = convertView;
            if(view==null){
                LayoutInflater inflater = (LayoutInflater)getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                view = inflater.inflate(R.layout.poi_list_item, parent, false);
            }
            TextView title = (TextView)view.findViewById(R.id.poi_name), detail = 
                (TextView)view.findViewById(R.id.poi_desc);
            title.setText(names[index]);
            detail.setText(descriptions[index]);
            return view;
        }
    }
    
    public void onListItemClick(ListView lv, View v, int index, long id){
    	
    	if (getResources().getConfiguration().orientation == android.content.res.Configuration.ORIENTATION_LANDSCAPE){
        	MainActivity parent = (MainActivity)getActivity();
        	parent.centerMapIn(index);
        } else {
        	POIListActivity parent = (POIListActivity)getActivity();
        	parent.centerMapIn(index);
        }
    }
    
}

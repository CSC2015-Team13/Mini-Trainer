package com.minitrainer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ExpandableListView;
import android.widget.SimpleExpandableListAdapter;

//expandable listview test
//will be changed to a layout similar to the one used for exercises
public class DietActivity extends Activity {
	  
	  String[] groups = new String[] {"Go vegan!", "I want to eat everything", "Go for seafood"};
	  String[] vegan = new String[] {"Diet 1", "Diet2", "Diet3", "Diet4"};
	  String[] normal = new String[] {"Diet1", "Diet2", "Diet3"};
	  String[] seafood = new String[] {"Diet1", "Diet2", "Diet3", "Diet4"};
	  
	  ArrayList<Map<String, String>> groupData;
	  ArrayList<Map<String, String>> childDataItem;

	  ArrayList<ArrayList<Map<String, String>>> childData;
	  Map<String, String> m;
	  ExpandableListView elvMain;
	  
	  
	    /** Called when the activity is first created. */
	    public void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
	        setContentView(R.layout.activity_diet);
	        
	        groupData = new ArrayList<Map<String, String>>();
	        for (String group : groups) {
	          m = new HashMap<String, String>();
	            m.put("groupName", group); 
	            groupData.add(m);  
	        }
	       
	        String groupFrom[] = new String[] {"groupName"};
	        int groupTo[] = new int[] {android.R.id.text1};
	        

	        childData = new ArrayList<ArrayList<Map<String, String>>>(); 
	        
	        childDataItem = new ArrayList<Map<String, String>>();
	        for (String phone : vegan) {
	          m = new HashMap<String, String>();
	            m.put("phoneName", phone); 
	            childDataItem.add(m);  
	        }
	        childData.add(childDataItem);
       
	        childDataItem = new ArrayList<Map<String, String>>();
	        for (String phone : normal) {
	          m = new HashMap<String, String>();
	            m.put("phoneName", phone);
	            childDataItem.add(m);  
	        }
	        childData.add(childDataItem);
     
	        childDataItem = new ArrayList<Map<String, String>>();
	        for (String phone : seafood) {
	          m = new HashMap<String, String>();
	            m.put("phoneName", phone);
	            childDataItem.add(m);  
	        }
	        childData.add(childDataItem);

	        String childFrom[] = new String[] {"phoneName"};
	        int childTo[] = new int[] {android.R.id.text1};
	        
	        SimpleExpandableListAdapter adapter = new SimpleExpandableListAdapter(
	            this,
	            groupData,
	            android.R.layout.simple_expandable_list_item_1,
	            groupFrom,
	            groupTo,
	            childData,
	            android.R.layout.simple_list_item_1,
	            childFrom,
	            childTo);
	            
	        elvMain = (ExpandableListView) findViewById(R.id.elvMain);
	        elvMain.setAdapter(adapter);
	    }
	}

/*
 * Crystal McDonald
 * Java II
 * 1308
 * 
 */



//Tide Prediction APP
package com.cm.clamztidalprediction;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;

import org.json.JSONException;
import org.json.JSONObject;

import com.cm.ClassFile;
import com.cm.WebFile;
import com.cm.LocDisplay;
import com.cm.R;
import com.example.java1_week3.SearchForm;
import com.example.java1_week3.TempDisplay;
import com.example.java1_week3.MainActivity.TempRequest;

import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {
	Context _context;
	LinearLayout _appLayout;
	SearchForm _search; 
	TidalPrediction _tide;
	LocDisplay _locations;
	Boolean _connected = false;
	HashMap<String, String> _history;
	TextView _title;
	TextView _time;
	TextView _tide;
	TextView _text;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //LinearLayout
        _context = this;
        _appLayout = new LinearLayout(this);
        _history = getHistory();
        Log.i("HISTORY READ",_history.toString());
        
       TextView locLabel = new TextView(this);
        locLabel.setText("Find weather by location. Enter your zip code now.");
        _appLayout.addView(locLabel);

        
        //add it to view to see it
      		_search = new SearchForm(_context, "Enter Zip Code", "Go");
      		
      		//get buttons and fields
      		//add search handler
      		//building this way using more of a class method instead of tags approach
      		//EditText searchField = _search.getField();
      		Button searchButton = _search.getButton();
      		
      		//Adding functionality to get buttons to do something
      		searchButton.setOnClickListener(new OnClickListener() {
      			
      			@Override
      			public void onClick(View v){
      				Log.i("CITY ENTERED: ",_search.getField().getText().toString());
      				getTemps(_search.getField().getText().toString());
      			}
      			
      		});
      		
      		
      		//Detects the network connection
      		_connected = WebFile.getConnectionStatus(_context);
      		if(_connected){
      			Log.i("NETWORK CONNECTION ", WebFile.getConnnectionType(_context));
      		}
      		
      		//add stock display
      		_temperature = new TempDisplay(_context);
      		
      		//add faves display
      		_locations = new LocDisplay(_context);
      		
      		//add views to main layout
      		//added button to LinearLayout
      		_appLayout.addView(_search);
      		_appLayout.addView(_temperature);
      		_appLayout.addView(_locations);
      		
      		
      		//to display under the search bar
      		_appLayout.setOrientation(LinearLayout.VERTICAL);
        
        setContentView(_appLayout);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.activity_main, menu);
        return true;
    }
    

  //Create my custom API URL
    private void getTemps(String zip){
    	Log.i("CLICK",zip);
    	//JSON output.  Weather by zip.
    	String baseURL = "http://query.yahooapis.com/v1/public/yql?q=%20SELECT%20*%20FROM%20weather.forecast%20WHERE%20location%3D%22" + zip + "%22&format=json&callback";
    	//http://weather.yahooapis.com/forecastrss?p=" + "95758"   is hard coded for Elk Grove only. http://weather.yahooapis.com/forecastrss?p=" + zip; outputs xml version. 
    
    	URL finalURL;
    	try{
    		finalURL = new URL(baseURL);
    		Log.i("my url:", baseURL);
    		TempRequest tr = new TempRequest();
    		tr.execute(finalURL);
    		
    	} catch (MalformedURLException e){
    		Log.e("BAD URL", "MALFORMED URL");
    		finalURL = null;
    	}
    }
    
    //create method to get history from Hard drive
    @SuppressWarnings("unchecked")
	private HashMap<String, String> getHistory(){
    	Object stored = ClassFile.readObjectFile(_context, "history", false);
    	
    	HashMap<String, String> history;
    	if(stored == null){
    		Log.i("HISTORY", "NO HISTORY FILE FOUND");
    		history = new HashMap<String, String>();
    	}else{
    		history = (HashMap<String, String>)stored;
    	}
    	return history;
    }
    
    private class TempRequest extends AsyncTask<URL,Void,String>{
    	//override 2 separate functions
    	@Override
    	protected String doInBackground(URL...urls){
    		String response = "";
    		//pass an array even though it only holds one
    		for(URL url: urls){
    			response = WebFile.getURLSTringResponse(url);
    		}
    		return response;
    	}
    	
    	@Override
    	protected void onPostExecute(String result){
    		Log.d("URL RESPONSE",result);
    		
    		try{
    		//parsing through JSON Data   accepts a string as a parameter
    		JSONObject json = new JSONObject(result);
    		JSONObject results = json.getJSONObject("query").getJSONObject("results").getJSONObject("channel");
    		if(results.getString("ttl").compareTo("Yahoo! Weather Error")==0){
    			Toast toast = Toast.makeText(_context, "Invalid Zip ", Toast.LENGTH_LONG);
    			toast.show();
    		}else{
    			Toast toast = Toast.makeText(_context, "Valid Zip" + results.get("title"), Toast.LENGTH_LONG);
    			toast.show();
    			
    			//makes sure history is there
    			_history.put(results.getString("string"), results.toString());
    			//target file to write history to harddrive
    			ClassFile.storeObjectFile(_context, "history", _history, false);
    			ClassFile.storeStringFile(_context, "temp", results.toString(), true);
    		}
    		} catch (JSONException e){
    			Log.e("JSON", "JSON OBJECT EXCEPTION " + e.toString());
    		}
    	}
    	
    }
}

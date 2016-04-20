package com.example.cloud218;

/**
 * This class takes implements the user's response to make a video public or private
 * @author Anumeha
 *
 */
import java.io.UnsupportedEncodingException;

import org.apache.http.entity.StringEntity;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class InputActivity extends ActionBarActivity {
	
	Button private_button;
    Button public_button;
    //TextView input_result;
    private String userName;
    private String accessToken;
    private String userId;
    private String video_fileName;
    
    

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_input);
		
		private_button = (Button) findViewById(R.id.private_button);
		public_button = (Button) findViewById(R.id.public_button);
		//input_result = (TextView)findViewById(R.id.input_result);
		
		
		private_button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
            	
            	Bundle extras = getIntent().getExtras();
        		
        		if (extras != null) {
        		   
        		    userName = extras.getString("userName");
        		    accessToken= extras.getString("accessToken");
        		    userId = extras.getString("userId");
        		    video_fileName = extras.getString("video_fileName");
        		    
        		}
        		
            	
            	new processBackend().execute();
                /*Intent VidIntent = new Intent(InputActivity.this, VideoActivity.class);
                VidIntent.putExtra("permission", "private");
                setResult(RESULT_OK, VidIntent);
                startActivity(VidIntent);*/
       }});
		
		
		public_button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
            	new processBackend().execute();
                /*Intent VidIntent = new Intent(InputActivity.this, VideoActivity.class);
                VidIntent.putExtra("permission", "public");
                setResult(RESULT_OK, VidIntent);
                startActivity(VidIntent);*/
       }});
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.input, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
	
	private class processBackend extends AsyncTask<String, String, JSONObject> {
		String requestType = "permission";
	    @Override
	    protected void onPreExecute() {
	        super.onPreExecute();
	       
	      
	   }

	    @Override
	    protected JSONObject doInBackground(String... args) {
	    	JSONObject jsonRequest = new JSONObject();
	    	
	    		try {
					 jsonRequest.put("userName", userName);
					 jsonRequest.put("accessToken", accessToken);
					 jsonRequest.put("userId", userId);
					 jsonRequest.put("requestType", "permission");
					 jsonRequest.put("name", "test1.mp4");
					 StringEntity se = new StringEntity( jsonRequest.toString());
					 jsonRequest = LibraryFunctions.createURL(requestType, se);
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (UnsupportedEncodingException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
	    		
	    	
	    	
	        return jsonRequest;
	       
	    	
	    }

	    @Override
	    protected void onPostExecute(JSONObject json) {
	    	
	    	try {
	    		Log.e("Response data ", json.toString());
	    		Log.e("success ", json.getString("success"));
	    		Log.e("error", json.getString("error") );
				if(json.getInt("success") == 1 && json.getInt("error") == 0 ) {
					
					Toast.makeText(getApplicationContext(),
	                        json.getString("successMsg"), Toast.LENGTH_LONG).show();
					Intent VideoIntent = new Intent(InputActivity.this, VideoActivity.class);
					
					startActivity(VideoIntent);
				}
				else if(json.getInt("error")==1){
					String errorMsg = json.getString("errorMsg");

					Toast.makeText(getApplicationContext(),
	                        json.getString("errorMsg"), Toast.LENGTH_LONG).show();
				}
			} catch (JSONException e) {
				Log.e("Activity_Video_Logout, OnPostExecute ", "error in json");
				e.printStackTrace();
			}
	    	
	        
	   }
	}
}

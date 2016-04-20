package com.example.cloud218;

/**
 * This class implements the registration activity. It gets the information from the user and send that information to the server and upon receiving the request
    it redirects to the login page
 * @author Anumeha
 *
 */

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.entity.StringEntity;
import org.json.JSONException;
import org.json.JSONObject;

//import com.example.cloud218.LoginActivity.processBackend;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class Registration extends ActionBarActivity {
	
	Button login_button;
    Button register_button;
    EditText fname_edit;
    EditText lname_edit;
    EditText email_edit_register;
    EditText pword_edit_register;
    private TextView registerError;
    
    private static String SUCCESS = "success";
    

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_registration);
		
		login_button = (Button) findViewById(R.id.login_view_button);
		register_button = (Button) findViewById(R.id.register_button);
        fname_edit = (EditText) findViewById(R.id.fname_edit);
        lname_edit = (EditText) findViewById(R.id.lname_edit);
        email_edit_register = (EditText) findViewById(R.id.email_edit_register);
        pword_edit_register = (EditText) findViewById(R.id.pword_edit_register);
        registerError = (TextView) findViewById(R.id.register_error);
        
        
        //Clicking on Login button opens the login view
        login_button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent loginIntent = new Intent(Registration.this, LoginActivity.class);
                startActivityForResult(loginIntent, 0);
                finish();
             }});
        
        //Clicking on Login button process the register activity
        register_button.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {

                if (  ( !fname_edit.getText().toString().equals("")) && 
                		( !lname_edit.getText().toString().equals("")) && 
                		( !email_edit_register.getText().toString().equals(""))  &&
                		( !pword_edit_register.getText().toString().equals("")))
                {
                	//If none of the field empty then call the Network request process in background
                	new processBackend().execute();
                }
                else if ( ( fname_edit.getText().toString().equals("")) )
                {
                	Toast.makeText(getApplicationContext(),
                            "First Nmae can not be empty", Toast.LENGTH_SHORT).show();
                    
                }
                else if ( ( lname_edit.getText().toString().equals("")) )
                {
                	Toast.makeText(getApplicationContext(),
                            "Last Name can not be empty", Toast.LENGTH_SHORT).show();
                }
                else if ( ( email_edit_register.getText().toString().equals("")) )
                {
                	Toast.makeText(getApplicationContext(),
                            "Email can not be empty", Toast.LENGTH_SHORT).show();
                }
                else if ( ( pword_edit_register.getText().toString().equals("")) )
                {
                	Toast.makeText(getApplicationContext(),
                            "Password can not be empty", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    Toast.makeText(getApplicationContext(),
                            "All the fields are required", Toast.LENGTH_SHORT).show();
                }
            }
        });
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.registration, menu);
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
	
	//process Register Request and Response activity in background
	 private class processBackend extends AsyncTask<String, String, JSONObject> {

	        String emailId, passWord, requestType, fname, lname;
	       
	        @Override
	        protected void onPreExecute() {
	            super.onPreExecute();
	           
	            fname_edit = (EditText) findViewById(R.id.fname_edit);
	            lname_edit = (EditText) findViewById(R.id.lname_edit);
	            email_edit_register = (EditText) findViewById(R.id.email_edit_register);
	            pword_edit_register = (EditText) findViewById(R.id.pword_edit_register);
	            
	            
	            fname = fname_edit.getText().toString();
	            lname = lname_edit.getText().toString();
	            emailId = email_edit_register.getText().toString();
	            passWord = pword_edit_register.getText().toString();
	            
	        }

	        @Override
	        protected JSONObject doInBackground(String... args) {
	        
	        	JSONObject jsonRequest = new JSONObject();
	        	
	            try {
	            	jsonRequest.put("fname", fname);
	            	jsonRequest.put("lname", lname);
	            	jsonRequest.put("emailId", emailId);
	            	jsonRequest.put("passWord", passWord);
	            	jsonRequest.put("requestType", "register");
	            	
	            	Log.e("JSONREQUEST REGISTRATION", jsonRequest.toString());
	            	
					 StringEntity se = new StringEntity( jsonRequest.toString());
					 jsonRequest = LibraryFunctions.createURL("register", se);
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (UnsupportedEncodingException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
	           
	            return jsonRequest;
	           
	        	
	        }

	        //method to handle response of the REST apis
	        @Override
	        protected void onPostExecute(JSONObject json) {
	        	registerError = (TextView) findViewById(R.id.register_error);
	        	
	        	try {
	        		Log.e("Response data ", json.toString());
	        		Log.e("success ", json.getString("success"));
	        		Log.e("error", json.getString("error") );
					if(json.getInt("success") == 1 && json.getInt("error") == 0 ) {
						
						Toast.makeText(getApplicationContext(),
		                        json.getString("successMsg"), Toast.LENGTH_LONG).show();
						
						registerError.setText(json.getString("successMsg"));
						Intent loginIntent = new Intent(Registration.this, LoginActivity.class);
						
						startActivity(loginIntent);
						
					}
					else if(json.getString("error")=="1"){
						String errorMsg = json.getString("errorMsg");
						registerError.setText(errorMsg);
						registerError.setVisibility(1);

						Toast.makeText(getApplicationContext(),
		                        json.getString("errorMsg"), Toast.LENGTH_LONG).show();
					}
				} catch (JSONException e) {
					Log.e("Activity_main, OnPostExecute ", "error in json");
					e.printStackTrace();
				}
	        	
	            
	       }
	    }
}

package com.example.cloud218;

/**
 * This class implements action listeners for all the video related activities by users 
 such as My Videos, All Videos, Capture Video
 * @author Anumeha
 *
 */
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class VideoActivity extends ActionBarActivity {
	
	private TextView vid_output_text;
	private Button captr_vid_btn;
	private Button all_vid_btn;
	private Button my_vid_btn;
	private Button del_vid_btn;
	private Button refresh_btn;
	private Button logout_btn;
	private final static int VIDEO =1;
	private final static int VIDEO_REQ_CODE = 200;
	private final static int INPUT_REQ_CODE = 20;
	private Uri uploadFileName;
	private String uploadFilePath = "";
	private ProgressDialog dialog;
	private  String upLoadServerUri = "https://1-dot-assignment2-905.appspot.com/_ah/upload/AMmfu6bUVo8SmiX0h7qxp-SY2qQmeF9DmZ22EvvsvbllVny8WuJnPCVR3VS_3aXaLzR-zC8prI-nEk-wSVDfNKeXpDdAbf37Gy1YbLR6b68MOP8cTegxXV49xPwkB7EA6dCWOJykwK8Q8G1-rKjAB5BZbh6hAse-cg/ALBNUaYAAAAAVIYWD7thC8RKJzBwg76zLdL0h-Eyn2-o/";
	int serverResponseCode = 0;
	
	private static String userId ;
    private static String userName = "";
    private static String accessToken ;
    private static String emailId ;
    private static JSONObject jsonRes;
    private static int input;
    private static String preference;
    private static String video_fileName;
    private static String vidURL;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_video);
		
		captr_vid_btn = (Button)findViewById(R.id.captr_vid_btn);
		all_vid_btn = (Button)findViewById(R.id.all_vid_btn);
		my_vid_btn = (Button)findViewById(R.id.my_vid_btn);
		//del_vid_btn = (Button)findViewById(R.id.del_vid_btn);
		refresh_btn = (Button)findViewById(R.id.refresh_btn);
		logout_btn = (Button)findViewById(R.id.logout_btn);
		vid_output_text = (TextView)findViewById(R.id.vid_output_text);
		
		Bundle extras = getIntent().getExtras();
		
		if (extras != null) {
		   
		    userName = extras.getString("userName");
		    accessToken= extras.getString("accessToken");
		    userId = extras.getString("userId");
		    emailId = extras.getString("emailId");
		    
		}
		
		//capture video listener
		captr_vid_btn.setOnClickListener(new Button.OnClickListener(){

			@Override
			public void onClick(View v) {
				
				new processBackend().execute("createURL");
				
				Intent vidIntent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
				
				Uri VidfileUri = getVideoFilePath(VIDEO); 
				vidIntent.putExtra(MediaStore.EXTRA_OUTPUT, VidfileUri);
				vidIntent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 1);
				
				startActivityForResult(vidIntent, VIDEO_REQ_CODE);
				// TODO Auto-generated method stub
				
				
				
			}
			
		});
		
		//All Video button listener
		all_vid_btn.setOnClickListener(new Button.OnClickListener(){

			@Override
			public void onClick(View v) {
				
				new processBackend().execute("getOneVideo");
				
				//new processBackend().execute("getAllVideos");
				
				//Intent listing=new Intent(VideoActivity.this,ListVideos.class);
				//startActivity(listing);
				
			}
			
		});
		
		//My Video button listener
		my_vid_btn.setOnClickListener(new Button.OnClickListener(){

			@Override
			public void onClick(View v) {
				
				new processBackend().execute("getMyVideos");
				
				//Intent listing=new Intent(VideoActivity.this,ListVideos.class);
				//startActivity(listing);
				
			}
			
		});
		
		//Refresh button listener
		
		refresh_btn.setOnClickListener(new Button.OnClickListener(){

			@Override
			public void onClick(View v) {
				
				//new processBackend().execute("refresh");
				Intent refreshIntent = new Intent(VideoActivity.this,RefreshTokenActivity.class);
				Bundle extras = getIntent().getExtras();
				if (extras != null) {
				  
				   userName = extras.getString("userName");
				   accessToken= extras.getString("accessToken");
				   userId = extras.getString("userId");
				   emailId = extras.getString("emailId");
				   refreshIntent.putExtra("userName", userName);
				   refreshIntent.putExtra("accessToken",accessToken);
				   refreshIntent.putExtra("userId", userId);
				   refreshIntent.putExtra("emailId", emailId);
				   startActivityForResult(refreshIntent, 0);
				}
				
			}
			
		});
		
		//Logout button  listener
		logout_btn.setOnClickListener(new Button.OnClickListener(){

			@Override
			public void onClick(View v) {
				
				new processBackend().execute("logout");
				
			}
			
		});
	}
	
	
	//Return the  path of media file created
	private  Uri getVideoFilePath(int type){

	      return Uri.fromFile(getVideoFile(type));
	}
	
	
	//Generate and return the video file created
	private  File getVideoFile(int type){

	    File videoStorageDir = new File(Environment.getExternalStoragePublicDirectory(
	              Environment.DIRECTORY_PICTURES), "MyVideo");
	    File mediaFile;
	    
	    //check if the directory MyVideo exists if not then create it
	    if (! videoStorageDir.exists()){

	        if (! videoStorageDir.mkdirs()){

	        	vid_output_text.setText("Error in creating directory MyVideo");

	            Toast.makeText(getApplicationContext(),
                        "Error in creating directory MyVideo", Toast.LENGTH_SHORT).show();

	            Log.d("MyVideo", "Error in creating directory MyVideo");
	            return null;
	        }
	    }
	    
	    Date uploadDate= new java.util.Date();
	    String time = new SimpleDateFormat("yyyyMMdd_HHmmss").format(uploadDate.getTime());

	    if(type == VIDEO) {
	        mediaFile = new File(videoStorageDir.getPath() + File.separator +
	        "VID_"+ time + ".mp4");

	    } else {
	        return null;
	    }

	    return mediaFile;
	}
	
	

	//Activity to do after video capturing in our case we are uploading the video file to server
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
	
	    if (requestCode == VIDEO_REQ_CODE) {
	    	
	    	//If video captured sc=uccessfully
	        if (resultCode == RESULT_OK) {

	            vid_output_text.setText("Video File : " +data.getData());
	            uploadFileName = data.getData();
	            
	            String[] file_name_split = uploadFileName.toString().split("//");
	            uploadFilePath = file_name_split[1];
	            
	            String[] file_path_components = uploadFilePath.split("/");
	            video_fileName = file_path_components[file_path_components.length -1];
	            
	            Log.e("video_fileName", video_fileName);
	            
	            Toast.makeText(this, "Captured Video File Saved to" +uploadFilePath, Toast.LENGTH_LONG).show();
	           
	         
	            
	            dialog = ProgressDialog.show(VideoActivity.this, "", "Uploading Video File...", true);
	            
	            new Thread(new Runnable() {
	                    public void run() {
	                    	
	                         runOnUiThread(new Runnable() {
	                        	 	@Override
	                                public void run() {
	                        	 		
	                                }
	                            });                      
	                         
	                         jsonRes = uploadVideoFile(uploadFilePath);
	                         dialog.dismiss(); 
	                         
	                         
	                         //code to add public or private permission to video
	                         Intent InputIntent = new Intent(VideoActivity.this, InputActivity.class);
	                         InputIntent.putExtra("userName", userName);
	                         InputIntent.putExtra("accessToken", accessToken);
	                         InputIntent.putExtra("userId", userId);
	                         InputIntent.putExtra("video_fileName", video_fileName);
	     					 startActivityForResult(InputIntent, INPUT_REQ_CODE);
	     					
	     					
	     					

	                    }
	                  }).start();
	          
	        } 
	        else if(requestCode == INPUT_REQ_CODE){
	        	Log.e("Inside else", "hdsnf");
	        	String value = "";
	        	Bundle extras = getIntent().getExtras();
	    		
	    		if (extras != null) {
	    		   
	    		    value = extras.getString("permission");
	    		    
	    		}
	        	
	        	Log.e("data returned", value);
	        }
	        else if (resultCode == RESULT_CANCELED) {
	        	
	        	// User cancelled the video capture
	        	vid_output_text.setText("User cancelled the video capture.");
	        	Toast.makeText(this, "User have canceled the video capturing", Toast.LENGTH_LONG).show();

	        } 
	        else {
	        	
	        	vid_output_text.setText("Video capture failed.");
	            Toast.makeText(this, "Video capture got failed for some reasons", Toast.LENGTH_LONG).show();
	              
	        }
	    }
	    
	    
	    
	}
	
	//method to upload video file
	public JSONObject uploadVideoFile(String videoFilePath) {
		
		
		//get the user data from login activity
		Bundle extras = getIntent().getExtras();
		JSONObject json = null;
		
		if (extras != null) {
		   
		    userName = extras.getString("userName");
		    accessToken= extras.getString("accessToken");
		    userId = extras.getString("userId");
		    
		}
		
		String fileName = videoFilePath;
	    OutputStream os = null;
		String url = vidURL;
		HttpURLConnection con = null;
	    File sourceFile = new File(videoFilePath); 

		    if (!sourceFile.isFile()) {

		           dialog.dismiss(); 

		           Log.e("uploadFile", "Video File you ae trying to uplaod does not exist :"
		                               +uploadFilePath);

		           runOnUiThread(new Runnable() {
		               public void run() {
		                   vid_output_text.setText("Video File you ae trying to uplaod does not exist :"
		                           +uploadFilePath);
		               }
		           }); 

		           return json;

		    }
		    else{
		    	try{
		    		 json = LibraryFunctions.uploadVideo(sourceFile,vidURL );
		    		 Log.e("JSON_RESPONSE", json.toString());
		    		

		    	}
		    	catch(Exception e){
		    		
		    	}
		    }
		    	//dialog.dismiss();       
		          return json; 
		    
		  
	}
	
	public boolean isExternalStorageWritable() {
	    String state = Environment.getExternalStorageState();
	    if (Environment.MEDIA_MOUNTED.equals(state)) {
	        return true;
	    }
	    return false;
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.video, menu);
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
	
	 //Inner private class to handle all network Request Response Activities
	private class processBackend extends AsyncTask<String, String, JSONObject> {
		String requestType = "logout";
	    @Override
	    protected void onPreExecute() {
	        super.onPreExecute();
	       
	      
	   }

	    @Override
	    protected JSONObject doInBackground(String... args) {
	    	JSONObject jsonRequest = new JSONObject();
	    	Log.e("arg0:",args[0]);
	    	
	    	
	    	if(args[0].equals("logout")){
	    		try {
	    			jsonRequest.put("userName", userName);
					 jsonRequest.put("userId", userId);
					 jsonRequest.put("emailId", emailId);
					 jsonRequest.put("emailID", emailId);
					 jsonRequest.put("accessToken", userId);
					 jsonRequest.put("requestType", "logout");
					 StringEntity se = new StringEntity( jsonRequest.toString());
					 jsonRequest = LibraryFunctions.createURL(requestType, se);
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (UnsupportedEncodingException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
	    		
	    	}
	    	else if(args[0].equals("chnagePermission")){
	    		try {
					 jsonRequest.put("userName", userName);
					 jsonRequest.put("userId", userId);
					 jsonRequest.put("accessToken", userId);
					 jsonRequest.put("requestType", args[0]);
					 jsonRequest.put("preference", preference);
					 
					 StringEntity se = new StringEntity( jsonRequest.toString());
					 jsonRequest = LibraryFunctions.createURL(requestType, se);
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (UnsupportedEncodingException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
	    		
	    	}
	    	else if(args[0].equals("getAllVideos")){
	    		try {
	    			jsonRequest.put("userName", userName);
					 jsonRequest.put("userId", userId);
					 jsonRequest.put("accessToken", userId);
					 jsonRequest.put("requestType", "getAllVideos");
					 StringEntity se = new StringEntity( jsonRequest.toString());
					 jsonRequest = LibraryFunctions.createURL(requestType, se);
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (UnsupportedEncodingException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
	    		
	    	}
	    	
	    	else if(args[0].equals("getMyVideos")){
	    		try {
	    			jsonRequest.put("userName", userName);
					 jsonRequest.put("userId", userId);
					 jsonRequest.put("requestType", "playVideo");
					 jsonRequest.put("name", "test1.mp4");
					 StringEntity se = new StringEntity( jsonRequest.toString());
					 jsonRequest = LibraryFunctions.getVideo(se);
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (UnsupportedEncodingException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
	    		
	    	}
	    	
	    	else if(args[0].equals("getOneVideo")){
	    		try {
	    			jsonRequest.put("userName", userName);
					 jsonRequest.put("userId", userId);
					 jsonRequest.put("requestType", "playVideo");
					 jsonRequest.put("name", "test1.mp4");
					 StringEntity se = new StringEntity( jsonRequest.toString());
					 jsonRequest = LibraryFunctions.getVideo(se);
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (UnsupportedEncodingException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
	    		
	    	}
	    	
	    	else if(args[0].equals("createURL")){
	    		
	    			
					 jsonRequest = LibraryFunctions.createUploadURL();
				
	    		
	    	}
	    	
	    	else if(args[0].equals("upload")){
	    		
    			
				 jsonRequest = LibraryFunctions.createUploadURL();
			
   		
   	}
	    
	        return jsonRequest;
	       
	    	
	    }
	   
	    //method to handle response of the REST apis
	    @Override
	    protected void onPostExecute(JSONObject json) {
	    	
	    	try {
	    		
				if(json.getInt("success") == 1 && json.getInt("error") == 0 ) {
					
					
					if(json.getString("responseType").equals("chnagePermission")){
						Toast.makeText(getApplicationContext(),
		                        json.getString("successMsg"), Toast.LENGTH_LONG).show();
					}
					else if(json.getString("responseType").equals("logout")){
					Intent loginIntent = new Intent(VideoActivity.this, LoginActivity.class);
					
					startActivity(loginIntent);
					}
					else if(json.getString("responseType").equals("getAllVideos")){
						
						JSONArray json_array = json.getJSONArray("videos");
						String[] video_links = new String[json_array.length()];
						//Log.e("json_aray_video", json_array.toString() );
						
						for(int i = 0; i< json_array.length(); i++){
							JSONObject video_object  = new  JSONObject();
							video_object = json_array.getJSONObject(i);
							Log.e("video_object",video_object.toString() );
							
							video_links[i]= video_object.getString("url");
						}
						
						Intent listing=new Intent(VideoActivity.this,ListVideos.class);
						listing.putExtra("linkurl", video_links);
						startActivity(listing);
						
						
					}
					
					else if(json.getString("responseType").equals("getMyVideos")){
						
						String[] video_links = new String[2];
						String filePath = json.getString("filePath");
						video_links[0]= filePath;
						video_links[1]= filePath;
						
						Log.e("filePath", filePath);
					
					
						Intent listing=new Intent(VideoActivity.this,ListVideos.class);
						listing.putExtra("linkurl", video_links);
						startActivity(listing);
						
						
						
						Log.e("VIDEO","I reached here");
						
						
					}
					
					else if(json.getString("responseType").equals("upload")){
						
						vidURL = json.getString("URL");
						
					}
					
					else if(json.getString("responseType").equals("getVideo")){
						String[] video_links = new String[2];
						String filePath = json.getString("filePath");
						video_links[0]= filePath;
						video_links[1]= filePath;
						
						Log.e("filePath", filePath);
					
					
						Intent listing=new Intent(VideoActivity.this,ListVideos.class);
						listing.putExtra("linkurl", video_links);
						startActivity(listing);
						
						
						
						Log.e("VIDEO","I reached here");
						
					}
					
				}
				else if(json.getString("error")=="1"){
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




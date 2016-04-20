package com.example.cloud218;

/**
 * This class implements the REST API calls to send request to the server and get the response back
 * @author Anumeha
 *
 */

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHeader;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.protocol.HTTP;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.Environment;
import android.util.Log;

public class LibraryFunctions {
	
	private static String loginUrl = "https://1-dot-assignment2-905.appspot.com/rest/user";
    private static String vidUrl = "https://1-dot-assignment2-905.appspot.com/rest/video";
    private static String DashboardUrl = "http://10.0.2.2/~shahumangr/test_android.php/";
    private static String chgpassURL = "http://10.0.2.2/~shahumangr/test_android.php/";
	
	
    //Create Request URL
    public static JSONObject createURL( String requestType, StringEntity se){
    	String url ="";
    	
    	if(requestType.equals("permission")){
    		url = vidUrl;
    	}
    	else{
    		url = loginUrl;
    	}
		
		JSONObject json = null;
		json = callAPI(url, se );
		Log.e("json value in createUL is  ",json.toString() );
		Log.e("URL IS  ",url );
		return json;
	}
	
	
	//Send and Receive request
	public static JSONObject callAPI(String url, StringEntity se) {
		InputStream instrm = null;
		JSONObject json = null;
		String data = null;
        
        try {
            
            DefaultHttpClient httpClient = new DefaultHttpClient();
            HttpPost httpPost = new HttpPost(url);
            se.setContentType(new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));
            httpPost.setEntity(se);
           
            //store the response data
            instrm =  httpClient.execute(httpPost).getEntity().getContent();
            Log.e("Response data ", instrm.toString());

        } catch (IOException e) {
            e.printStackTrace();
        }

        json = StremToJSON(instrm);
        return json;

    }
	
	public static JSONObject uploadMetaData(StringEntity se) {
		
		InputStream instrm = null;
		JSONObject json = null;
		String data = null;
        
        try {
            
            DefaultHttpClient httpClient = new DefaultHttpClient();
            HttpPost httpPost = new HttpPost("https://1-dot-assignment2-905.appspot.com/rest/video");
            se.setContentType(new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));
            httpPost.setEntity(se);
           
            //store the response data
            instrm =  httpClient.execute(httpPost).getEntity().getContent();
            Log.e("Response data ", instrm.toString());

        } catch (IOException e) {
            e.printStackTrace();
        }

        json = StremToJSON(instrm);
        return json;
    }
	
@SuppressWarnings("null")
public static JSONObject getVideo(StringEntity se) {
		
		InputStream instrm = null;
		JSONObject jsonData = new JSONObject();
		String data = null;
        
        try {
            
            DefaultHttpClient httpClient = new DefaultHttpClient();
            HttpPost httpPost = new HttpPost("https://1-dot-assignment2-905.appspot.com/rest/video");
            se.setContentType(new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));
            httpPost.setEntity(se);
           
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            
            httpClient.execute(httpPost).getEntity().writeTo(baos);
            
            byte[] bytes = baos.toByteArray();
            
            File file = getVideoFile();
            
            Log.e("filename is", file.toString());
            Long size = new Long(file.length());
            
            
            FileOutputStream out = new FileOutputStream(file);
            out.write(bytes);
            out.close();
            
            Log.e("size of the file  is",size.toString(file.length()));

            Log.e("absolute path is",file.getAbsolutePath());
            
           String fileName = file.toString();
           
            
            Log.e("Byte data ", bytes.toString());
            
            try {
            	jsonData.put("success", 1);
            	jsonData.put("error", 0);
            	jsonData.put("responseType", "getVideo");
            	jsonData.put("filePath", fileName);
    		} catch (JSONException e) {
    			// TODO Auto-generated catch block
    			e.printStackTrace();
    		}

        } catch (IOException e) {
            e.printStackTrace();
        }

        
        
        return jsonData;
    }
	
	public static JSONObject createUploadURL(){
		InputStream instrm = null;
		JSONObject json = null;
		
		HttpClient httpclient = new DefaultHttpClient();    
		HttpConnectionParams.setConnectionTimeout(httpclient.getParams(), 10000); //Timeout Limit

		HttpGet httpGet = new HttpGet("https://1-dot-assignment2-905.appspot.com/rest/upload");
		try {
			//String response = httpclient.execute(httpGet).toString();
			
			//Log.e("GET RESPONSE",response);
			instrm =  httpclient.execute(httpGet).getEntity().getContent();
		} catch (IllegalStateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		json = StremToJSON(instrm);
		return json;
			
	}
	
	
	public static JSONObject uploadVideo(File sourceFile, String vidURL){
		InputStream instrm = null;
		JSONObject json = null;
		HttpResponse response;
		
		HttpClient httpclient = new DefaultHttpClient();
		HttpPost httppost = new HttpPost(vidURL);

		FileBody fileBody  = new FileBody(sourceFile);
		MultipartEntity reqEntity = new MultipartEntity();

		reqEntity.addPart("file", fileBody);

		httppost.setEntity(reqEntity);
		
		try {
			response = httpclient.execute(httppost);
			instrm =  response.getEntity().getContent();
		} catch (ClientProtocolException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		catch (IllegalStateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		
		json = StremToJSON(instrm);
		
		Log.e("RESPONSE", json.toString());
		return json;
		
	      
		
		
		
	}
	public static JSONObject StremToJSON(InputStream instrm){
		
		JSONObject json = null;
		String data = null;
		 try {
	        	
	        	//code to convert the response data to string
	            BufferedReader reader = new BufferedReader(new InputStreamReader(
	            		instrm, "iso-8859-1"), 8);
	            StringBuilder strbldr = new StringBuilder();
	            String line = null;
	            while ((line = reader.readLine()) != null) {
	            	strbldr.append(line + "\n");
	            }
	            instrm.close();
	            data = strbldr.toString();
	            Log.e("JSON RESPONSE", data);
	        } catch (Exception e) {
	            Log.e("Buffer Error", "Error converting result " + e.toString());
	        }

	        // try parse the string to a JSON object
	        try {
	            json = new JSONObject(data);
	        } catch (JSONException e) {
	            Log.e("JSON Parser", "Error parsing data " + e.toString());
	        }

		
		return json;
	}
	
	public static  File getVideoFile(){

	    File videoStorageDir = new File(Environment.getExternalStoragePublicDirectory(
	              Environment.DIRECTORY_PICTURES), "MyVideo");
	    File mediaFile;
	    
	    //check if the directory MyVideo exists if not then create it
	    if (! videoStorageDir.exists()){

	        if (! videoStorageDir.mkdirs()){
	        	Log.d("MyVideo", "Error in creating directory MyVideo");
	            return null;
	        }
	    }
	    
	    Date uploadDate= new java.util.Date();
	    String time = new SimpleDateFormat("yyyyMMdd_HHmmss").format(uploadDate.getTime());

	        mediaFile = new File(videoStorageDir.getPath() + File.separator +
	        "VID_"+ time + ".mp4");

	    
	    return mediaFile;
	}

	
	

}

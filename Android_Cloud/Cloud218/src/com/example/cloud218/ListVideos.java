package com.example.cloud218;

/**
 * This class is used to list all the videos, User can list either his own videos or all the publicly available videos
 * @author Anumeha
 *
 */

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class ListVideos extends ActionBarActivity {
 //Create a listview object
	ListView listView;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_list_videos);
		// initialize the listview
	listView=(ListView)findViewById(R.id.list);
	
	Bundle extras = getIntent().getExtras();
	if (extras != null) {
		String[] linkurl= extras.getStringArray("linkurl");
		
		
	
	
	//fetch the string containing all the url's. For now the url's are hard coded.
	//anu please attach your code here for fetching the urls from the backend
	/*String[] linkurl=new String[]{"http://www.androidbegin.com/tutorial/AndroidCommercial.3gp",
			"http://www.androidbegin.com/tutorial/AndroidCommercial.3gp",
			"http://www.androidbegin.com/tutorial/AndroidCommercial.3gp",
			"http://www.androidbegin.com/tutorial/AndroidCommercial.3gp",
			"http://10.0.2.2/~shahumangr/upload/VID_20141127_144836.mp4"};*/
	
	//create an adapter
	ArrayAdapter<String> adapter=new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1, android.R.id.text1, linkurl);
	listView.setAdapter(adapter);
	
	//on each item click it opens a new intent which plays the video
	listView.setOnItemClickListener(new OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			String abspath= parent.getItemAtPosition(position).toString();
			Intent intent=new Intent();
			
			Log.e("abspathList video",abspath.toString());
			
			intent.setClass(getApplicationContext(), PlayVideos.class);
			intent.putExtra("video",abspath);
			startActivity(intent);
			
			
		}
	});
	}	
	
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.list_videos, menu);
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
}

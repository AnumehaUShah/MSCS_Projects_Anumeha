package com.example.cloud218;

/**
 * This class implements the functionalities for playing the video files.
 * @author Anumeha
 *
 */

import android.support.v7.app.ActionBarActivity;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.MediaController;
import android.widget.VideoView;

public class PlayVideos extends ActionBarActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_play_videos);
		Bundle bundle=getIntent().getExtras();
		String path=bundle.getString("video");
		VideoView vidview=(VideoView)findViewById(R.id.videoView1);
		MediaController mc=new MediaController(this);
		mc.setAnchorView(vidview);
		//Uri video=Uri.parse(path);
	 	Log.d("this is mys path", path);
		vidview.setVideoPath(path);
	 	//vidview.setVideoURI(video);
	 	vidview.setMediaController(mc);
	 	vidview.requestFocus();
	 	vidview.start();
    }

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.play_videos, menu);
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

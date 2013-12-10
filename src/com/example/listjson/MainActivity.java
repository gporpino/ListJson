package com.example.listjson;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.example.listjson.fragments.ListTracksFragment;
import com.example.listjson.fragments.ListTracksFragment.OnTrackSelectedListener;
import com.example.listjson.fragments.TrackDetailsFragment;
import com.example.listjson.model.Track;

public class MainActivity extends Activity implements OnTrackSelectedListener {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu items for use in the action bar
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.main, menu);
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle presses on the action bar items
		switch (item.getItemId()) {
		case R.id.action_settings:
			// openSearch();
			return true;
		case R.id.action_refresh:
			reloadList();
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	private void reloadList() {
		ListTracksFragment listFragment = (ListTracksFragment) getFragmentManager()
				.findFragmentById(R.id.tracks_fragment);

		if (listFragment != null) {
			listFragment.reloadList();
		}

		TrackDetailsFragment detailsFragment = (TrackDetailsFragment) getFragmentManager()
				.findFragmentById(R.id.details_fragment);

		if (detailsFragment != null) {
			detailsFragment.clear();
		}
	}

	@Override
	public void onTrackSelected(Track track) {

		TrackDetailsFragment fragment = (TrackDetailsFragment) getFragmentManager()
				.findFragmentById(R.id.details_fragment);

		if (fragment != null) {
			// If article frag is available, we're in two-pane layout...

			// Call a method in the ArticleFragment to update its content
			fragment.updateTrackDetails(track);
		} else {
			Intent intent = new Intent(this, TrackDetailsActivity.class);

			intent.putExtra(TrackDetailsFragment.TRACK_ID, track.getId());
			startActivity(intent);
		}
	}

}

package com.example.listjson;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;

import com.example.listjson.fragments.TrackDetailsFragment;

public class TrackDetailsActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Fragment f = TrackDetailsFragment.newInstance(getIntent()
				.getStringExtra(TrackDetailsFragment.TRACK_ID));
		getFragmentManager().beginTransaction().add(android.R.id.content, f)
				.commit();
	}

}

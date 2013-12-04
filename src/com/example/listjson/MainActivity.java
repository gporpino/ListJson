package com.example.listjson;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.AsyncTask.Status;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.listjson.model.Track;

public class MainActivity extends Activity {

	private static final String JSON_URL = "http://ws.audioscrobbler.com/2.0/?method=artist.gettoptracks&artist=Rush&api_key=539d8eae9bc84055e36a301da71d1afa&format=json";

	private ListView listView;
	private View loadingView;

	private static int mShortAnimationDuration;

	private DownloadWebpageTask task;

	private ArrayList<Track> tracks;

	private Menu menu;

	@Override
	@Deprecated
	public Object onRetainNonConfigurationInstance() {

		return task;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		loadConfigurations();

		listView = (ListView) findViewById(R.id.listView);
		loadingView = findViewById(R.id.loading_spinner);

		updateList(savedInstanceState);
	}

	private void loadConfigurations() {
		mShortAnimationDuration = getResources().getInteger(
				android.R.integer.config_shortAnimTime);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);

		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle presses on the action bar items
		switch (item.getItemId()) {
		case R.id.action_settings:
			// openSearch();
			return true;
		case R.id.action_refresh:
			reloadContent();
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	private void animateLoadingView() {
		ValueAnimator animator = ObjectAnimator.ofFloat(loadingView,
				View.ROTATION, 0f, 360f);

		animator.setDuration(1000);

		animator.setRepeatMode(ValueAnimator.RESTART);
		animator.setRepeatCount(ValueAnimator.INFINITE);

		animator.setInterpolator(new LinearInterpolator());

		animator.start();
	}

	private void updateList(Bundle savedInstanceState) {

		task = (DownloadWebpageTask) getLastNonConfigurationInstance();
		if (task != null && task.getStatus() != Status.FINISHED) {
			task.setContentView(listView);
			task.setLoadingView(loadingView);

			if (task.getStatus() == Status.FINISHED) {
				loadingView.setVisibility(View.GONE);
			} else {
				animateLoadingView();

			}
		} else {
			if (savedInstanceState == null) {
				reloadContent();
				return;
			}

			tracks = savedInstanceState.getParcelableArrayList("tracks");
			if (tracks == null) {
				reloadContent();
			} else {
				fillContent(tracks);
				loadingView.setVisibility(View.GONE);
			}

		}

	}

	private void reloadContent() {
		if (task != null && task.getStatus() != Status.FINISHED) {
			return;
		}
		ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();

		if (networkInfo != null && networkInfo.isConnected()) {
			tracks = null;

			task = new DownloadWebpageTask();

			task.setContentView(listView);
			task.setLoadingView(loadingView);

			task.execute(JSON_URL);
		} else {

			List<String> errors = new ArrayList<String>();
			errors.add("Check if you are connected");

			listView.setAdapter(new ArrayAdapter<String>(this,
					android.R.layout.simple_list_item_1, errors));
		}
	}

	private void fillContent(final ArrayList<Track> result) {
		ArrayAdapter<Track> adapter = new ArrayAdapter<Track>(this,
				android.R.layout.simple_list_item_1, result);

		listView.setAdapter(adapter);
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		outState.putParcelableArrayList("tracks", tracks);

		super.onSaveInstanceState(outState);
	}

	private static class DownloadWebpageTask extends
			AsyncTask<String, Void, ArrayList<Track>> {
		private ValueAnimator animator;

		private View loadingView;
		private ListView contentView;

		public void setContentView(ListView contentView) {
			this.contentView = contentView;
		}

		public void setLoadingView(View loadingView) {
			this.loadingView = loadingView;
		}

		@Override
		protected void onPreExecute() {
			if (contentView == null || loadingView == null)
				return;

			loadingView.setVisibility(View.VISIBLE);

			contentView.animate().alpha(0f)
					.setDuration(mShortAnimationDuration);

			loadingView.animate().alpha(1f)
					.setDuration(mShortAnimationDuration);

			animateLoadingView();

			super.onPreExecute();
		}

		private void animateLoadingView() {
			animator = ObjectAnimator.ofFloat(loadingView, View.ROTATION, 0f,
					360f);

			animator.setDuration(1000);

			animator.setRepeatMode(ValueAnimator.RESTART);
			animator.setRepeatCount(ValueAnimator.INFINITE);

			animator.setInterpolator(new LinearInterpolator());

			animator.start();
		}

		@Override
		protected ArrayList<Track> doInBackground(String... urls) {
			try {
				Thread.sleep(3000);
			} catch (InterruptedException e1) {

				e1.printStackTrace();
			}

			try {
				return new JsonParser<ArrayList<Track>>(urls[0],
						new TrackJsonConverter()).parse();

			} catch (IOException e) {

				List<String> errors = new ArrayList<String>();
				errors.add("Problem to download content.");

				contentView.setAdapter(new ArrayAdapter<String>(contentView
						.getContext(), android.R.layout.simple_list_item_1,
						errors));
				return null;
			} catch (JSONException e) {
				List<String> errors = new ArrayList<String>();
				errors.add("Problem load json properties. ");

				contentView.setAdapter(new ArrayAdapter<String>(contentView
						.getContext(), android.R.layout.simple_list_item_1,
						errors));
				return null;
			}
		}

		// onPostExecute displays the results of the AsyncTask.
		@Override
		protected void onPostExecute(final ArrayList<Track> result) {

			if (contentView == null || loadingView == null)
				return;

			MainActivity main = (MainActivity) contentView.getContext();

			main.tracks = result;

			fillContent(main.tracks);

			loadingView.animate().alpha(0f)
					.setDuration(mShortAnimationDuration);

			contentView.animate().alpha(1f)
					.setDuration(mShortAnimationDuration);

			animator.cancel();
		}

		private void fillContent(final ArrayList<Track> result) {
			ArrayAdapter<Track> adapter = new ArrayAdapter<Track>(
					contentView.getContext(),
					android.R.layout.simple_list_item_1, result);

			contentView.setAdapter(adapter);
		}

	}

}

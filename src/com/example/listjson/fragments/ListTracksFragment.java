package com.example.listjson.fragments;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.app.ListFragment;
import android.app.LoaderManager.LoaderCallbacks;
import android.content.AsyncTaskLoader;
import android.content.Context;
import android.content.Loader;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.LinearInterpolator;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.listjson.R;
import com.example.listjson.json.converters.TracksJsonConverter;
import com.example.listjson.json.parsers.JsonParser;
import com.example.listjson.model.ImageSize;
import com.example.listjson.model.Track;

public class ListTracksFragment extends ListFragment implements
		LoaderCallbacks<List<Track>> {

	OnTrackSelectedListener mCallback;

	public interface OnTrackSelectedListener {
		public void onTrackSelected(Track track);
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);

		// This makes sure that the container activity has implemented
		// the callback interface. If not, it throws an exception
		try {
			mCallback = (OnTrackSelectedListener) activity;
		} catch (ClassCastException e) {
			throw new ClassCastException(activity.toString()
					+ " must implement OnTrackSelectedListener");
		}
	}

	private static final String JSON_URL = "http://ws.audioscrobbler.com/2.0/?method=artist.gettoptracks&artist=Rush&api_key=539d8eae9bc84055e36a301da71d1afa&format=json";

	private ListView listView;
	private View loadingView;

	private ObjectAnimator animator;

	private ArrayAdapter<Track> mAdapter;

	private static int mShortAnimationDuration;

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		mAdapter = new ArrayAdapter<Track>(this.getActivity(), 0) {

			@Override
			public View getView(int position, View convertView, ViewGroup parent) {
				LayoutInflater inflater = (LayoutInflater) getActivity()
						.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				View rowView = inflater.inflate(
						R.layout.adapter_item_track_list, parent, false);

				ImageView imageView = (ImageView) rowView
						.findViewById(R.id.item_icon);

				TextView textView = (TextView) rowView
						.findViewById(R.id.item_content);

				Track track = getItem(position);

				new DownloadImageTask(imageView).execute(track.getImageInfo(
						ImageSize.SMALL).getUrl());

				textView.setText(track.getName());

				return rowView;
			};
		};

		setListAdapter(mAdapter);

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View view = inflater
				.inflate(R.layout.fragment_tracks, container, false);

		listView = (ListView) view.findViewById(android.R.id.list);
		loadingView = view.findViewById(R.id.list_loading_spinner2);

		loadList();

		return view;
	}

	@Override
	public void onListItemClick(ListView l, View v, int position, long id) {

		Track track = (Track) l.getAdapter().getItem(position);

		mCallback.onTrackSelected(track);
	}

	private void configAnimatorForLoadingView() {

		loadConfigurations();

		animator = ObjectAnimator.ofFloat(loadingView, View.ROTATION, 0f, 360f);

		animator.setDuration(1000);

		animator.setRepeatMode(ValueAnimator.RESTART);
		animator.setRepeatCount(ValueAnimator.INFINITE);

		animator.setInterpolator(new LinearInterpolator());
		animator.start();

	}

	private void loadConfigurations() {
		mShortAnimationDuration = getResources().getInteger(
				android.R.integer.config_shortAnimTime);
	}

	@Override
	public Loader<List<Track>> onCreateLoader(int id, Bundle args) {
		return new TopTracksLoader(this.getActivity());
	}

	private void loadList() {

		configAnimatorForLoadingView();

		listView.animate().alpha(0f).setDuration(mShortAnimationDuration);
		loadingView.animate().alpha(1f).setDuration(mShortAnimationDuration);

		animator.start();

		this.getLoaderManager().initLoader(0, null, this);

	}

	public void reloadList() {

		listView.animate().alpha(0f).setDuration(mShortAnimationDuration);
		loadingView.animate().alpha(1f).setDuration(mShortAnimationDuration);

		animator.start();

		this.getLoaderManager().restartLoader(0, null, this);

	}

	private void fillContent(final List<Track> result) {

		mAdapter.clear();
		mAdapter.addAll(result);
		listView.setVisibility(ListView.VISIBLE);

	}

	@Override
	public void onLoadFinished(Loader<List<Track>> loader, List<Track> tracks) {

		fillContent(tracks);

		loadingView.animate().alpha(0f).setDuration(mShortAnimationDuration);
		listView.animate().alpha(1f).setDuration(mShortAnimationDuration);

		animator.cancel();

	}

	@Override
	public void onLoaderReset(Loader<List<Track>> arg0) {
		listView.animate().alpha(0f).setDuration(mShortAnimationDuration);

		loadingView.animate().alpha(1f).setDuration(mShortAnimationDuration);

		configAnimatorForLoadingView();
	}

	private static class TopTracksLoader extends AsyncTaskLoader<List<Track>> {

		private List<Track> mData;

		public TopTracksLoader(Context context) {
			super(context);
		}

		@Override
		protected void onStartLoading() {

			if (mData != null) {
				deliverResult(mData);
			} else {
				forceLoad();
			}
		}

		@Override
		public List<Track> loadInBackground() {

			try {
				mData = new JsonParser<ArrayList<Track>>(JSON_URL,
						new TracksJsonConverter()).parse();

				return mData;
			} catch (IOException e) {
				return null;
			} catch (JSONException e) {
				return null;
			}

		}

	}

	private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
		ImageView view;

		public DownloadImageTask(ImageView bmImage) {
			this.view = bmImage;

		}

		@Override
		protected Bitmap doInBackground(String... urls) {
			String urldisplay = urls[0];
			Bitmap bitmap = null;
			try {
				InputStream in = new java.net.URL(urldisplay).openStream();
				bitmap = BitmapFactory.decodeStream(in);
			} catch (Exception e) {
				e.printStackTrace();
			}
			return bitmap;
		}

		@Override
		protected void onPostExecute(Bitmap result) {
			view.setImageBitmap(result);
		}
	}

}

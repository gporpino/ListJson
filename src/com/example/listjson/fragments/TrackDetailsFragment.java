package com.example.listjson.fragments;

import java.io.IOException;
import java.security.InvalidParameterException;

import org.json.JSONException;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.app.Fragment;
import android.app.LoaderManager.LoaderCallbacks;
import android.content.AsyncTaskLoader;
import android.content.Context;
import android.content.Loader;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.LinearInterpolator;
import android.widget.ListView;
import android.widget.TextView;

import com.example.listjson.R;
import com.example.listjson.json.converters.TrackJsonConverter;
import com.example.listjson.json.parsers.JsonParser;
import com.example.listjson.model.Track;

public class TrackDetailsFragment extends Fragment implements
		LoaderCallbacks<Track> {

	private static final String JSON_URL = "http://ws.audioscrobbler.com/2.0/?method=track.getInfo&api_key=a039b12d7ad86686f6ca34b880c94200&mbid={mbid}&format=json";

	private static int mShortAnimationDuration;

	public static final String TRACK_ID = "mbid";

	private View contentView;
	private View loadingView;
	private ObjectAnimator animator;

	private String currentTrackId;

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		configAnimatorForLoadingView();

		contentView.animate().alpha(0f).setDuration(mShortAnimationDuration);
		loadingView.animate().alpha(0f).setDuration(mShortAnimationDuration);

		Bundle extras = getActivity().getIntent().getExtras();

		if (extras == null) {
			return;
		}

		currentTrackId = extras.getString(TrackDetailsFragment.TRACK_ID);
		if (currentTrackId == null) {
			throw new InvalidParameterException(
					"Parameter TrackDetailsFragment.TRACK_ID must be especified.");
		}

		loadContent();

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View view = inflater.inflate(R.layout.fragment_track_details,
				container, false);

		loadingView = view.findViewById(R.id.detail_loading_spinner);
		contentView = view.findViewById(android.R.id.content);

		return view;
	}

	public void clear() {
		contentView.animate().alpha(0f).setDuration(mShortAnimationDuration);
		loadingView.animate().alpha(0f).setDuration(mShortAnimationDuration);

		TextView trackNameTextView = (TextView) contentView
				.findViewById(R.id.track_name);
		trackNameTextView.setText(null);

		TextView trackArtistNameTextView = (TextView) contentView
				.findViewById(R.id.track_artist_name);
		trackArtistNameTextView.setText(null);
	}

	private void configAnimatorForLoadingView() {

		loadConfigurations();

		animator = ObjectAnimator.ofFloat(loadingView, View.ROTATION, 0f, 360f);

		animator.setDuration(1000);

		animator.setRepeatMode(ValueAnimator.RESTART);
		animator.setRepeatCount(ValueAnimator.INFINITE);

		animator.setInterpolator(new LinearInterpolator());

	}

	private void loadConfigurations() {
		mShortAnimationDuration = getResources().getInteger(
				android.R.integer.config_shortAnimTime);
	}

	@Override
	public Loader<Track> onCreateLoader(int id, Bundle args) {
		return new TrackLoader(this.getActivity(), currentTrackId);
	}

	private void loadContent() {

		contentView.animate().alpha(0f).setDuration(mShortAnimationDuration);
		loadingView.animate().alpha(1f).setDuration(mShortAnimationDuration);

		animator.start();

		this.getLoaderManager().initLoader(0, null, this);

	}

	private void reloadList() {

		contentView.animate().alpha(0f).setDuration(mShortAnimationDuration);
		loadingView.animate().alpha(1f).setDuration(mShortAnimationDuration);

		animator.start();

		this.getLoaderManager().restartLoader(0, null, this);

	}

	private void fillContent(final Track result) {

		contentView.setVisibility(ListView.VISIBLE);

		TextView trackNameTextView = (TextView) contentView
				.findViewById(R.id.track_name);
		trackNameTextView.setText(result.getName());

		TextView trackArtistNameTextView = (TextView) contentView
				.findViewById(R.id.track_artist_name);
		trackArtistNameTextView.setText(result.getArtist().getName());

	}

	@Override
	public void onLoadFinished(Loader<Track> loader, Track track) {

		fillContent(track);

		loadingView.animate().alpha(0f).setDuration(mShortAnimationDuration);
		contentView.animate().alpha(1f).setDuration(mShortAnimationDuration);

		animator.cancel();

	}

	@Override
	public void onLoaderReset(Loader<Track> arg0) {
		contentView.animate().alpha(0f).setDuration(mShortAnimationDuration);

		loadingView.animate().alpha(1f).setDuration(mShortAnimationDuration);

		configAnimatorForLoadingView();
	}

	public void updateTrackDetails(Track track) {
		this.currentTrackId = track.getId();
		reloadList();
	}

	private static class TrackLoader extends AsyncTaskLoader<Track> {

		private Track mData;
		private String trackId;

		public TrackLoader(Context context) {
			super(context);
		}

		public TrackLoader(Activity context, String trackId) {
			super(context);
			this.trackId = trackId;
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
		public Track loadInBackground() {

			String url = JSON_URL.replace("{mbid}", trackId);

			try {
				mData = new JsonParser<Track>(url, new TrackJsonConverter())
						.parse();

				return mData;
			} catch (IOException e) {
				return null;
			} catch (JSONException e) {
				return null;
			}

		}
	}

}

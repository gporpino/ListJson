package com.example.listjson.json.converters;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.example.listjson.model.Artist;
import com.example.listjson.model.ImageInfo;
import com.example.listjson.model.ImageSize;
import com.example.listjson.model.Track;

public class TrackJsonConverter implements JsonConverter<Track> {

	private static final String JSON_ATTRIBUTE_ALBUM = "album";
	private static final String JSON_ATTRIBUTE_IMAGE = "image";
	private static final String JSON_ATTRIBUTE_ARTIST = "artist";
	private static final String JSON_ATTRIBUTE_ID = "mbid";
	private static final String JSON_ATTRIBUTE_NAME = "name";
	private static final String JSON_ATTRIBUTE_DURATION = "duration";
	private static final String JSON_ATTRIBUTE_PLAYCOUNT = "playcount";
	private static final String JSON_ATTRIBUTE_TRACK = "track";
	private static final String JSON_ATTRIBUTE_URL = "#text";
	private static final String JSON_ATTRIBUTE_SIZE = "size";

	@Override
	public Track convert(JSONObject json) throws JSONException {

		try {
			JSONObject trackJson = json.getJSONObject(JSON_ATTRIBUTE_TRACK);
			return parseTrack(trackJson);
		} catch (JSONException e) {
			return parseTrack(json);
		}

	}

	private Track parseTrack(JSONObject trackJson) throws JSONException {
		Track track = new Track();

		track.setId(trackJson.getString(JSON_ATTRIBUTE_ID));
		track.setName(trackJson.getString(JSON_ATTRIBUTE_NAME));
		track.setDuration(trackJson.getInt(JSON_ATTRIBUTE_DURATION));
		track.setPlaycount(trackJson.getInt(JSON_ATTRIBUTE_PLAYCOUNT));

		if (trackJson.has(JSON_ATTRIBUTE_ARTIST)) {
			track.setArtist(parseArtist(trackJson
					.getJSONObject(JSON_ATTRIBUTE_ARTIST)));
		}

		if (trackJson.has(JSON_ATTRIBUTE_IMAGE)) {
			track.setImages(parseImages(trackJson
					.getJSONArray(JSON_ATTRIBUTE_IMAGE)));
		} else if (trackJson.has(JSON_ATTRIBUTE_ALBUM)) {
			JSONObject album = trackJson.getJSONObject(JSON_ATTRIBUTE_ALBUM);
			track.setImages(parseImages(album
					.getJSONArray(JSON_ATTRIBUTE_IMAGE)));
		}

		return track;
	}

	private List<ImageInfo> parseImages(JSONArray jsonArray)
			throws JSONException {

		List<ImageInfo> images = new ArrayList<ImageInfo>();

		for (int i = 0; i < jsonArray.length(); i++) {

			images.add(parseImage(jsonArray.getJSONObject(i)));

		}
		return images;

	}

	private ImageInfo parseImage(JSONObject jsonObject) throws JSONException {
		ImageInfo image = new ImageInfo();

		image.setUrl(jsonObject.getString(JSON_ATTRIBUTE_URL));

		String textSize = jsonObject.getString(JSON_ATTRIBUTE_SIZE);
		image.setSize(ImageSize.findByText(textSize));

		return image;
	}

	private Artist parseArtist(JSONObject jsonObject) throws JSONException {
		Artist artist = new Artist();

		artist.setName(jsonObject.getString(JSON_ATTRIBUTE_NAME));

		return artist;
	}

}

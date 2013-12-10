package com.example.listjson.json.converters;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.example.listjson.model.Track;

public class TracksJsonConverter implements JsonConverter<ArrayList<Track>> {

	private static final String TOPTRACKS = "toptracks";

	private static final String JSON_ATTRIBUTE_TRACK = "track";

	private final TrackJsonConverter trackConverter;

	public TracksJsonConverter() {
		trackConverter = new TrackJsonConverter();
	}

	@Override
	public ArrayList<Track> convert(JSONObject json) throws JSONException {
		ArrayList<Track> tracks = new ArrayList<Track>();
		JSONObject toptracks = json.getJSONObject(TOPTRACKS);

		JSONArray trackArray = toptracks.getJSONArray(JSON_ATTRIBUTE_TRACK);

		for (int i = 0; i < trackArray.length(); i++) {
			Track track = trackConverter.convert(trackArray.getJSONObject(i));
			tracks.add(track);
		}

		return tracks;
	}

}

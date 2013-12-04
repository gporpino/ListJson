package com.example.listjson;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.example.listjson.model.Artist;
import com.example.listjson.model.Track;

public class TrackJsonConverter implements JsonConverter<ArrayList<Track>> {

	private static final String JSON_ATTRIBUTE_ARTIST = "artist";
	private static final String JSON_ATTRIBUTE_TRACK = "track";
	private static final String JSON_ATTRIBUTE_NAME = "name";
	private static final String JSON_ATTRIBUTE_DURATION = "duration";
	private static final String JSON_ATTRIBUTE_PLAYCOUNT = "playcount";

	@Override
	public ArrayList<Track> convert(JSONObject json) throws JSONException {
		ArrayList<Track> tracks = new ArrayList<Track>();
		JSONObject toptracks = json.getJSONObject("toptracks");

		JSONArray trackArray = toptracks.getJSONArray(JSON_ATTRIBUTE_TRACK);

		for (int i = 0; i < trackArray.length(); i++) {
			Track track = parseTrack(trackArray.getJSONObject(i));
			tracks.add(track);
		}

		return tracks;
	}

	private Track parseTrack(JSONObject trackJson) throws JSONException {
		Track track = new Track();

		track.setName(trackJson.getString(JSON_ATTRIBUTE_NAME));
		track.setDuration(trackJson.getInt(JSON_ATTRIBUTE_DURATION));
		track.setPlaycount(trackJson.getInt(JSON_ATTRIBUTE_PLAYCOUNT));
		track.setArtist(parseArtist(trackJson
				.getJSONObject(JSON_ATTRIBUTE_ARTIST)));
		return track;
	}

	private Artist parseArtist(JSONObject jsonObject) throws JSONException {
		Artist artist = new Artist();

		artist.setName(jsonObject.getString(JSON_ATTRIBUTE_NAME));

		return artist;
	}

}

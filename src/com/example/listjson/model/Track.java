package com.example.listjson.model;

import android.os.Parcel;
import android.os.Parcelable;

public class Track implements Parcelable {
	private String name;
	private Integer duration;
	private Integer playcount;

	private Artist artist;

	private Track(Parcel in) {
		name = in.readString();
		duration = in.readInt();
		playcount = in.readInt();
		artist = (Artist) in.readSerializable();
	}

	public Track() {
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getDuration() {
		return duration;
	}

	public void setDuration(Integer duration) {
		this.duration = duration;
	}

	public Integer getPlaycount() {
		return playcount;
	}

	public void setPlaycount(Integer playcount) {
		this.playcount = playcount;
	}

	public Artist getArtist() {
		return artist;
	}

	public void setArtist(Artist artist) {
		this.artist = artist;
	}

	@Override
	public String toString() {

		return name;
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(name);
		dest.writeInt(duration);
		dest.writeInt(playcount);
		dest.writeSerializable(artist);

	}

	public static final Parcelable.Creator<Track> CREATOR = new Parcelable.Creator<Track>() {
		@Override
		public Track createFromParcel(Parcel in) {
			return new Track(in);
		}

		@Override
		public Track[] newArray(int size) {
			return new Track[size];
		}
	};
}

package com.example.listjson.model;

import java.util.List;

import android.os.Parcel;
import android.os.Parcelable;

public class Track implements Parcelable {

	private String id;

	private String name;
	private Integer duration;
	private Integer playcount;

	private Artist artist;

	private List<ImageInfo> images;

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
		dest.writeParcelableArray(images.toArray(new ImageInfo[images.size()]),
				Parcelable.PARCELABLE_WRITE_RETURN_VALUE);

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

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public List<ImageInfo> getImages() {
		return images;
	}

	public void setImages(List<ImageInfo> images) {
		this.images = images;
	}

	public ImageInfo getSmallestImageInfo() {
		ImageInfo smallest = null;
		int smallestOrder = ImageSize.values().length + 1;

		if (images != null) {

			for (ImageInfo info : images) {
				if (info.getSize().ordinal() < smallestOrder) {
					smallest = info;
					smallestOrder = info.getSize().ordinal();
				}
			}

		}
		return smallest;
	}

	public ImageInfo getBiggestImageInfo() {
		ImageInfo biggest = null;
		int biggestOrder = -1;

		if (images != null) {

			for (ImageInfo info : images) {
				if (info.getSize().ordinal() > biggestOrder) {
					biggest = info;
					biggestOrder = info.getSize().ordinal();
				}
			}

		}
		return biggest;
	}
}

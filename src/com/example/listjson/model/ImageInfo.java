package com.example.listjson.model;

import android.os.Parcel;
import android.os.Parcelable;

public class ImageInfo implements Parcelable {

	public ImageInfo() {

	}

	public ImageInfo(Parcel in) {

	}

	private String url;
	private ImageSize size;

	public ImageSize getSize() {
		return size;
	}

	public void setSize(ImageSize size) {
		this.size = size;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	@Override
	public int describeContents() {

		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(url);
		dest.writeSerializable(size);

	}

	public static final Parcelable.Creator<ImageInfo> CREATOR = new Parcelable.Creator<ImageInfo>() {
		@Override
		public ImageInfo createFromParcel(Parcel in) {
			return new ImageInfo(in);
		}

		@Override
		public ImageInfo[] newArray(int size) {
			return new ImageInfo[size];
		}
	};
}

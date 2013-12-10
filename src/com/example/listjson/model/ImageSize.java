package com.example.listjson.model;

public enum ImageSize {
	SMALL("small"), MEDIUM("medium"), LARGE("large"), EXTRA_LARGE("extralarge");

	private final String text;

	private ImageSize(String text) {
		this.text = text;
	}

	public String getText() {
		return text;
	}

	public static ImageSize findByText(String text) {
		for (ImageSize imageSize : values()) {
			if (imageSize.getText().equals(text)) {
				return imageSize;
			}
		}
		return null;
	}
}

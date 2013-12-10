package com.example.listjson.json.parsers;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import org.json.JSONException;
import org.json.JSONObject;

import com.example.listjson.json.converters.JsonConverter;

public class JsonParser<T> {

	private final String url_string;
	private final JsonConverter<T> converter;

	public JsonParser(String url, JsonConverter<T> converter) {
		this.url_string = url;
		this.converter = converter;
	}

	// Given a URL, establishes an HttpUrlConnection and retrieves
	// the web page content as a InputStream, which it returns as
	// a string.
	public T parse() throws IOException, JSONException {
		InputStream is = null;

		try {
			URL url = new URL(this.url_string);

			HttpURLConnection conn = (HttpURLConnection) url.openConnection();

			conn.setReadTimeout(10000 /* milliseconds */);
			conn.setConnectTimeout(15000 /* milliseconds */);

			conn.setRequestMethod("GET");
			conn.setDoInput(true);

			conn.connect();

			String result = convertInputStreamIntoString(conn.getInputStream());

			return convertJsonStringToObjects(result);

		} finally {
			if (is != null) {
				is.close();
			}
		}
	}

	private String convertInputStreamIntoString(InputStream in)
			throws IOException {

		StringBuilder out = new StringBuilder();
		BufferedReader br = new BufferedReader(new InputStreamReader(in));
		for (String line = br.readLine(); line != null; line = br.readLine())
			out.append(line);
		br.close();
		return out.toString();

	}

	private T convertJsonStringToObjects(String stringJson)
			throws JSONException {

		JSONObject json = new JSONObject(stringJson);

		return converter.convert(json);

	}
}

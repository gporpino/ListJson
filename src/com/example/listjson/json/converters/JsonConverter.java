package com.example.listjson.json.converters;

import org.json.JSONException;
import org.json.JSONObject;

public interface JsonConverter<T> {

	T convert(JSONObject object) throws JSONException;

}

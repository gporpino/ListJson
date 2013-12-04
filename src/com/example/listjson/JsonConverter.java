package com.example.listjson;

import org.json.JSONException;
import org.json.JSONObject;

public interface JsonConverter<T> {

	T convert(JSONObject reader) throws JSONException;

}

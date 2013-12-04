package com.example.listjson.model;

import java.io.Serializable;

public class Artist implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -9076781361325096833L;

	private String name;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

}

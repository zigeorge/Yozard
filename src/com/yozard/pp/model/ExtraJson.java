package com.yozard.pp.model;

import java.util.Vector;

import com.google.gson.annotations.SerializedName;

public class ExtraJson {
	@SerializedName("array")
	Vector<Extras> array;

	public Vector<Extras> getExtras() {
		return array;
	}

	public void setExtras(Vector<Extras> extras) {
		this.array = extras;
	}

}

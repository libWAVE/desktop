package com.libwave.desktop.api;

import java.io.Serializable;

@SuppressWarnings("serial")
public class BooleanWrapper implements Serializable {

	private boolean value;

	public boolean isValue() {
		return value;
	}

	public BooleanWrapper(boolean value) {
		super();
		this.value = value;
	}

	public BooleanWrapper() {
		super();
	}

}

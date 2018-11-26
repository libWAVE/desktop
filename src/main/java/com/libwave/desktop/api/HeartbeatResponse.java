package com.libwave.desktop.api;

@SuppressWarnings("serial")
public class HeartbeatResponse extends BaseModelObject {

	public HeartbeatResponse() {
		super();
	}

	@Override
	public String getType() {
		return "heartbeat";
	}

	@Override
	public String toString() {
		return "HeartbeatResponse [getType()=" + getType() + "]";
	}

}
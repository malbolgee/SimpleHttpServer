package com.malbolge.httpserver.config;

public class Configuration {
	private int mPort;
	private String mWebRoot;

	public int getPort() {
		return mPort;
	}

	public void setPort(int port) {
		this.mPort = port;
	}

	public String getWebroot() {
		return mWebRoot;
	}

	public void setWebroot(String webRoot) {
		this.mWebRoot = webRoot;
	}
}

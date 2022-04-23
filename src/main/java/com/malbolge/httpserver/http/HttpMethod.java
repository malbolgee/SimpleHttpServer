package com.malbolge.httpserver.http;

public enum HttpMethod {
	GET, HEAD;

	public static final int MAX_LENGTH;

	static {
		int tmpMaxLength = -1;
		for (HttpMethod method : values()) {
			if (method.name().length() > tmpMaxLength) {
				tmpMaxLength = method.name().length();
			}
		}
		MAX_LENGTH = tmpMaxLength;
	}
}
